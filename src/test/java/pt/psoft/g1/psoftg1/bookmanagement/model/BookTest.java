package pt.psoft.g1.psoftg1.bookmanagement.model;

import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.factories.BookFactory;
import pt.psoft.g1.psoftg1.bookmanagement.services.UpdateBookRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookTest {
    private final String validIsbn = "9782826012092";
    private final String validTitle = "Encantos de contar";
    private final Author validAuthor1 = new Author("João Alberto", "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.", null);
    private final Author validAuthor2 = new Author("Maria José", "A Maria José nasceu em Viseu e só come laranjas às segundas feiras.", null);
    private final Genre validGenre = new Genre("1","Fantasia");
    private ArrayList<Author> authors = new ArrayList<>();

    @BeforeEach
    void setUp(){
        authors.clear();
    }

    @Test
    void ensureIsbnNotNull(){
        authors.add(validAuthor1);
        assertThrows(IllegalArgumentException.class, () -> new Book(null,null, validTitle, null, validGenre, authors, null));
    }

    @Test
    void ensureTitleNotNull(){
        authors.add(validAuthor1);
        assertThrows(IllegalArgumentException.class, () -> new Book(null,validIsbn, null, null, validGenre, authors, null));
    }

    @Test
    void ensureGenreNotNull(){
        authors.add(validAuthor1);
        assertThrows(IllegalArgumentException.class, () -> new Book(null,validIsbn, validTitle, null,null, authors, null));
    }

    @Test
    void ensureAuthorsNotNull(){
        authors.add(validAuthor1);
        assertThrows(IllegalArgumentException.class, () -> new Book(null,validIsbn, validTitle, null, validGenre, null, null));
    }

    @Test
    void ensureAuthorsNotEmpty(){
        assertThrows(IllegalArgumentException.class, () -> new Book(null,validIsbn, validTitle, null, validGenre, authors, null));
    }

    @Test
    void ensureBookCreatedWithMultipleAuthors() {
        authors.add(validAuthor1);
        authors.add(validAuthor2);
        assertDoesNotThrow(() -> new Book(null,validIsbn, validTitle, null, validGenre, authors, null));
    }

    @Test
    void whenValidParameters_thenBookIsCreated() {
        // Arrange
        String pk = "1";
        String isbn = "9782826012092";
        String title = "Ensaio sobre a Cegueira";
        String description = "A blindness epidemic strikes";
        String photoURI = "book.jpg";

        Genre genreDouble = mock(Genre.class);
        Author authorDouble = mock(Author.class);
        List<Author> authors = new ArrayList<>();
        authors.add(authorDouble);

        // Act
        Book book = new Book(pk, isbn, title, description, genreDouble, authors, photoURI);

        // Assert
        assertNotNull(book);
        assertEquals(isbn, book.getIsbn());
        assertEquals(title, book.getTitle().toString());
        assertEquals(description, book.getDescription());
        assertEquals(genreDouble, book.getGenre());
        assertEquals(1, book.getAuthors().size());
        assertNotNull(book.getPhoto());
        assertEquals(photoURI, book.getPhoto().getPhotoFile());
    }

    @Test
    void whenCreateBookUsingFactory_thenBookIsCreatedWithGeneratedPk() {
        // Arrange
        String isbn = "9782826012092";
        String title = "Ensaio sobre a Cegueira";
        String description = "A blindness epidemic strikes";
        String photoURI = "book.jpg";

        Genre genreDouble = mock(Genre.class);
        Author authorDouble = mock(Author.class);
        List<Author> authors = new ArrayList<>();
        authors.add(authorDouble);

        BookFactory factoryDouble = mock(BookFactory.class);
        Book bookDouble = mock(Book.class);

        when(factoryDouble.create(isbn, title, description, genreDouble, authors, photoURI))
                .thenReturn(bookDouble);

        // Act
        Book book = factoryDouble.create(isbn, title, description, genreDouble, authors, photoURI);

        // Assert
        assertNotNull(book);
        verify(factoryDouble).create(isbn, title, description, genreDouble, authors, photoURI);
    }

    @Test
    void whenNullGenre_thenThrowsIllegalArgumentException() {
        // Arrange
        String expectedMessage = "Genre cannot be null";
        String pk = "1";
        String isbn = "9782826012092";
        String title = "Ensaio sobre a Cegueira";
        String description = "A blindness epidemic strikes";
        String photoURI = "http://example.com/book.jpg";

        Author authorDouble = mock(Author.class);
        List<Author> authors = new ArrayList<>();
        authors.add(authorDouble);

        // Act + Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Book(pk, isbn, title, description, null, authors, photoURI)
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void whenNullAuthorList_thenThrowsIllegalArgumentException() {
        // Arrange
        String expectedMessage = "Author list is null";
        String pk = "1";
        String isbn = "9782826012092";
        String title = "Ensaio sobre a Cegueira";
        String description = "A blindness epidemic strikes";
        String photoURI = "http://example.com/book.jpg";

        Genre genreDouble = mock(Genre.class);

        // Act + Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Book(pk, isbn, title, description, genreDouble, null, photoURI)
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void whenEmptyAuthorList_thenThrowsIllegalArgumentException() {
        // Arrange
        String expectedMessage = "Author list is empty";
        String pk = "1";
        String isbn = "9782826012092";
        String title = "Ensaio sobre a Cegueira";
        String description = "A blindness epidemic strikes";
        String photoURI = "http://example.com/book.jpg";

        Genre genreDouble = mock(Genre.class);
        List<Author> authors = new ArrayList<>();

        // Act + Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Book(pk, isbn, title, description, genreDouble, authors, photoURI)
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void whenValidBookWithMultipleAuthors_thenBookIsCreated() {
        // Arrange
        String pk = "1";
        String isbn = "9782826012092";
        String title = "Book Title";
        String description = "Book Description";
        String photoURI = "http://example.com/book.jpg";

        Genre genreDouble = mock(Genre.class);
        Author authorDouble1 = mock(Author.class);
        Author authorDouble2 = mock(Author.class);
        List<Author> authors = new ArrayList<>();
        authors.add(authorDouble1);
        authors.add(authorDouble2);

        // Act
        Book book = new Book(pk, isbn, title, description, genreDouble, authors, photoURI);

        // Assert
        assertEquals(2, book.getAuthors().size());
    }

    @Test
    void whenApplyPatchWithValidVersionAndTitle_thenTitleIsUpdated() {
        // Arrange
        String pk = "1";
        String isbn = "9782826012092";
        String title = "Original Title";
        String description = "Book Description";
        String photoURI = "http://example.com/book.jpg";

        Genre genreDouble = mock(Genre.class);
        Author authorDouble = mock(Author.class);
        List<Author> authors = new ArrayList<>();
        authors.add(authorDouble);

        Book book = new Book(pk, isbn, title, description, genreDouble, authors, photoURI);

        UpdateBookRequest request = mock(UpdateBookRequest.class);
        when(request.getTitle()).thenReturn("New Title");
        when(request.getDescription()).thenReturn(null);
        when(request.getGenreObj()).thenReturn(null);
        when(request.getAuthorObjList()).thenReturn(null);
        when(request.getPhotoURI()).thenReturn(null);

        Long currentVersion = book.getVersion();

        // Act
        book.applyPatch(currentVersion, request);

        // Assert
        assertEquals("New Title", book.getTitle().toString());
    }

    @Test
    void whenApplyPatchWithValidVersionAndDescription_thenDescriptionIsUpdated() {
        // Arrange
        String pk = "1";
        String isbn = "9782826012092";
        String title = "Book Title";
        String description = "Original Description";
        String photoURI = "http://example.com/book.jpg";

        Genre genreDouble = mock(Genre.class);
        Author authorDouble = mock(Author.class);
        List<Author> authors = new ArrayList<>();
        authors.add(authorDouble);

        Book book = new Book(pk, isbn, title, description, genreDouble, authors, photoURI);

        UpdateBookRequest request = mock(UpdateBookRequest.class);
        when(request.getTitle()).thenReturn(null);
        when(request.getDescription()).thenReturn("New Description");
        when(request.getGenreObj()).thenReturn(null);
        when(request.getAuthorObjList()).thenReturn(null);
        when(request.getPhotoURI()).thenReturn(null);

        Long currentVersion = book.getVersion();

        // Act
        book.applyPatch(currentVersion, request);

        // Assert
        assertEquals("New Description", book.getDescription());
    }

    @Test
    void whenApplyPatchWithValidVersionAndGenre_thenGenreIsUpdated() {
        // Arrange
        String pk = "1";
        String isbn = "9782826012092";
        String title = "Book Title";
        String description = "Book Description";
        String photoURI = "http://example.com/book.jpg";

        Genre originalGenreDouble = mock(Genre.class);
        Genre newGenreDouble = mock(Genre.class);
        Author authorDouble = mock(Author.class);
        List<Author> authors = new ArrayList<>();
        authors.add(authorDouble);

        Book book = new Book(pk, isbn, title, description, originalGenreDouble, authors, photoURI);

        UpdateBookRequest request = mock(UpdateBookRequest.class);
        when(request.getTitle()).thenReturn(null);
        when(request.getDescription()).thenReturn(null);
        when(request.getGenreObj()).thenReturn(newGenreDouble);
        when(request.getAuthorObjList()).thenReturn(null);
        when(request.getPhotoURI()).thenReturn(null);

        Long currentVersion = book.getVersion();

        // Act
        book.applyPatch(currentVersion, request);

        // Assert
        assertEquals(newGenreDouble, book.getGenre());
    }

    @Test
    void whenApplyPatchWithValidVersionAndAuthors_thenAuthorsAreUpdated() {
        // Arrange
        String pk = "1";
        String isbn = "9782826012092";
        String title = "Book Title";
        String description = "Book Description";
        String photoURI = "http://example.com/book.jpg";

        Genre genreDouble = mock(Genre.class);
        Author authorDouble1 = mock(Author.class);
        Author authorDouble2 = mock(Author.class);
        List<Author> originalAuthors = new ArrayList<>();
        originalAuthors.add(authorDouble1);

        Book book = new Book(pk, isbn, title, description, genreDouble, originalAuthors, photoURI);

        List<Author> newAuthors = new ArrayList<>();
        newAuthors.add(authorDouble2);

        UpdateBookRequest request = mock(UpdateBookRequest.class);
        when(request.getTitle()).thenReturn(null);
        when(request.getDescription()).thenReturn(null);
        when(request.getGenreObj()).thenReturn(null);
        when(request.getAuthorObjList()).thenReturn(newAuthors);
        when(request.getPhotoURI()).thenReturn(null);

        Long currentVersion = book.getVersion();

        // Act
        book.applyPatch(currentVersion, request);

        // Assert
        assertEquals(1, book.getAuthors().size());
        assertEquals(authorDouble2, book.getAuthors().get(0));
    }

    @Test
    void whenApplyPatchWithValidVersionAndPhoto_thenPhotoIsUpdated() {
        // Arrange
        String pk = "1";
        String isbn = "9782826012092";
        String title = "Book Title";
        String description = "Book Description";
        String photoURI = "book.jpg";

        Genre genreDouble = mock(Genre.class);
        Author authorDouble = mock(Author.class);
        List<Author> authors = new ArrayList<>();
        authors.add(authorDouble);

        Book book = new Book(pk, isbn, title, description, genreDouble, authors, photoURI);

        UpdateBookRequest request = mock(UpdateBookRequest.class);
        when(request.getTitle()).thenReturn(null);
        when(request.getDescription()).thenReturn(null);
        when(request.getGenreObj()).thenReturn(null);
        when(request.getAuthorObjList()).thenReturn(null);
        when(request.getPhotoURI()).thenReturn("new-book.jpg");

        Long currentVersion = book.getVersion();

        // Act
        book.applyPatch(currentVersion, request);

        // Assert
        assertNotNull(book.getPhoto());
        assertEquals("new-book.jpg", book.getPhoto().getPhotoFile());
    }

    @Test
    void whenApplyPatchWithAllFields_thenAllFieldsAreUpdated() {
        // Arrange
        String pk = "1";
        String isbn = "9782826012092";
        String title = "Original Title";
        String description = "Original Description";
        String photoURI = "book.jpg";

        Genre originalGenreDouble = mock(Genre.class);
        Genre newGenreDouble = mock(Genre.class);
        Author authorDouble1 = mock(Author.class);
        Author authorDouble2 = mock(Author.class);
        List<Author> originalAuthors = new ArrayList<>();
        originalAuthors.add(authorDouble1);

        Book book = new Book(pk, isbn, title, description, originalGenreDouble, originalAuthors, photoURI);

        List<Author> newAuthors = new ArrayList<>();
        newAuthors.add(authorDouble2);

        UpdateBookRequest request = mock(UpdateBookRequest.class);
        when(request.getTitle()).thenReturn("New Title");
        when(request.getDescription()).thenReturn("New Description");
        when(request.getGenreObj()).thenReturn(newGenreDouble);
        when(request.getAuthorObjList()).thenReturn(newAuthors);
        when(request.getPhotoURI()).thenReturn("new-book.jpg");

        Long currentVersion = book.getVersion();

        // Act
        book.applyPatch(currentVersion, request);

        // Assert
        assertEquals("New Title", book.getTitle().toString());
        assertEquals("New Description", book.getDescription());
        assertEquals(newGenreDouble, book.getGenre());
        assertEquals(1, book.getAuthors().size());
        assertEquals(authorDouble2, book.getAuthors().get(0));
        assertNotNull(book.getPhoto());
        assertEquals("new-book.jpg", book.getPhoto().getPhotoFile());
    }

    @Test
    void whenApplyPatchWithNullFields_thenOriginalValuesAreKept() {
        // Arrange
        String pk = "1";
        String isbn = "9782826012092";
        String title = "Original Title";
        String description = "Original Description";
        String photoURI = "book.jpg";

        Genre genreDouble = mock(Genre.class);
        Author authorDouble = mock(Author.class);
        List<Author> authors = new ArrayList<>();
        authors.add(authorDouble);

        Book book = new Book(pk, isbn, title, description, genreDouble, authors, photoURI);

        UpdateBookRequest request = mock(UpdateBookRequest.class);
        when(request.getTitle()).thenReturn(null);
        when(request.getDescription()).thenReturn(null);
        when(request.getGenreObj()).thenReturn(null);
        when(request.getAuthorObjList()).thenReturn(null);
        when(request.getPhotoURI()).thenReturn(null);

        Long currentVersion = book.getVersion();

        // Act
        book.applyPatch(currentVersion, request);

        // Assert
        assertEquals(title, book.getTitle().toString());
        assertEquals(description, book.getDescription());
        assertEquals(genreDouble, book.getGenre());
        assertEquals(1, book.getAuthors().size());
        assertNotNull(book.getPhoto());
        assertEquals(photoURI, book.getPhoto().getPhotoFile());
    }

    @Test
    void whenGetPk_thenReturnsPk() {
        // Arrange
        String pk = "1";
        String isbn = "9782826012092";
        String title = "Book Title";
        String description = "Book Description";
        String photoURI = "http://example.com/book.jpg";

        Genre genreDouble = mock(Genre.class);
        Author authorDouble = mock(Author.class);
        List<Author> authors = new ArrayList<>();
        authors.add(authorDouble);

        // Act
        Book book = new Book(pk, isbn, title, description, genreDouble, authors, photoURI);

        // Assert
        assertEquals(pk, book.getPk());
    }
}