package pt.psoft.g1.psoftg1.genremanagement.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("GenreRepository")
class GenreRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GenreRepository genreRepository;

    private Genre testGenre;
    private Genre secondGenre;
    private static final String GENRE_NAME = "Fiction";

    @BeforeEach
    void setUp() {
        testGenre = new Genre("1", GENRE_NAME);
        entityManager.persistAndFlush(testGenre);

        secondGenre = new Genre("2", "Mystery");
        entityManager.persistAndFlush(secondGenre);

        entityManager.clear();
    }


    // ==========================================
    // findByString Tests
    // ==========================================

    @Nested
    @DisplayName("findByString")
    class FindByStringTests {

        @Test
        @DisplayName("Should find genre by exact name")
        void testFindByString_Exact() {
            Optional<Genre> result = genreRepository.findByString(GENRE_NAME);

            assertTrue(result.isPresent());
            assertEquals(GENRE_NAME, result.get().toString());
        }

        @Test
        @DisplayName("Should return empty for non-existent genre")
        void testFindByString_NotFound() {
            Optional<Genre> result = genreRepository.findByString("NonExistent");

            assertFalse(result.isPresent());
        }

        @Test
        @DisplayName("Should be case sensitive")
        void testFindByString_CaseSensitive() {
        }
    }

}