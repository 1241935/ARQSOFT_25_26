package pt.psoft.g1.psoftg1.genremanagement.services;

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
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreServiceImpl genreService;

    private Genre testGenre;
    private static final String GENRE_NAME = "Fiction";

    @BeforeEach
    void setUp() {
        testGenre = new Genre("1",GENRE_NAME);
    }

    // ==========================================
    // findByString Tests
    // ==========================================

    @Nested
    @DisplayName("findByString")
    class FindByStringTests {

        @Test
        @DisplayName("Should find genre by name when exists")
        void testFindByString_Found() {
            when(genreRepository.findByString(GENRE_NAME)).thenReturn(Optional.of(testGenre));

            Optional<Genre> result = genreService.findByString(GENRE_NAME);

            assertTrue(result.isPresent());
            assertEquals(GENRE_NAME, result.get().toString());
            verify(genreRepository).findByString(GENRE_NAME);
        }

        @Test
        @DisplayName("Should return empty when genre not found")
        void testFindByString_NotFound() {
            when(genreRepository.findByString("NonExistent")).thenReturn(Optional.empty());

            Optional<Genre> result = genreService.findByString("NonExistent");

            assertFalse(result.isPresent());
            verify(genreRepository).findByString("NonExistent");
        }

        @Test
        @DisplayName("Should handle null genre name")
        void testFindByString_Null() {
            when(genreRepository.findByString(null)).thenReturn(Optional.empty());

            Optional<Genre> result = genreService.findByString(null);

            assertFalse(result.isPresent());
            verify(genreRepository).findByString(null);
        }

        @Test
        @DisplayName("Should handle empty genre name")
        void testFindByString_Empty() {
            when(genreRepository.findByString("")).thenReturn(Optional.empty());

            Optional<Genre> result = genreService.findByString("");

            assertFalse(result.isPresent());
            verify(genreRepository).findByString("");
        }

        @Test
        @DisplayName("Should handle case sensitive search")
        void testFindByString_CaseSensitive() {
            when(genreRepository.findByString("fiction")).thenReturn(Optional.empty());

            Optional<Genre> result = genreService.findByString("fiction");

            assertFalse(result.isPresent());
            verify(genreRepository).findByString("fiction");
        }
    }

    // ==========================================
    // findAll Tests
    // ==========================================

    @Nested
    @DisplayName("findAll")
    class FindAllTests {

        @Test
        @DisplayName("Should return all genres when genres exist")
        void testFindAll_WithGenres() {
            List<Genre> genres = Arrays.asList(
                    new Genre("2","Fiction1"),
                    new Genre("3","Mystery"),
                    new Genre("4","Science")
            );
            when(genreRepository.findAll()).thenReturn(genres);

            Iterable<Genre> result = genreService.findAll();

            assertNotNull(result);
            assertEquals(3, ((List<Genre>) result).size());
            verify(genreRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no genres exist")
        void testFindAll_Empty() {
            when(genreRepository.findAll()).thenReturn(Collections.emptyList());

            Iterable<Genre> result = genreService.findAll();

            assertNotNull(result);
            assertEquals(0, ((List<Genre>) result).size());
            verify(genreRepository).findAll();
        }

        @Test
        @DisplayName("Should return single genre")
        void testFindAll_SingleGenre() {
            List<Genre> genres = Arrays.asList(testGenre);
            when(genreRepository.findAll()).thenReturn(genres);

            Iterable<Genre> result = genreService.findAll();

            assertNotNull(result);
            assertEquals(1, ((List<Genre>) result).size());
        }
    }

    // ==========================================
    // findTopGenreByBooks Tests
    // ==========================================

    @Nested
    @DisplayName("findTopGenreByBooks")
    class FindTopGenreByBooksTests {

        @Test
        @DisplayName("Should return top 5 genres by book count")
        void testFindTopGenres_Success() {
            List<GenreBookCountDTO> genreCounts = Arrays.asList(
                    new GenreBookCountDTO("Fiction", 100L),
                    new GenreBookCountDTO("Mystery", 75L),
                    new GenreBookCountDTO("Science", 50L)
            );
            Page<GenreBookCountDTO> page = new PageImpl<>(genreCounts);

            when(genreRepository.findTop5GenreByBookCount(any(PageRequest.class)))
                    .thenReturn(page);

            List<GenreBookCountDTO> result = genreService.findTopGenreByBooks();

            assertNotNull(result);
            assertEquals(3, result.size());
            verify(genreRepository).findTop5GenreByBookCount(any(PageRequest.class));
        }

        @Test
        @DisplayName("Should return empty list when no genres")
        void testFindTopGenres_Empty() {
            Page<GenreBookCountDTO> emptyPage = new PageImpl<>(Collections.emptyList());

            when(genreRepository.findTop5GenreByBookCount(any(PageRequest.class)))
                    .thenReturn(emptyPage);

            List<GenreBookCountDTO> result = genreService.findTopGenreByBooks();

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(genreRepository).findTop5GenreByBookCount(any(PageRequest.class));
        }

        @Test
        @DisplayName("Should request page of size 5")
        void testFindTopGenres_PageSize() {
            Page<GenreBookCountDTO> page = new PageImpl<>(Collections.emptyList());

            when(genreRepository.findTop5GenreByBookCount(any(PageRequest.class)))
                    .thenReturn(page);

            genreService.findTopGenreByBooks();

            verify(genreRepository).findTop5GenreByBookCount(argThat(pageable ->
                    pageable.getPageSize() == 5 && pageable.getPageNumber() == 0
            ));
        }

        @Test
        @DisplayName("Should handle exactly 5 genres")
        void testFindTopGenres_ExactlyFive() {
            List<GenreBookCountDTO> genreCounts = Arrays.asList(
                    new GenreBookCountDTO("Genre1", 100L),
                    new GenreBookCountDTO("Genre2", 90L),
                    new GenreBookCountDTO("Genre3", 80L),
                    new GenreBookCountDTO("Genre4", 70L),
                    new GenreBookCountDTO("Genre5", 60L)
            );
            Page<GenreBookCountDTO> page = new PageImpl<>(genreCounts);

            when(genreRepository.findTop5GenreByBookCount(any(PageRequest.class)))
                    .thenReturn(page);

            List<GenreBookCountDTO> result = genreService.findTopGenreByBooks();

            assertEquals(5, result.size());
        }
    }

    // ==========================================
    // save Tests
    // ==========================================

    @Nested
    @DisplayName("save")
    class SaveTests {

        @Test
        @DisplayName("Should save genre successfully")
        void testSave_Success() {
            when(genreRepository.save(testGenre)).thenReturn(testGenre);

            Genre result = genreService.save(testGenre);

            assertNotNull(result);
            assertEquals(testGenre, result);
            verify(genreRepository).save(testGenre);
        }

        @Test
        @DisplayName("Should save new genre")
        void testSave_NewGenre() {
            Genre newGenre = new Genre("2","Horror");
            when(genreRepository.save(newGenre)).thenReturn(newGenre);

            Genre result = genreService.save(newGenre);

            assertNotNull(result);
            assertEquals("Horror", result.toString());
            verify(genreRepository).save(newGenre);
        }

    }

    // ==========================================
    // getLendingsPerMonthLastYearByGenre Tests
    // ==========================================

    @Nested
    @DisplayName("getLendingsPerMonthLastYearByGenre")
    class GetLendingsPerMonthLastYearTests {

        @Test
        @DisplayName("Should return lendings per month for last year")
        void testGetLendingsPerMonth_Success() {
            List<GenreLendingsDTO> lendingsList = Arrays.asList(
                    new GenreLendingsDTO("Fiction", 10L)
            );
            List<GenreLendingsPerMonthDTO> monthlyLendings = Arrays.asList(
                    new GenreLendingsPerMonthDTO(2024, 1, lendingsList)
            );

            when(genreRepository.getLendingsPerMonthLastYearByGenre())
                    .thenReturn(monthlyLendings);

            List<GenreLendingsPerMonthDTO> result = genreService.getLendingsPerMonthLastYearByGenre();

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(genreRepository).getLendingsPerMonthLastYearByGenre();
        }

        @Test
        @DisplayName("Should return empty list when no lendings")
        void testGetLendingsPerMonth_Empty() {
            when(genreRepository.getLendingsPerMonthLastYearByGenre())
                    .thenReturn(Collections.emptyList());

            List<GenreLendingsPerMonthDTO> result = genreService.getLendingsPerMonthLastYearByGenre();

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(genreRepository).getLendingsPerMonthLastYearByGenre();
        }

        @Test
        @DisplayName("Should handle multiple months")
        void testGetLendingsPerMonth_MultipleMonths() {
            List<GenreLendingsDTO> jan = Arrays.asList(new GenreLendingsDTO("Fiction", 10L));
            List<GenreLendingsDTO> feb = Arrays.asList(new GenreLendingsDTO("Fiction", 15L));

            List<GenreLendingsPerMonthDTO> monthlyLendings = Arrays.asList(
                    new GenreLendingsPerMonthDTO(2024, 1, jan),
                    new GenreLendingsPerMonthDTO(2024, 2, feb)
            );

            when(genreRepository.getLendingsPerMonthLastYearByGenre())
                    .thenReturn(monthlyLendings);

            List<GenreLendingsPerMonthDTO> result = genreService.getLendingsPerMonthLastYearByGenre();

            assertEquals(2, result.size());
        }
    }

    // ==========================================
    // getAverageLendings Tests
    // ==========================================

    @Nested
    @DisplayName("getAverageLendings")
    class GetAverageLendingsTests {

        @Test
        @DisplayName("Should return average lendings for specified month")
        void testGetAverageLendings_Success() {
            GetAverageLendingsQuery query = new GetAverageLendingsQuery();
            query.setYear(2024);
            query.setMonth(1);

            pt.psoft.g1.psoftg1.shared.services.Page page =
                    new pt.psoft.g1.psoftg1.shared.services.Page(1, 10);

            List<GenreLendingsDTO> averages = Arrays.asList(
                    new GenreLendingsDTO("Fiction", 5.5)
            );

            when(genreRepository.getAverageLendingsInMonth(any(LocalDate.class), eq(page)))
                    .thenReturn(averages);

            List<GenreLendingsDTO> result = genreService.getAverageLendings(query, page);

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(genreRepository).getAverageLendingsInMonth(any(LocalDate.class), eq(page));
        }

        @Test
        @DisplayName("Should use default page when null")
        void testGetAverageLendings_NullPage() {
            GetAverageLendingsQuery query = new GetAverageLendingsQuery();
            query.setYear(2024);
            query.setMonth(1);

            when(genreRepository.getAverageLendingsInMonth(any(LocalDate.class), any()))
                    .thenReturn(Collections.emptyList());

            List<GenreLendingsDTO> result = genreService.getAverageLendings(query, null);

            assertNotNull(result);
            verify(genreRepository).getAverageLendingsInMonth(
                    any(LocalDate.class),
                    argThat(p -> p.getNumber() == 1 && p.getLimit() == 10)
            );
        }

        @Test
        @DisplayName("Should handle empty results")
        void testGetAverageLendings_Empty() {
            GetAverageLendingsQuery query = new GetAverageLendingsQuery();
            query.setYear(2024);
            query.setMonth(1);

            pt.psoft.g1.psoftg1.shared.services.Page page =
                    new pt.psoft.g1.psoftg1.shared.services.Page(1, 10);

            when(genreRepository.getAverageLendingsInMonth(any(LocalDate.class), eq(page)))
                    .thenReturn(Collections.emptyList());

            List<GenreLendingsDTO> result = genreService.getAverageLendings(query, page);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should handle different months")
        void testGetAverageLendings_DifferentMonths() {
            GetAverageLendingsQuery query = new GetAverageLendingsQuery();
            query.setYear(2024);
            query.setMonth(12);

            pt.psoft.g1.psoftg1.shared.services.Page page =
                    new pt.psoft.g1.psoftg1.shared.services.Page(1, 10);

            when(genreRepository.getAverageLendingsInMonth(any(LocalDate.class), eq(page)))
                    .thenReturn(Collections.emptyList());

            genreService.getAverageLendings(query, page);

            verify(genreRepository).getAverageLendingsInMonth(
                    argThat(date -> date.getMonthValue() == 12),
                    eq(page)
            );
        }

        @Test
        @DisplayName("Should respect page limits")
        void testGetAverageLendings_PageLimits() {
            GetAverageLendingsQuery query = new GetAverageLendingsQuery();
            query.setYear(2024);
            query.setMonth(1);

            pt.psoft.g1.psoftg1.shared.services.Page page =
                    new pt.psoft.g1.psoftg1.shared.services.Page(2, 5);

            when(genreRepository.getAverageLendingsInMonth(any(LocalDate.class), eq(page)))
                    .thenReturn(Collections.emptyList());

            genreService.getAverageLendings(query, page);

            verify(genreRepository).getAverageLendingsInMonth(any(LocalDate.class),
                    argThat(p -> p.getNumber() == 2 && p.getLimit() == 5)
            );
        }
    }

    // ==========================================
    // getLendingsAverageDurationPerMonth Tests
    // ==========================================

    @Nested
    @DisplayName("getLendingsAverageDurationPerMonth")
    class GetLendingsAverageDurationTests {

        @Test
        @DisplayName("Should return average duration for valid date range")
        void testGetAverageDuration_Success() {
            String startDate = "2024-01-01";
            String endDate = "2024-12-31";

            List<GenreLendingsDTO> lendingsList = Arrays.asList(
                    new GenreLendingsDTO("Fiction", 14.5)
            );
            List<GenreLendingsPerMonthDTO> monthlyDurations = Arrays.asList(
                    new GenreLendingsPerMonthDTO(2024, 1, lendingsList)
            );

            when(genreRepository.getLendingsAverageDurationPerMonth(
                    any(LocalDate.class), any(LocalDate.class)))
                    .thenReturn(monthlyDurations);

            List<GenreLendingsPerMonthDTO> result = genreService
                    .getLendingsAverageDurationPerMonth(startDate, endDate);

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(genreRepository).getLendingsAverageDurationPerMonth(
                    any(LocalDate.class), any(LocalDate.class)
            );
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for invalid date format")
        void testGetAverageDuration_InvalidFormat() {
            String invalidDate = "01-01-2024";
            String endDate = "2024-12-31";

            assertThrows(IllegalArgumentException.class, () ->
                    genreService.getLendingsAverageDurationPerMonth(invalidDate, endDate)
            );

            verify(genreRepository, never()).getLendingsAverageDurationPerMonth(
                    any(LocalDate.class), any(LocalDate.class)
            );
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when start after end")
        void testGetAverageDuration_StartAfterEnd() {
            String startDate = "2024-12-31";
            String endDate = "2024-01-01";

            assertThrows(IllegalArgumentException.class, () ->
                    genreService.getLendingsAverageDurationPerMonth(startDate, endDate)
            );

            verify(genreRepository, never()).getLendingsAverageDurationPerMonth(
                    any(LocalDate.class), any(LocalDate.class)
            );
        }

        @Test
        @DisplayName("Should throw NotFoundException when no data found")
        void testGetAverageDuration_NoData() {
            String startDate = "2024-01-01";
            String endDate = "2024-12-31";

            when(genreRepository.getLendingsAverageDurationPerMonth(
                    any(LocalDate.class), any(LocalDate.class)))
                    .thenReturn(Collections.emptyList());

            assertThrows(NotFoundException.class, () ->
                    genreService.getLendingsAverageDurationPerMonth(startDate, endDate)
            );
        }

        @Test
        @DisplayName("Should handle same start and end date")
        void testGetAverageDuration_SameDate() {
            String date = "2024-06-15";

            List<GenreLendingsPerMonthDTO> monthlyDurations = Arrays.asList(
                    new GenreLendingsPerMonthDTO(2024, 6,
                            Arrays.asList(new GenreLendingsDTO("Fiction", 14.0)))
            );

            when(genreRepository.getLendingsAverageDurationPerMonth(
                    any(LocalDate.class), any(LocalDate.class)))
                    .thenReturn(monthlyDurations);

            List<GenreLendingsPerMonthDTO> result = genreService
                    .getLendingsAverageDurationPerMonth(date, date);

            assertNotNull(result);
            verify(genreRepository).getLendingsAverageDurationPerMonth(
                    LocalDate.parse(date), LocalDate.parse(date)
            );
        }

        @Test
        @DisplayName("Should handle multiple months in range")
        void testGetAverageDuration_MultipleMonths() {
            String startDate = "2024-01-01";
            String endDate = "2024-03-31";

            List<GenreLendingsPerMonthDTO> monthlyDurations = Arrays.asList(
                    new GenreLendingsPerMonthDTO(2024, 1, Arrays.asList(new GenreLendingsDTO("Fiction", 10.0))),
                    new GenreLendingsPerMonthDTO(2024, 2, Arrays.asList(new GenreLendingsDTO("Fiction", 12.0))),
                    new GenreLendingsPerMonthDTO(2024, 3, Arrays.asList(new GenreLendingsDTO("Fiction", 11.0)))
            );

            when(genreRepository.getLendingsAverageDurationPerMonth(
                    any(LocalDate.class), any(LocalDate.class)))
                    .thenReturn(monthlyDurations);

            List<GenreLendingsPerMonthDTO> result = genreService
                    .getLendingsAverageDurationPerMonth(startDate, endDate);

            assertEquals(3, result.size());
        }

        @Test
        @DisplayName("Should throw exception for malformed date")
        void testGetAverageDuration_MalformedDate() {
            assertThrows(IllegalArgumentException.class, () ->
                    genreService.getLendingsAverageDurationPerMonth("2024-13-01", "2024-12-31")
            );

            assertThrows(IllegalArgumentException.class, () ->
                    genreService.getLendingsAverageDurationPerMonth("2024-01-32", "2024-12-31")
            );

            assertThrows(IllegalArgumentException.class, () ->
                    genreService.getLendingsAverageDurationPerMonth("not-a-date", "2024-12-31")
            );
        }
    }
}