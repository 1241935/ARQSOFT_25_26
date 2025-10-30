package pt.psoft.g1.psoftg1.bookmanagement.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    private Book testBook;
    private Author testAuthor;
    private Genre testGenre;
    private static final String ISBN = "9780590353403";
    private static final String TITLE = "Test Book";

    @BeforeEach
    void setUp() {
        // Setup test data
        testAuthor = new Author("Test Author", "Bio", null);
        entityManager.persistAndFlush(testAuthor);

        testGenre = new Genre("1","Fiction");
        entityManager.persistAndFlush(testGenre);

        testBook = new Book("1",ISBN, TITLE, "Description", testGenre,
                Arrays.asList(testAuthor), null);
        entityManager.persistAndFlush(testBook);
        entityManager.clear();
    }

    // ==========================================
    // findByIsbn Tests
    // ==========================================

    @Nested
    @DisplayName("findByIsbn - Black Box Tests")
    class FindByIsbnTests {

        @Test
        @DisplayName("Should find book by ISBN when exists")
        void testFindByIsbn_Found() {
            Optional<Book> result = bookRepository.findByIsbn(ISBN);

            assertTrue(result.isPresent());
            assertEquals(ISBN, result.get().getIsbn());
        }

        @Test
        @DisplayName("Should return empty when ISBN not found")
        void testFindByIsbn_NotFound() {
            Optional<Book> result = bookRepository.findByIsbn("1234567890123");

            assertFalse(result.isPresent());
        }

        @Test
        @DisplayName("Should return empty for null ISBN")
        void testFindByIsbn_Null() {
            Optional<Book> result = bookRepository.findByIsbn(null);

            assertFalse(result.isPresent());
        }

        @Test
        @DisplayName("Should return empty for empty ISBN")
        void testFindByIsbn_Empty() {
            Optional<Book> result = bookRepository.findByIsbn("");

            assertFalse(result.isPresent());
        }
    }

    // ==========================================
    // findByGenre Tests
    // ==========================================

    @Nested
    @DisplayName("findByGenre")
    class FindByGenreTests {

        @Test
        @DisplayName("Should find books by exact genre")
        void testFindByGenre_Exact() {
            List<Book> result = bookRepository.findByGenre("Fiction");

            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertTrue(result.stream().allMatch(b ->
                    b.getGenre().toString().contains("Fiction")));
        }

        @Test
        @DisplayName("Should find books by partial genre match")
        void testFindByGenre_Partial() {
            List<Book> result = bookRepository.findByGenre("Fic");

            assertNotNull(result);
            assertFalse(result.isEmpty());
        }

        @Test
        @DisplayName("Should return empty list for non-existent genre")
        void testFindByGenre_NotFound() {
            List<Book> result = bookRepository.findByGenre("NonExistent");

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should handle case sensitivity")
        void testFindByGenre_CaseSensitive() {
            List<Book> result = bookRepository.findByGenre("fiction");

            assertNotNull(result);
        }
    }

    // ==========================================
    // findByTitle Tests
    // ==========================================

    @Nested
    @DisplayName("findByTitle")
    class FindByTitleTests {

        @Test
        @DisplayName("Should find books by exact title")
        void testFindByTitle_Exact() {
            List<Book> result = bookRepository.findByTitle(TITLE);

            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertTrue(result.stream().anyMatch(b ->
                    b.getTitle().getTitle().equals(TITLE)));
        }

        @Test
        @DisplayName("Should find books by partial title")
        void testFindByTitle_Partial() {
            List<Book> result = bookRepository.findByTitle("Test");

            assertNotNull(result);
            assertFalse(result.isEmpty());
        }

        @Test
        @DisplayName("Should return empty list when title not found")
        void testFindByTitle_NotFound() {
            List<Book> result = bookRepository.findByTitle("NonExistent");

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    // ==========================================
    // findByAuthorName Tests
    // ==========================================

    @Nested
    @DisplayName("findByAuthorName")
    class FindByAuthorNameTests {

        @Test
        @DisplayName("Should find books by author name")
        void testFindByAuthorName_Found() {
            List<Book> result = bookRepository.findByAuthorName("Test Author%");

            assertNotNull(result);
            assertFalse(result.isEmpty());
        }

        @Test
        @DisplayName("Should find books by partial author name")
        void testFindByAuthorName_Partial() {
            List<Book> result = bookRepository.findByAuthorName("Test%");

            assertNotNull(result);
            assertFalse(result.isEmpty());
        }

        @Test
        @DisplayName("Should return empty list when author not found")
        void testFindByAuthorName_NotFound() {
            List<Book> result = bookRepository.findByAuthorName("NonExistent%");

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should handle wildcard pattern")
        void testFindByAuthorName_Wildcard() {
            List<Book> result = bookRepository.findByAuthorName("%Author%");

            assertNotNull(result);
            assertFalse(result.isEmpty());
        }
    }

    // ==========================================
    // findBooksByAuthorNumber Tests
    // ==========================================

    @Nested
    @DisplayName("findBooksByAuthorNumber")
    class FindBooksByAuthorNumberTests {

        @Test
        @DisplayName("Should find books by author number")
        void testFindBooksByAuthorNumber_Found() {
            List<Book> result = bookRepository.findBooksByAuthorNumber(
                    testAuthor.getAuthorNumber());

            assertNotNull(result);
            assertFalse(result.isEmpty());
        }

        @Test
        @DisplayName("Should return empty list for non-existent author")
        void testFindBooksByAuthorNumber_NotFound() {
            List<Book> result = bookRepository.findBooksByAuthorNumber(999L);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should return multiple books for author with multiple books")
        void testFindBooksByAuthorNumber_Multiple() {
            Book book2 = new Book("2","9788498389395", "Book 2", "Desc",
                    testGenre, Arrays.asList(testAuthor), null);
            entityManager.persistAndFlush(book2);

            List<Book> result = bookRepository.findBooksByAuthorNumber(
                    testAuthor.getAuthorNumber());

            assertNotNull(result);
            assertTrue(result.size() >= 2);
        }
    }

    // ==========================================
    // searchBooks Tests
    // ==========================================

    @Nested
    @DisplayName("searchBooks")
    class SearchBooksTests {

        @Test
        @DisplayName("Should search books by title")
        void testSearchBooks_ByTitle() {
            pt.psoft.g1.psoftg1.shared.services.Page page =
                    new pt.psoft.g1.psoftg1.shared.services.Page(1, 10);
            SearchBooksQuery query = new SearchBooksQuery("Test", "", "");

            List<Book> result = bookRepository.searchBooks(page, query);

            assertNotNull(result);
            assertFalse(result.isEmpty());
        }

        @Test
        @DisplayName("Should search books by genre")
        void testSearchBooks_ByGenre() {
            pt.psoft.g1.psoftg1.shared.services.Page page =
                    new pt.psoft.g1.psoftg1.shared.services.Page(1, 10);
            SearchBooksQuery query = new SearchBooksQuery("", "Fiction", "");

            List<Book> result = bookRepository.searchBooks(page, query);

            assertNotNull(result);
            assertFalse(result.isEmpty());
        }

        @Test
        @DisplayName("Should search books by author name")
        void testSearchBooks_ByAuthor() {
            pt.psoft.g1.psoftg1.shared.services.Page page =
                    new pt.psoft.g1.psoftg1.shared.services.Page(1, 10);
            SearchBooksQuery query = new SearchBooksQuery("", "", "Test");

            List<Book> result = bookRepository.searchBooks(page, query);

            assertNotNull(result);
            assertFalse(result.isEmpty());
        }

        @Test
        @DisplayName("Should search books by multiple criteria")
        void testSearchBooks_MultipleCriteria() {
            pt.psoft.g1.psoftg1.shared.services.Page page =
                    new pt.psoft.g1.psoftg1.shared.services.Page(1, 10);
            SearchBooksQuery query = new SearchBooksQuery("Test", "Fiction", "Test");

            List<Book> result = bookRepository.searchBooks(page, query);

            assertNotNull(result);
            assertFalse(result.isEmpty());
        }

        @Test
        @DisplayName("Should respect page limit")
        void testSearchBooks_PageLimit() {
            pt.psoft.g1.psoftg1.shared.services.Page page =
                    new pt.psoft.g1.psoftg1.shared.services.Page(1, 1);
            SearchBooksQuery query = new SearchBooksQuery("", "", "");

            List<Book> result = bookRepository.searchBooks(page, query);

            assertNotNull(result);
            assertTrue(result.size() <= 1);
        }


        @Test
        @DisplayName("Should return empty list when no matches")
        void testSearchBooks_NoMatches() {
            pt.psoft.g1.psoftg1.shared.services.Page page =
                    new pt.psoft.g1.psoftg1.shared.services.Page(1, 10);
            SearchBooksQuery query = new SearchBooksQuery("NonExistent", "", "");

            List<Book> result = bookRepository.searchBooks(page, query);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
}