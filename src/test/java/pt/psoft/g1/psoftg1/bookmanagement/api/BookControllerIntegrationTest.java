package pt.psoft.g1.psoftg1.bookmanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.factories.BookFactory;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.LibraryApi;
import pt.psoft.g1.psoftg1.genremanagement.factories.GenreFactory;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookFactory bookFactory;

    @Autowired
    private GenreFactory genreFactory;

    @MockBean
    private LibraryApi libraryApi;

    @MockBean
    private FileStorageService fileStorageService;

    private Genre testGenre;
    private Author testAuthor;

    // Contador para ISBNs únicos
    private static final AtomicInteger isbnCounter = new AtomicInteger(1000);

    @BeforeEach
    void setUp() {
        reset(libraryApi);

        testGenre = genreRepository.findByString("TECHNOLOGY")
                .orElseGet(() -> createTestGenre("TECHNOLOGY"));
        testAuthor = authorRepository.searchByNameName("Robert C Martin")
                .stream()
                .findFirst()
                .orElseGet(() -> createTestAuthor("Robert C Martin"));
    }

    // ===========================================
    // BUSCA ISBN - TESTES
    // ===========================================

    @Test
    @WithMockUser(roles = "READER")
    void searchIsbn_WhenBookFoundInExternalApi_ReturnsIsbn() throws Exception {
        String title = "Clean Code";
        String expectedIsbn = "9780132350884";

        when(libraryApi.findIsbnByTitle(anyString()))
                .thenReturn(Optional.of(expectedIsbn));

        mockMvc.perform(get("/api/books/isbn")
                        .param("title", title))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedIsbn));

    }

    @Test
    @WithMockUser(roles = "READER")
    void searchIsbn_WhenBookNotFound_Returns404() throws Exception {
        String title = "NonExistent";

        when(libraryApi.findIsbnByTitle(anyString()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/isbn")
                        .param("title", title))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void createBook_WithValidData_CreatesSuccessfully() throws Exception {
        String uniqueIsbn = "9780132350884";

        String bookJson = String.format("""
            {
                "title": "Clean Code",
                "description": "A Handbook of Agile Software Craftsmanship",
                "genre": "%s",
                "authors": [%d]
            }
            """, testGenre.getGenre(), testAuthor.getId());

        mockMvc.perform(put("/api/books/{isbn}", uniqueIsbn)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andDo(print())  // opcional: imprime a resposta para debug
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn").value(uniqueIsbn))
                .andExpect(jsonPath("$.title").value("Clean Code"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void createBook_WithDuplicateIsbn_Returns400() throws Exception {
        String duplicateIsbn = "9780134685991";

        // Cria o primeiro livro
        Book existingBook = createTestBook(duplicateIsbn, "First Book");

        // Tenta criar segundo com mesmo ISBN
        String bookJson = String.format("""
                {
                    "isbn": "%s",
                    "title": "Second Book",
                    "genreId": %d,
                    "authorIds": [%d]
                }
                """, duplicateIsbn, testGenre.getPk(), testAuthor.getId());

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().is4xxClientError()); // 400 ou 409
    }

    @Test
    @WithMockUser(roles = "READER")
    void findByIsbn_WhenBookExists_ReturnsBookView() throws Exception {
        // Cria dados reais no repository
        Book book = createTestBook("9781503715233", "Spider Man");

        // Não mockar bookService
        mockMvc.perform(get("/api/books/{isbn}", book.getIsbn()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(jsonPath("$.title").value("Spider Man"));
    }

    @Test
    @WithMockUser(roles = "READER")
    void findByIsbn_WhenBookNotExists_Returns404() throws Exception {
        String nonExistentIsbn = "9781438893051";

        mockMvc.perform(get("/api/books/{isbn}", nonExistentIsbn))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void deleteBookPhoto_WhenPhotoExists_DeletesSuccessfully() throws Exception {
        Book book = createTestBook("9781974738786", "Spider Man Fake Red");

        // Define apenas o URI do ficheiro (String)
        book.setPhoto("photo.png");
        bookRepository.save(book);

        mockMvc.perform(delete("/api/books/{isbn}/photo", book.getIsbn()))
                .andExpect(status().isOk());

        verify(fileStorageService).deleteFile("photo.png");
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void deleteBookPhoto_WhenNoPhoto_Returns404() throws Exception {
        String isbn = "9781974738786";

        mockMvc.perform(delete("/api/books/{isbn}/photo", "9781974738786"))
                .andExpect(status().isNotFound());

        verify(fileStorageService, never()).deleteFile(any());
    }


    /*
    @Test
    @Order(5)
    @WithMockUser(roles = "LIBRARIAN")
    void updateBook_WhenExists_UpdatesSuccessfully() throws Exception {
        String uniqueIsbn = "9780316551366";
        Book book = createTestBook(uniqueIsbn, "The 100");

        String updateJson = String.format("""
                {
                  "isbn": "%s",
                  "title": "The 101",
                  "genre": "%s",
                  "authors": [%d]
                  "description": "Updated description"
                }
                """,book.getIsbn(), book.getGenre(), book.getAuthors().get(0).getId());


        mockMvc.perform(patch("/api/books/{isbn}", book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("If-Match", book.getVersion())
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The 101"));

    }*/

    @Test
    @WithMockUser(roles = "READER")
    void findBooks_ByTitle_ReturnsMatchingBooks() throws Exception {
        // Arrange
        Book book1 = createTestBook("9781572229167", "Sql Guide");

        // Act & Assert
        mockMvc.perform(get("/api/books")
                        .param("title", "Sql Guide"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(1)));
    }

    @Test
    @WithMockUser(roles = "READER")
    void findBooks_ByTitle_NoMatch_Returns404() throws Exception {

        mockMvc.perform(get("/api/books")
                        .param("title", "Nonexistent Title"))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(roles = "READER")
    void findBooks_ByGenre_ReturnsMatchingBooks() throws Exception {
        // Arrange
        String genre = "TECHNOLOGY";


        // Act & Assert
        mockMvc.perform(get("/api/books")
                        .param("genre", genre))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", not(empty())))
                .andExpect(jsonPath("$.items[0].genre").value(genre));
    }

    @Test
    @WithMockUser(roles = "READER")
    void findBooks_ByGenre_NoMatch_Returns404() throws Exception {


        mockMvc.perform(get("/api/books")
                        .param("genre", "fweiufjew Genre"))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(roles = "READER")
    void findBooks_ByAuthorName_ReturnsMatchingBooks() throws Exception {
        // Arrange
        Book book1 = createTestBook("9781945051753", "Sql QuickStart");

        // Act & Assert
        mockMvc.perform(get("/api/books")
                        .param("authorName", book1.getAuthors().get(0).getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", not(empty())))
                .andExpect(jsonPath("$.items[0].authors[0]").value(book1.getAuthors().get(0).getName()));
    }

    @Test
    @WithMockUser(roles = "READER")
    void findBooks_ByAuthorName_NoMatch_ReturnsNotFound() throws Exception {


        mockMvc.perform(get("/api/books")
                        .param("authorName", "Nonexistent Author"))
                .andExpect(status().isNotFound());
    }






    // ===========================================
    // AUTORIZAÇÃO - TESTES
    // ===========================================

    @Test
    void searchIsbn_WithoutAuthentication_Returns401() throws Exception {
        mockMvc.perform(get("/api/books/isbn")
                        .param("title", "Any Book"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "READER")
    void createBook_WithReaderRole_Returns403() throws Exception {
        String uniqueIsbn = generateUniqueIsbn();

        String bookJson = String.format("""
                {
                    "isbn": "%s",
                    "title": "Test",
                    "genreId": %d,
                    "authorIds": [%d]
                }
                """, uniqueIsbn, testGenre.getPk(), testAuthor.getId());

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isForbidden());
    }


    // ===========================================
    // WORKFLOW COMPLETO
    // ===========================================
    /*
    @Test
    @Order(16)
    @WithMockUser(roles = "LIBRARIAN")
    void fullWorkflow_CreateUpdateDelete_WorksCorrectly() throws Exception {
        String uniqueIsbn = generateUniqueIsbn();

        // 1. Create
        String bookJson = String.format("""
                {
                    "isbn": "%s",
                    "title": "Workflow Test",
                    "description": "Original",
                    "genreId": %d,
                    "authorIds": [%d]
                }
                """, uniqueIsbn, testGenre.getPk(), testAuthor.getId());

        String createResponse = mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long bookPk = objectMapper.readTree(createResponse).get("pk").asLong();
        Long version = objectMapper.readTree(createResponse).get("version").asLong();

        // 2. Update
        String updateJson = String.format("""
                {
                    "title": "Workflow Test - Updated",
                    "version": %d
                }
                """, version);

        mockMvc.perform(put("/api/books/" + bookPk)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk());

        // 3. Delete
        mockMvc.perform(delete("/api/books/" + bookPk))
                .andExpect(status().isNoContent());

        // 4. Verify deletion
        mockMvc.perform(get("/api/books/" + bookPk))
                .andExpect(status().isNotFound());
    }*/

    // ===========================================
    // HELPER METHODS
    // ===========================================

    private Genre createTestGenre(String genreName) {
        Genre genre = genreFactory.create(genreName);
        return genreRepository.save(genre);
    }

    private Author createTestAuthor(String name) {
        Author author = new Author(name, "Biography", null);
        return authorRepository.save(author);
    }

    private Book createTestBook(String isbn, String title) {
        Book book = bookFactory.create(
                isbn,
                title,
                "Test description",
                testGenre,
                List.of(testAuthor),
                null
        );
        return bookRepository.save(book);
    }

    /**
     * Gera ISBN único para cada teste
     */
    private String generateUniqueIsbn() {
        int counter = isbnCounter.getAndIncrement();
        return String.format("978013235%04d", counter);
    }
}