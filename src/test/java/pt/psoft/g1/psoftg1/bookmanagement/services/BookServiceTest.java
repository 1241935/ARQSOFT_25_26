package pt.psoft.g1.psoftg1.bookmanagement.services;

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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.factories.BookFactory;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private ReaderRepository readerRepository;

    @Mock
    private LibraryApi libraryApi;

    @Mock
    private BookFactory bookFactory;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book testBook;
    private Author testAuthor;
    private Genre testGenre;
    private CreateBookRequest createRequest;
    private static final String ISBN = "978-3-16-148410-0";
    private static final String TITLE = "Test Book";
    private static final String DESCRIPTION = "Test Description";
    private static final Long AUTHOR_NUMBER = 1L;

    @BeforeEach
    void setUp() {
        testAuthor = new Author("Test Author", "Bio", null);
        testGenre = new Genre("1","Fiction");
        testBook = mock(Book.class);

        ReflectionTestUtils.setField(bookService, "suggestionsLimitPerGenre", 5L);
    }

    // ==========================================
    // create Tests
    // ==========================================

    @Nested
    @DisplayName("create")
    class CreateTests {

        @BeforeEach
        void setUp() {
            createRequest = new CreateBookRequest();
            createRequest.setTitle(TITLE);
            createRequest.setDescription(DESCRIPTION);
            createRequest.setGenre("Fiction");
            createRequest.setAuthors(Arrays.asList(AUTHOR_NUMBER));
        }

        @Test
        @DisplayName("Should create book successfully")
        void testCreate_Success() {
            when(bookRepository.findByIsbn(ISBN)).thenReturn(Optional.empty());
            when(authorRepository.findByAuthorNumber(AUTHOR_NUMBER)).thenReturn(Optional.of(testAuthor));
            when(genreRepository.findByString("Fiction")).thenReturn(Optional.of(testGenre));
            when(bookFactory.create(eq(ISBN), eq(TITLE), eq(DESCRIPTION), eq(testGenre), anyList(), isNull()))
                    .thenReturn(testBook);
            when(bookRepository.save(testBook)).thenReturn(testBook);

            Book result = bookService.create(createRequest, ISBN);

            assertNotNull(result);
            verify(bookRepository).findByIsbn(ISBN);
            verify(bookRepository).save(testBook);
        }

        @Test
        @DisplayName("Should throw ConflictException when ISBN already exists")
        void testCreate_ISBNExists() {
            when(bookRepository.findByIsbn(ISBN)).thenReturn(Optional.of(testBook));

            assertThrows(ConflictException.class, () ->
                    bookService.create(createRequest, ISBN)
            );

            verify(bookRepository).findByIsbn(ISBN);
            verify(bookRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw NotFoundException when genre not found")
        void testCreate_GenreNotFound() {
            when(bookRepository.findByIsbn(ISBN)).thenReturn(Optional.empty());
            when(authorRepository.findByAuthorNumber(AUTHOR_NUMBER)).thenReturn(Optional.of(testAuthor));
            when(genreRepository.findByString("Fiction")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () ->
                    bookService.create(createRequest, ISBN)
            );

            verify(bookRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should skip non-existent authors")
        void testCreate_SkipNonExistentAuthors() {
            createRequest.setAuthors(Arrays.asList(AUTHOR_NUMBER, 999L));

            when(bookRepository.findByIsbn(ISBN)).thenReturn(Optional.empty());
            when(authorRepository.findByAuthorNumber(AUTHOR_NUMBER)).thenReturn(Optional.of(testAuthor));
            when(authorRepository.findByAuthorNumber(999L)).thenReturn(Optional.empty());
            when(genreRepository.findByString("Fiction")).thenReturn(Optional.of(testGenre));
            when(bookFactory.create(eq(ISBN), eq(TITLE), eq(DESCRIPTION), eq(testGenre), anyList(), isNull()))
                    .thenReturn(testBook);
            when(bookRepository.save(testBook)).thenReturn(testBook);

            Book result = bookService.create(createRequest, ISBN);

            assertNotNull(result);
            verify(authorRepository).findByAuthorNumber(AUTHOR_NUMBER);
            verify(authorRepository).findByAuthorNumber(999L);
        }

        @Test
        @DisplayName("Should create book with photo when both photo and URI provided")
        void testCreate_WithPhotoAndURI() {
            MultipartFile mockFile = mock(MultipartFile.class);
            createRequest.setPhoto(mockFile);
            createRequest.setPhotoURI("photo.jpg");

            when(bookRepository.findByIsbn(ISBN)).thenReturn(Optional.empty());
            when(authorRepository.findByAuthorNumber(AUTHOR_NUMBER)).thenReturn(Optional.of(testAuthor));
            when(genreRepository.findByString("Fiction")).thenReturn(Optional.of(testGenre));
            when(bookFactory.create(eq(ISBN), eq(TITLE), eq(DESCRIPTION), eq(testGenre), anyList(), eq("photo.jpg")))
                    .thenReturn(testBook);
            when(bookRepository.save(testBook)).thenReturn(testBook);

            Book result = bookService.create(createRequest, ISBN);

            assertNotNull(result);
            assertEquals("photo.jpg", createRequest.getPhotoURI());
        }

        @Test
        @DisplayName("Should ignore photo when only photo provided (no URI)")
        void testCreate_OnlyPhoto() {
            MultipartFile mockFile = mock(MultipartFile.class);
            createRequest.setPhoto(mockFile);
            createRequest.setPhotoURI(null);

            when(bookRepository.findByIsbn(ISBN)).thenReturn(Optional.empty());
            when(authorRepository.findByAuthorNumber(AUTHOR_NUMBER)).thenReturn(Optional.of(testAuthor));
            when(genreRepository.findByString("Fiction")).thenReturn(Optional.of(testGenre));
            when(bookFactory.create(eq(ISBN), eq(TITLE), eq(DESCRIPTION), eq(testGenre), anyList(), isNull()))
                    .thenReturn(testBook);
            when(bookRepository.save(testBook)).thenReturn(testBook);

            bookService.create(createRequest, ISBN);

            assertNull(createRequest.getPhoto());
            assertNull(createRequest.getPhotoURI());
        }
    }

    // ==========================================
    // update Tests
    // ==========================================

    @Nested
    @DisplayName("update")
    class UpdateTests {

        private UpdateBookRequest updateRequest;

        @BeforeEach
        void setUp() {
            updateRequest = new UpdateBookRequest();
            updateRequest.setIsbn(ISBN);
            updateRequest.setTitle("Updated Title");
        }

        @Test
        @DisplayName("Should update book successfully")
        void testUpdate_Success() {
            when(bookRepository.findByIsbn(ISBN)).thenReturn(Optional.of(testBook));
            when(bookRepository.save(testBook)).thenReturn(testBook);

            Book result = bookService.update(updateRequest, "0");

            assertNotNull(result);
            verify(testBook).applyPatch(eq(0L), eq(updateRequest));
            verify(bookRepository).save(testBook);
        }

        @Test
        @DisplayName("Should throw NotFoundException when book not found")
        void testUpdate_BookNotFound() {
            when(bookRepository.findByIsbn(ISBN)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () ->
                    bookService.update(updateRequest, "0")
            );

            verify(bookRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should update authors when provided")
        void testUpdate_WithAuthors() {
            updateRequest.setAuthors(Arrays.asList(AUTHOR_NUMBER));

            when(bookRepository.findByIsbn(ISBN)).thenReturn(Optional.of(testBook));
            when(authorRepository.findByAuthorNumber(AUTHOR_NUMBER)).thenReturn(Optional.of(testAuthor));
            when(bookRepository.save(testBook)).thenReturn(testBook);

            Book result = bookService.update(updateRequest, "0");

            assertNotNull(result);
            assertNotNull(updateRequest.getAuthorObjList());
            assertEquals(1, updateRequest.getAuthorObjList().size());
        }

        @Test
        @DisplayName("Should skip non-existent authors in update")
        void testUpdate_SkipNonExistentAuthors() {
            updateRequest.setAuthors(Arrays.asList(AUTHOR_NUMBER, 999L));

            when(bookRepository.findByIsbn(ISBN)).thenReturn(Optional.of(testBook));
            when(authorRepository.findByAuthorNumber(AUTHOR_NUMBER)).thenReturn(Optional.of(testAuthor));
            when(authorRepository.findByAuthorNumber(999L)).thenReturn(Optional.empty());
            when(bookRepository.save(testBook)).thenReturn(testBook);

            Book result = bookService.update(updateRequest, "0");

            assertNotNull(result);
            assertEquals(1, updateRequest.getAuthorObjList().size());
        }

        @Test
        @DisplayName("Should update genre when provided")
        void testUpdate_WithGenre() {
            updateRequest.setGenre("Mystery");
            Genre mysteryGenre = new Genre("1","Mystery");

            when(bookRepository.findByIsbn(ISBN)).thenReturn(Optional.of(testBook));
            when(genreRepository.findByString("Mystery")).thenReturn(Optional.of(mysteryGenre));
            when(bookRepository.save(testBook)).thenReturn(testBook);

            Book result = bookService.update(updateRequest, "0");

            assertNotNull(result);
            assertEquals(mysteryGenre, updateRequest.getGenreObj());
        }

        @Test
        @DisplayName("Should throw NotFoundException when genre not found")
        void testUpdate_GenreNotFound() {
            updateRequest.setGenre("NonExistent");

            when(bookRepository.findByIsbn(ISBN)).thenReturn(Optional.of(testBook));
            when(genreRepository.findByString("NonExistent")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () ->
                    bookService.update(updateRequest, "0")
            );
        }

        @Test
        @DisplayName("Should handle photo update with both photo and URI")
        void testUpdate_WithPhotoAndURI() {
            MultipartFile mockFile = mock(MultipartFile.class);
            updateRequest.setPhoto(mockFile);
            updateRequest.setPhotoURI("new-photo.jpg");

            when(bookRepository.findByIsbn(ISBN)).thenReturn(Optional.of(testBook));
            when(bookRepository.save(testBook)).thenReturn(testBook);

            Book result = bookService.update(updateRequest, "0");

            assertNotNull(result);
            assertEquals("new-photo.jpg", updateRequest.getPhotoURI());
        }
    }

    // ==========================================
    // findTop5BooksLent Tests
    // ==========================================

    @Nested
    @DisplayName("findTop5BooksLent")
    class FindTop5BooksLentTests {

        @Test
        @DisplayName("Should return top 5 books")
        void testFindTop5BooksLent_Success() {
            List<BookCountDTO> bookCounts = Arrays.asList(
                    mock(BookCountDTO.class),
                    mock(BookCountDTO.class),
                    mock(BookCountDTO.class)
            );
            Page<BookCountDTO> page = new PageImpl<>(bookCounts);

            when(bookRepository.findTop5BooksLent(any(LocalDate.class), any(PageRequest.class)))
                    .thenReturn(page);

            List<BookCountDTO> result = bookService.findTop5BooksLent();

            assertNotNull(result);
            assertEquals(3, result.size());
            verify(bookRepository).findTop5BooksLent(any(LocalDate.class), any(PageRequest.class));
        }

        @Test
        @DisplayName("Should return empty list when no books lent")
        void testFindTop5BooksLent_Empty() {
            Page<BookCountDTO> emptyPage = new PageImpl<>(Collections.emptyList());

            when(bookRepository.findTop5BooksLent(any(LocalDate.class), any(PageRequest.class)))
                    .thenReturn(emptyPage);

            List<BookCountDTO> result = bookService.findTop5BooksLent();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    // ==========================================
    // removeBookPhoto Tests
    // ==========================================

    @Nested
    @DisplayName("removeBookPhoto")
    class RemoveBookPhotoTests {

        @Test
        @DisplayName("Should remove photo successfully")
        void testRemoveBookPhoto_Success() {
            Book bookWithPhoto = mock(Book.class);
            pt.psoft.g1.psoftg1.shared.model.Photo photo = mock(pt.psoft.g1.psoftg1.shared.model.Photo.class);

            when(bookRepository.findByIsbn(ISBN)).thenReturn(Optional.of(bookWithPhoto));
            when(bookWithPhoto.getPhoto()).thenReturn(photo);
            when(photo.getPhotoFile()).thenReturn("photo.jpg");
            when(bookRepository.save(bookWithPhoto)).thenReturn(bookWithPhoto);

            Book result = bookService.removeBookPhoto(ISBN, 0L);

            assertNotNull(result);
            verify(bookWithPhoto).removePhoto(0L);
            verify(photoRepository).deleteByPhotoFile("photo.jpg");
            verify(bookRepository).save(bookWithPhoto);
        }

        @Test
        @DisplayName("Should throw NotFoundException when book not found")
        void testRemoveBookPhoto_BookNotFound() {
            when(bookRepository.findByIsbn(ISBN)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () ->
                    bookService.removeBookPhoto(ISBN, 0L)
            );

            verify(photoRepository, never()).deleteByPhotoFile(anyString());
        }

        @Test
        @DisplayName("Should throw NotFoundException when book has no photo")
        void testRemoveBookPhoto_NoPhoto() {
            Book bookNoPhoto = mock(Book.class);
            when(bookRepository.findByIsbn(ISBN)).thenReturn(Optional.of(bookNoPhoto));
            when(bookNoPhoto.getPhoto()).thenReturn(null);

            assertThrows(NotFoundException.class, () ->
                    bookService.removeBookPhoto(ISBN, 0L)
            );

            verify(photoRepository, never()).deleteByPhotoFile(anyString());
        }
    }

    // ==========================================
    // Search and Find Tests
    // ==========================================

    @Nested
    @DisplayName("Search and Find")
    class SearchAndFindTests {

        @Test
        @DisplayName("Should find books by genre")
        void testFindByGenre() {
            List<Book> books = Arrays.asList(testBook);
            when(bookRepository.findByGenre("Fiction")).thenReturn(books);

            List<Book> result = bookService.findByGenre("Fiction");

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(bookRepository).findByGenre("Fiction");
        }

        @Test
        @DisplayName("Should find books by title")
        void testFindByTitle() {
            List<Book> books = Arrays.asList(testBook);
            when(bookRepository.findByTitle(TITLE)).thenReturn(books);

            List<Book> result = bookService.findByTitle(TITLE);

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(bookRepository).findByTitle(TITLE);
        }

        @Test
        @DisplayName("Should find books by author name")
        void testFindByAuthorName() {
            List<Book> books = Arrays.asList(testBook);
            when(bookRepository.findByAuthorName("Test Author%")).thenReturn(books);

            List<Book> result = bookService.findByAuthorName("Test Author");

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(bookRepository).findByAuthorName("Test Author%");
        }

        @Test
        @DisplayName("Should find book by ISBN")
        void testFindByIsbn_Found() {
            when(bookRepository.findByIsbn(ISBN)).thenReturn(Optional.of(testBook));

            Book result = bookService.findByIsbn(ISBN);

            assertNotNull(result);
            verify(bookRepository).findByIsbn(ISBN);
        }

        @Test
        @DisplayName("Should throw NotFoundException when ISBN not found")
        void testFindByIsbn_NotFound() {
            when(bookRepository.findByIsbn(ISBN)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () ->
                    bookService.findByIsbn(ISBN)
            );
        }

        @Test
        @DisplayName("Should search books with query")
        void testSearchBooks() {
            pt.psoft.g1.psoftg1.shared.services.Page page =
                    new pt.psoft.g1.psoftg1.shared.services.Page(1, 10);
            SearchBooksQuery query = new SearchBooksQuery(TITLE, "Fiction", "Author");
            List<Book> books = Arrays.asList(testBook);

            when(bookRepository.searchBooks(page, query)).thenReturn(books);

            List<Book> result = bookService.searchBooks(page, query);

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(bookRepository).searchBooks(page, query);
        }

        @Test
        @DisplayName("Should use default page when null")
        void testSearchBooks_NullPage() {
            SearchBooksQuery query = new SearchBooksQuery(TITLE, "", "");

            when(bookRepository.searchBooks(any(), eq(query))).thenReturn(Collections.emptyList());

            List<Book> result = bookService.searchBooks(null, query);

            assertNotNull(result);
            verify(bookRepository).searchBooks(any(), eq(query));
        }

        @Test
        @DisplayName("Should use default query when null")
        void testSearchBooks_NullQuery() {
            pt.psoft.g1.psoftg1.shared.services.Page page =
                    new pt.psoft.g1.psoftg1.shared.services.Page(1, 10);

            when(bookRepository.searchBooks(eq(page), any())).thenReturn(Collections.emptyList());

            List<Book> result = bookService.searchBooks(page, null);

            assertNotNull(result);
            verify(bookRepository).searchBooks(eq(page), any());
        }
    }

    // ==========================================
    // Book Suggestions Tests
    // ==========================================

    @Nested
    @DisplayName("getBooksSuggestionsForReader")
    class BookSuggestionsTests {

        private ReaderDetails readerDetails;
        private static final String READER_NUMBER = "2024/1";

        @BeforeEach
        void setUp() {
            readerDetails = mock(ReaderDetails.class);
        }

        @Test
        @DisplayName("Should return suggestions based on reader interests")
        void testGetSuggestions_Success() {
            List<Genre> interests = Arrays.asList(testGenre);
            List<Book> books = Arrays.asList(testBook, mock(Book.class));

            when(readerRepository.findByReaderNumber(READER_NUMBER))
                    .thenReturn(Optional.of(readerDetails));
            when(readerDetails.getInterestList()).thenReturn(interests);
            when(bookRepository.findByGenre("Fiction")).thenReturn(books);

            List<Book> result = bookService.getBooksSuggestionsForReader(READER_NUMBER);

            assertNotNull(result);
            assertEquals(2, result.size());
            verify(readerRepository).findByReaderNumber(READER_NUMBER);
        }

        @Test
        @DisplayName("Should throw NotFoundException when reader not found")
        void testGetSuggestions_ReaderNotFound() {
            when(readerRepository.findByReaderNumber(READER_NUMBER))
                    .thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () ->
                    bookService.getBooksSuggestionsForReader(READER_NUMBER)
            );
        }

        @Test
        @DisplayName("Should throw NotFoundException when reader has no interests")
        void testGetSuggestions_NoInterests() {
            when(readerRepository.findByReaderNumber(READER_NUMBER))
                    .thenReturn(Optional.of(readerDetails));
            when(readerDetails.getInterestList()).thenReturn(Collections.emptyList());

            assertThrows(NotFoundException.class, () ->
                    bookService.getBooksSuggestionsForReader(READER_NUMBER)
            );
        }

        @Test
        @DisplayName("Should limit suggestions per genre")
        void testGetSuggestions_LimitPerGenre() {
            List<Genre> interests = Arrays.asList(testGenre);
            List<Book> manyBooks = Arrays.asList(
                    mock(Book.class), mock(Book.class), mock(Book.class),
                    mock(Book.class), mock(Book.class), mock(Book.class),
                    mock(Book.class), mock(Book.class)
            );

            when(readerRepository.findByReaderNumber(READER_NUMBER))
                    .thenReturn(Optional.of(readerDetails));
            when(readerDetails.getInterestList()).thenReturn(interests);
            when(bookRepository.findByGenre("Fiction")).thenReturn(manyBooks);

            List<Book> result = bookService.getBooksSuggestionsForReader(READER_NUMBER);

            assertNotNull(result);
            assertEquals(5, result.size()); // Limited to suggestionsLimitPerGenre
        }

        @Test
        @DisplayName("Should skip genres with no books")
        void testGetSuggestions_SkipEmptyGenres() {
            Genre emptyGenre = new Genre("1","Empty");
            List<Genre> interests = Arrays.asList(emptyGenre, testGenre);
            List<Book> books = Arrays.asList(testBook);

            when(readerRepository.findByReaderNumber(READER_NUMBER))
                    .thenReturn(Optional.of(readerDetails));
            when(readerDetails.getInterestList()).thenReturn(interests);
            when(bookRepository.findByGenre("Empty")).thenReturn(Collections.emptyList());
            when(bookRepository.findByGenre("Fiction")).thenReturn(books);

            List<Book> result = bookService.getBooksSuggestionsForReader(READER_NUMBER);

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    // ==========================================
    // External API Tests
    // ==========================================

    @Nested
    @DisplayName("findIsbnFromExternalApi")
    class ExternalApiTests {

        @Test
        @DisplayName("Should find ISBN from external API")
        void testFindIsbn_Success() {
            when(libraryApi.getProviderName()).thenReturn("TestProvider");
            when(libraryApi.findIsbnByTitle(TITLE)).thenReturn(Optional.of(ISBN));

            Optional<String> result = bookService.findIsbnFromExternalApi(TITLE);

            assertTrue(result.isPresent());
            assertEquals(ISBN, result.get());
            verify(libraryApi).findIsbnByTitle(TITLE);
        }

        @Test
        @DisplayName("Should return empty when ISBN not found")
        void testFindIsbn_NotFound() {
            when(libraryApi.getProviderName()).thenReturn("TestProvider");
            when(libraryApi.findIsbnByTitle(TITLE)).thenReturn(Optional.empty());

            Optional<String> result = bookService.findIsbnFromExternalApi(TITLE);

            assertFalse(result.isPresent());
        }

        @Test
        @DisplayName("Should return empty when API throws exception")
        void testFindIsbn_Exception() {
            when(libraryApi.getProviderName()).thenReturn("TestProvider");
            when(libraryApi.findIsbnByTitle(TITLE)).thenThrow(new RuntimeException("API Error"));

            Optional<String> result = bookService.findIsbnFromExternalApi(TITLE);

            assertFalse(result.isPresent());
        }
    }

    // ==========================================
    // save Tests
    // ==========================================

    @Nested
    @DisplayName("save")
    class SaveTests {

        @Test
        @DisplayName("Should save book")
        void testSave() {
            when(bookRepository.save(testBook)).thenReturn(testBook);

            Book result = bookService.save(testBook);

            assertNotNull(result);
            assertEquals(testBook, result);
            verify(bookRepository).save(testBook);
        }
    }
}