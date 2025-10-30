package pt.psoft.g1.psoftg1.authormanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorMapper mapper;

    @Mock
    private PhotoRepository photoRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author testAuthor;
    private CreateAuthorRequest createRequest;
    private UpdateAuthorRequest updateRequest;
    private static final Long AUTHOR_NUMBER = 1L;
    private static final String AUTHOR_NAME = "Test Author";
    private static final String AUTHOR_BIO = "Test Bio";

    @BeforeEach
    void setUp() {
        testAuthor = new Author(AUTHOR_NAME, AUTHOR_BIO, null);
    }

    // ==========================================
    // findAll Tests
    // ==========================================

    @Nested
    @DisplayName("findAll")
    class FindAllTests {

        @Test
        @DisplayName("Should return all authors when authors exist")
        void testFindAll_AuthorsExist() {
            List<Author> authors = Arrays.asList(
                    new Author("Author 1", "Bio 1", null),
                    new Author("Author 2", "Bio 2", null)
            );
            when(authorRepository.findAll()).thenReturn(authors);

            Iterable<Author> result = authorService.findAll();

            assertNotNull(result);
            assertEquals(2, ((List<Author>) result).size());
            verify(authorRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no authors exist")
        void testFindAll_NoAuthors() {
            when(authorRepository.findAll()).thenReturn(Collections.emptyList());

            Iterable<Author> result = authorService.findAll();

            assertNotNull(result);
            assertEquals(0, ((List<Author>) result).size());
            verify(authorRepository).findAll();
        }
    }

    // ==========================================
    // findByAuthorNumber Tests
    // ==========================================

    @Nested
    @DisplayName("findByAuthorNumber")
    class FindByAuthorNumberTests {

        @Test
        @DisplayName("Should return author when found")
        void testFindByAuthorNumber_Found() {
            when(authorRepository.findByAuthorNumber(AUTHOR_NUMBER))
                    .thenReturn(Optional.of(testAuthor));

            Optional<Author> result = authorService.findByAuthorNumber(AUTHOR_NUMBER);

            assertTrue(result.isPresent());
            assertEquals(testAuthor, result.get());
            verify(authorRepository).findByAuthorNumber(AUTHOR_NUMBER);
        }

        @Test
        @DisplayName("Should return empty when author not found")
        void testFindByAuthorNumber_NotFound() {
            when(authorRepository.findByAuthorNumber(AUTHOR_NUMBER))
                    .thenReturn(Optional.empty());

            Optional<Author> result = authorService.findByAuthorNumber(AUTHOR_NUMBER);

            assertFalse(result.isPresent());
            verify(authorRepository).findByAuthorNumber(AUTHOR_NUMBER);
        }

        @Test
        @DisplayName("Should handle null author number")
        void testFindByAuthorNumber_Null() {
            when(authorRepository.findByAuthorNumber(null))
                    .thenReturn(Optional.empty());

            Optional<Author> result = authorService.findByAuthorNumber(null);

            assertFalse(result.isPresent());
            verify(authorRepository).findByAuthorNumber(null);
        }
    }

    // ==========================================
    // findByName Tests
    // ==========================================

    @Nested
    @DisplayName("findByName")
    class FindByNameTests {

        @Test
        @DisplayName("Should return authors matching name")
        void testFindByName_Found() {
            List<Author> authors = Arrays.asList(testAuthor);
            when(authorRepository.searchByNameNameStartsWith(AUTHOR_NAME))
                    .thenReturn(authors);

            List<Author> result = authorService.findByName(AUTHOR_NAME);

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(authorRepository).searchByNameNameStartsWith(AUTHOR_NAME);
        }

        @Test
        @DisplayName("Should return empty list when no matches")
        void testFindByName_NoMatches() {
            when(authorRepository.searchByNameNameStartsWith("NonExistent"))
                    .thenReturn(Collections.emptyList());

            List<Author> result = authorService.findByName("NonExistent");

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(authorRepository).searchByNameNameStartsWith("NonExistent");
        }

        @Test
        @DisplayName("Should handle partial name search")
        void testFindByName_PartialMatch() {
            List<Author> authors = Arrays.asList(
                    new Author("John Smith", "Bio", null),
                    new Author("John Doe", "Bio", null)
            );
            when(authorRepository.searchByNameNameStartsWith("John"))
                    .thenReturn(authors);

            List<Author> result = authorService.findByName("John");

            assertNotNull(result);
            assertEquals(2, result.size());
            verify(authorRepository).searchByNameNameStartsWith("John");
        }
    }

    // ==========================================
    // create Author Tests
    // ==========================================

    @Nested
    @DisplayName("create")
    class CreateTests {

        @BeforeEach
        void setUp() {
            createRequest = new CreateAuthorRequest(AUTHOR_NAME, AUTHOR_BIO, null, null);
        }

        @Test
        @DisplayName("Should create author without photo")
        void testCreate_NoPhoto() {
            when(mapper.create(createRequest)).thenReturn(testAuthor);
            when(authorRepository.save(testAuthor)).thenReturn(testAuthor);

            Author result = authorService.create(createRequest);

            assertNotNull(result);
            assertEquals(AUTHOR_NAME, result.getName());
            verify(mapper).create(createRequest);
            verify(authorRepository).save(testAuthor);
        }

        @Test
        @DisplayName("Should create author with photo when both photo and URI provided")
        void testCreate_WithPhotoAndURI() {
            MultipartFile mockFile = mock(MultipartFile.class);
            createRequest.setPhoto(mockFile);
            createRequest.setPhotoURI("photo.jpg");

            Author authorWithPhoto = new Author(AUTHOR_NAME, AUTHOR_BIO, "photo.jpg");
            when(mapper.create(createRequest)).thenReturn(authorWithPhoto);
            when(authorRepository.save(any(Author.class))).thenReturn(authorWithPhoto);

            Author result = authorService.create(createRequest);

            assertNotNull(result);
            verify(mapper).create(createRequest);
            verify(authorRepository).save(any(Author.class));
        }

        @Test
        @DisplayName("Should ignore photo when only photo provided (no URI)")
        void testCreate_OnlyPhoto() {
            MultipartFile mockFile = mock(MultipartFile.class);
            createRequest.setPhoto(mockFile);
            createRequest.setPhotoURI(null);

            when(mapper.create(createRequest)).thenReturn(testAuthor);
            when(authorRepository.save(testAuthor)).thenReturn(testAuthor);

            Author result = authorService.create(createRequest);

            assertNotNull(result);
            assertNull(createRequest.getPhoto());
            assertNull(createRequest.getPhotoURI());
            verify(authorRepository).save(testAuthor);
        }

        @Test
        @DisplayName("Should ignore URI when only URI provided (no photo)")
        void testCreate_OnlyURI() {
            createRequest.setPhoto(null);
            createRequest.setPhotoURI("photo.jpg");

            when(mapper.create(createRequest)).thenReturn(testAuthor);
            when(authorRepository.save(testAuthor)).thenReturn(testAuthor);

            Author result = authorService.create(createRequest);

            assertNotNull(result);
            assertNull(createRequest.getPhoto());
            assertNull(createRequest.getPhotoURI());
            verify(authorRepository).save(testAuthor);
        }
    }

    // ==========================================
    // partialUpdate Tests
    // ==========================================

    @Nested
    @DisplayName("partialUpdate")
    class PartialUpdateTests {

        @BeforeEach
        void setUp() {
            updateRequest = new UpdateAuthorRequest("Updated Name", "Updated Bio", null, null);
        }

        @Test
        @DisplayName("Should update author when found")
        void testPartialUpdate_Success() {
            when(authorRepository.findByAuthorNumber(AUTHOR_NUMBER))
                    .thenReturn(Optional.of(testAuthor));
            when(authorRepository.save(testAuthor)).thenReturn(testAuthor);

            Author result = authorService.partialUpdate(AUTHOR_NUMBER, updateRequest, 0L);

            assertNotNull(result);
            verify(authorRepository).findByAuthorNumber(AUTHOR_NUMBER);
            verify(authorRepository).save(testAuthor);
        }

        @Test
        @DisplayName("Should throw NotFoundException when author not found")
        void testPartialUpdate_NotFound() {
            when(authorRepository.findByAuthorNumber(AUTHOR_NUMBER))
                    .thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () ->
                    authorService.partialUpdate(AUTHOR_NUMBER, updateRequest, 0L)
            );

            verify(authorRepository).findByAuthorNumber(AUTHOR_NUMBER);
            verify(authorRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should update with photo when both photo and URI provided")
        void testPartialUpdate_WithPhotoAndURI() {
            MultipartFile mockFile = mock(MultipartFile.class);
            updateRequest.setPhoto(mockFile);
            updateRequest.setPhotoURI("new-photo.jpg");

            when(authorRepository.findByAuthorNumber(AUTHOR_NUMBER))
                    .thenReturn(Optional.of(testAuthor));
            when(authorRepository.save(testAuthor)).thenReturn(testAuthor);

            Author result = authorService.partialUpdate(AUTHOR_NUMBER, updateRequest, 0L);

            assertNotNull(result);
            verify(authorRepository).save(testAuthor);
        }

        @Test
        @DisplayName("Should ignore photo when only photo provided")
        void testPartialUpdate_OnlyPhoto() {
            MultipartFile mockFile = mock(MultipartFile.class);
            updateRequest.setPhoto(mockFile);
            updateRequest.setPhotoURI(null);

            when(authorRepository.findByAuthorNumber(AUTHOR_NUMBER))
                    .thenReturn(Optional.of(testAuthor));
            when(authorRepository.save(testAuthor)).thenReturn(testAuthor);

            Author result = authorService.partialUpdate(AUTHOR_NUMBER, updateRequest, 0L);

            assertNotNull(result);
            assertNull(updateRequest.getPhoto());
            assertNull(updateRequest.getPhotoURI());
            verify(authorRepository).save(testAuthor);
        }

        @Test
        @DisplayName("Should ignore URI when only URI provided")
        void testPartialUpdate_OnlyURI() {
            updateRequest.setPhoto(null);
            updateRequest.setPhotoURI("photo.jpg");

            when(authorRepository.findByAuthorNumber(AUTHOR_NUMBER))
                    .thenReturn(Optional.of(testAuthor));
            when(authorRepository.save(testAuthor)).thenReturn(testAuthor);

            Author result = authorService.partialUpdate(AUTHOR_NUMBER, updateRequest, 0L);

            assertNotNull(result);
            assertNull(updateRequest.getPhoto());
            assertNull(updateRequest.getPhotoURI());
            verify(authorRepository).save(testAuthor);
        }
    }

    // ==========================================
    // findTopAuthorByLendings Tests
    // ==========================================

    @Nested
    @DisplayName("findTopAuthorByLendings")
    class FindTopAuthorByLendingsTests {

        @Test
        @DisplayName("Should return top 5 authors")
        void testFindTopAuthorByLendings_Success() {
            List<AuthorLendingView> mockViews = Arrays.asList(
                    mock(AuthorLendingView.class),
                    mock(AuthorLendingView.class),
                    mock(AuthorLendingView.class)
            );
            Page<AuthorLendingView> page = new PageImpl<>(mockViews);

            when(authorRepository.findTopAuthorByLendings(any(PageRequest.class)))
                    .thenReturn(page);

            List<AuthorLendingView> result = authorService.findTopAuthorByLendings();

            assertNotNull(result);
            assertEquals(3, result.size());
            verify(authorRepository).findTopAuthorByLendings(any(PageRequest.class));
        }

        @Test
        @DisplayName("Should return empty list when no authors")
        void testFindTopAuthorByLendings_Empty() {
            Page<AuthorLendingView> emptyPage = new PageImpl<>(Collections.emptyList());

            when(authorRepository.findTopAuthorByLendings(any(PageRequest.class)))
                    .thenReturn(emptyPage);

            List<AuthorLendingView> result = authorService.findTopAuthorByLendings();

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(authorRepository).findTopAuthorByLendings(any(PageRequest.class));
        }
    }

    // ==========================================
    // findBooksByAuthorNumber Tests
    // ==========================================

    @Nested
    @DisplayName("findBooksByAuthorNumber")
    class FindBooksByAuthorNumberTests {

        @Test
        @DisplayName("Should return books for author")
        void testFindBooksByAuthorNumber_Found() {
            List<Book> books = Arrays.asList(
                    mock(Book.class),
                    mock(Book.class)
            );
            when(bookRepository.findBooksByAuthorNumber(AUTHOR_NUMBER))
                    .thenReturn(books);

            List<Book> result = authorService.findBooksByAuthorNumber(AUTHOR_NUMBER);

            assertNotNull(result);
            assertEquals(2, result.size());
            verify(bookRepository).findBooksByAuthorNumber(AUTHOR_NUMBER);
        }

        @Test
        @DisplayName("Should return empty list when author has no books")
        void testFindBooksByAuthorNumber_NoBooks() {
            when(bookRepository.findBooksByAuthorNumber(AUTHOR_NUMBER))
                    .thenReturn(Collections.emptyList());

            List<Book> result = authorService.findBooksByAuthorNumber(AUTHOR_NUMBER);

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(bookRepository).findBooksByAuthorNumber(AUTHOR_NUMBER);
        }
    }

    // ==========================================
    // findCoAuthorsByAuthorNumber Tests
    // ==========================================

    @Nested
    @DisplayName("findCoAuthorsByAuthorNumber")
    class FindCoAuthorsByAuthorNumberTests {

        @Test
        @DisplayName("Should return co-authors")
        void testFindCoAuthorsByAuthorNumber_Found() {
            List<Author> coAuthors = Arrays.asList(
                    new Author("Co-Author 1", "Bio 1", null),
                    new Author("Co-Author 2", "Bio 2", null)
            );
            when(authorRepository.findCoAuthorsByAuthorNumber(AUTHOR_NUMBER))
                    .thenReturn(coAuthors);

            List<Author> result = authorService.findCoAuthorsByAuthorNumber(AUTHOR_NUMBER);

            assertNotNull(result);
            assertEquals(2, result.size());
            verify(authorRepository).findCoAuthorsByAuthorNumber(AUTHOR_NUMBER);
        }

        @Test
        @DisplayName("Should return empty list when no co-authors")
        void testFindCoAuthorsByAuthorNumber_NoCoAuthors() {
            when(authorRepository.findCoAuthorsByAuthorNumber(AUTHOR_NUMBER))
                    .thenReturn(Collections.emptyList());

            List<Author> result = authorService.findCoAuthorsByAuthorNumber(AUTHOR_NUMBER);

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(authorRepository).findCoAuthorsByAuthorNumber(AUTHOR_NUMBER);
        }
    }

    // ==========================================
    // removeAuthorPhoto Tests
    // ==========================================

    @Nested
    @DisplayName("removeAuthorPhoto")
    class RemoveAuthorPhotoTests {

        @Test
        @DisplayName("Should remove photo successfully")
        void testRemoveAuthorPhoto_Success() {
            Author authorWithPhoto = new Author(AUTHOR_NAME, AUTHOR_BIO, "photo.jpg");

            when(authorRepository.findByAuthorNumber(AUTHOR_NUMBER))
                    .thenReturn(Optional.of(authorWithPhoto));
            when(authorRepository.save(authorWithPhoto))
                    .thenReturn(authorWithPhoto);

            Optional<Author> result = authorService.removeAuthorPhoto(AUTHOR_NUMBER, 0L);

            assertTrue(result.isPresent());
            verify(authorRepository).findByAuthorNumber(AUTHOR_NUMBER);
            verify(authorRepository).save(authorWithPhoto);
            verify(photoRepository).deleteByPhotoFile("photo.jpg");
        }

        @Test
        @DisplayName("Should throw NotFoundException when author not found")
        void testRemoveAuthorPhoto_AuthorNotFound() {
            when(authorRepository.findByAuthorNumber(AUTHOR_NUMBER))
                    .thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () ->
                    authorService.removeAuthorPhoto(AUTHOR_NUMBER, 0L)
            );

            verify(authorRepository).findByAuthorNumber(AUTHOR_NUMBER);
            verify(authorRepository, never()).save(any());
            verify(photoRepository, never()).deleteByPhotoFile(anyString());
        }

        @Test
        @DisplayName("Should handle removing photo from author without photo")
        void testRemoveAuthorPhoto_NoPhoto() {
            Author authorNoPhoto = new Author(AUTHOR_NAME, AUTHOR_BIO, null);

            when(authorRepository.findByAuthorNumber(AUTHOR_NUMBER))
                    .thenReturn(Optional.of(authorNoPhoto));

            assertThrows(NullPointerException.class, () ->
                    authorService.removeAuthorPhoto(AUTHOR_NUMBER, 0L)
            );

            verify(authorRepository).findByAuthorNumber(AUTHOR_NUMBER);
        }
    }
}