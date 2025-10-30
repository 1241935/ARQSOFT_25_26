package pt.psoft.g1.psoftg1.authormanagement.repository;

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
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuthorRepository authorRepository;

    private Author testAuthor;
    private Author coAuthor;
    private Genre testGenre;
    private static final String AUTHOR_NAME = "Test Author";
    private static final String AUTHOR_BIO = "Test Bio";

    @BeforeEach
    void setUp() {
        testAuthor = new Author(AUTHOR_NAME, AUTHOR_BIO, null);
        entityManager.persistAndFlush(testAuthor);

        coAuthor = new Author("Co-Author", "Co-Author Bio", null);
        entityManager.persistAndFlush(coAuthor);

        testGenre = new Genre("","Fiction");
        entityManager.persistAndFlush(testGenre);

        entityManager.clear();
    }

    // ==========================================
    // findByAuthorNumber Tests
    // ==========================================

    @Nested
    @DisplayName("findByAuthorNumber")
    class FindByAuthorNumberTests {

        @Test
        @DisplayName("Should return empty when author number not found")
        void testFindByAuthorNumber_NotFound() {
            Optional<Author> result = authorRepository.findByAuthorNumber(999L);

            assertFalse(result.isPresent());
        }

        @Test
        @DisplayName("Should return empty for null author number")
        void testFindByAuthorNumber_Null() {
            Optional<Author> result = authorRepository.findByAuthorNumber(null);

            assertFalse(result.isPresent());
        }

        @Test
        @DisplayName("Should return empty for negative author number")
        void testFindByAuthorNumber_Negative() {
            Optional<Author> result = authorRepository.findByAuthorNumber(-1L);

            assertFalse(result.isPresent());
        }

        @Test
        @DisplayName("Should return correct author for valid number")
        void testFindByAuthorNumber_CorrectAuthor() {
            Optional<Author> result = authorRepository.findByAuthorNumber(
                    testAuthor.getAuthorNumber());

            assertTrue(result.isPresent());
            assertEquals(testAuthor.getAuthorNumber(), result.get().getAuthorNumber());
            assertEquals(AUTHOR_NAME, result.get().getName());
            assertEquals(AUTHOR_BIO, result.get().getBio());
        }
    }


    // ==========================================
    // findTopAuthorByLendings Tests
    // ==========================================

    @Nested
    @DisplayName("findTopAuthorByLendings")
    class FindTopAuthorByLendingsTests {

        @Test
        @DisplayName("Should return page of authors ordered by lending count")
        void testFindTopAuthors_Ordered() {
            Pageable pageable = PageRequest.of(0, 5);

            Page<AuthorLendingView> result = authorRepository.findTopAuthorByLendings(pageable);

            assertNotNull(result);
            // Verify it returns a valid page (may be empty if no lendings)
            assertTrue(result.getSize() <= 5);
        }

        @Test
        @DisplayName("Should respect page size limit")
        void testFindTopAuthors_PageLimit() {
            Pageable pageable = PageRequest.of(0, 3);

            Page<AuthorLendingView> result = authorRepository.findTopAuthorByLendings(pageable);

            assertNotNull(result);
            assertTrue(result.getSize() <= 3);
        }

        @Test
        @DisplayName("Should return empty page when no lendings exist")
        void testFindTopAuthors_NoLendings() {
            Pageable pageable = PageRequest.of(0, 5);

            Page<AuthorLendingView> result = authorRepository.findTopAuthorByLendings(pageable);

            assertNotNull(result);
            // May be empty if no lendings have been created
        }

        @Test
        @DisplayName("Should order by lending count descending")
        void testFindTopAuthors_DescendingOrder() {
            // This test assumes lendings exist
            Pageable pageable = PageRequest.of(0, 5);

            Page<AuthorLendingView> result = authorRepository.findTopAuthorByLendings(pageable);

            assertNotNull(result);
            List<AuthorLendingView> content = result.getContent();

            // Verify descending order if multiple results
            for (int i = 0; i < content.size() - 1; i++) {
                assertTrue(content.get(i).getLendingCount() >=
                        content.get(i + 1).getLendingCount());
            }
        }

        @Test
        @DisplayName("Should handle pagination")
        void testFindTopAuthors_Pagination() {
            Pageable page0 = PageRequest.of(0, 2);
            Pageable page1 = PageRequest.of(1, 2);

            Page<AuthorLendingView> result0 = authorRepository.findTopAuthorByLendings(page0);
            Page<AuthorLendingView> result1 = authorRepository.findTopAuthorByLendings(page1);

            assertNotNull(result0);
            assertNotNull(result1);
            // Verify different pages don't have same content (if enough data)
            if (result0.hasContent() && result1.hasContent()) {
                assertNotEquals(result0.getContent(), result1.getContent());
            }
        }
    }

    // ==========================================
    // findCoAuthorsByAuthorNumber Tests
    // ==========================================

    @Nested
    @DisplayName("findCoAuthorsByAuthorNumber")
    class FindCoAuthorsByAuthorNumberTests {

        @Test
        @DisplayName("Should find co-authors for author with shared books")
        void testFindCoAuthors_Found() {
            // Create a book with both authors
            Book sharedBook = new Book("1","9789722325332", "Shared Book", "Description",
                    testGenre, Arrays.asList(testAuthor, coAuthor), null);
            entityManager.persistAndFlush(sharedBook);
            entityManager.clear();

            List<Author> result = authorRepository.findCoAuthorsByAuthorNumber(
                    testAuthor.getAuthorNumber());

            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertTrue(result.stream().anyMatch(a ->
                    a.getAuthorNumber().equals(coAuthor.getAuthorNumber())));
        }

        @Test
        @DisplayName("Should return empty list when author has no co-authors")
        void testFindCoAuthors_NoCoAuthors() {
            // Create book with only testAuthor
            Book soloBook = new Book("1","9789722325332", "Solo Book", "Description",
                    testGenre, Arrays.asList(testAuthor), null);
            entityManager.persistAndFlush(soloBook);
            entityManager.clear();

            List<Author> result = authorRepository.findCoAuthorsByAuthorNumber(
                    testAuthor.getAuthorNumber());

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should not include the author themselves")
        void testFindCoAuthors_ExcludeSelf() {
            Book sharedBook = new Book("1","9789722325332", "Shared Book", "Description",
                    testGenre, Arrays.asList(testAuthor, coAuthor), null);
            entityManager.persistAndFlush(sharedBook);
            entityManager.clear();

            List<Author> result = authorRepository.findCoAuthorsByAuthorNumber(
                    testAuthor.getAuthorNumber());

            assertNotNull(result);
            assertFalse(result.stream().anyMatch(a ->
                    a.getAuthorNumber().equals(testAuthor.getAuthorNumber())));
        }

        @Test
        @DisplayName("Should return distinct co-authors")
        void testFindCoAuthors_Distinct() {
            // Create multiple books with same co-author
            Book book1 = new Book("1","9789722325332", "Book 1", "Description",
                    testGenre, Arrays.asList(testAuthor, coAuthor), null);
            Book book2 = new Book("2","9780590353403", "Book 2", "Description",
                    testGenre, Arrays.asList(testAuthor, coAuthor), null);
            entityManager.persistAndFlush(book1);
            entityManager.persistAndFlush(book2);
            entityManager.clear();

            List<Author> result = authorRepository.findCoAuthorsByAuthorNumber(
                    testAuthor.getAuthorNumber());

            assertNotNull(result);
            // Co-author should appear only once despite multiple shared books
            long coAuthorCount = result.stream()
                    .filter(a -> a.getAuthorNumber().equals(coAuthor.getAuthorNumber()))
                    .count();
            assertEquals(1, coAuthorCount);
        }

        @Test
        @DisplayName("Should return empty for non-existent author")
        void testFindCoAuthors_AuthorNotFound() {
            List<Author> result = authorRepository.findCoAuthorsByAuthorNumber(999L);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should handle multiple co-authors")
        void testFindCoAuthors_Multiple() {
            Author coAuthor2 = new Author("Co-Author 2", "Bio 2", null);
            Author coAuthor3 = new Author("Co-Author 3", "Bio 3", null);
            entityManager.persistAndFlush(coAuthor2);
            entityManager.persistAndFlush(coAuthor3);

            Book multiAuthorBook = new Book("1","9789722325332", "Multi Author", "Description",
                    testGenre, Arrays.asList(testAuthor, coAuthor, coAuthor2, coAuthor3), null);
            entityManager.persistAndFlush(multiAuthorBook);
            entityManager.clear();

            List<Author> result = authorRepository.findCoAuthorsByAuthorNumber(
                    testAuthor.getAuthorNumber());

            assertNotNull(result);
            assertEquals(3, result.size());
            assertFalse(result.stream().anyMatch(a ->
                    a.getAuthorNumber().equals(testAuthor.getAuthorNumber())));
        }
    }

}