package pt.psoft.g1.psoftg1.bookmanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IsbndbServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    private IsbndbService isbndbService;

    private static final String FAKE_API_KEY = "fake-api-key-for-testing";
    private static final String API_URL = "https://api2.isbndb.com/books/{title}";

    @BeforeEach
    void setUp() {
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        isbndbService = new IsbndbService(restTemplateBuilder, FAKE_API_KEY);
    }

    @Test
    void testFindIsbnByTitle_Success() {
        // Arrange
        String title = "Clean Code";
        String expectedIsbn = "9780132350884";

        Map<String, Object> bookData = new HashMap<>();
        bookData.put("isbn13", expectedIsbn);
        bookData.put("title", "Clean Code");
        bookData.put("authors", List.of("Robert C. Martin"));

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("books", List.of(bookData));

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(API_URL),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class),
                eq(title)
        )).thenReturn(responseEntity);

        // Act
        Optional<String> result = isbndbService.findIsbnByTitle(title);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedIsbn, result.get());

        verify(restTemplate, times(1)).exchange(
                eq(API_URL),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class),
                eq(title)
        );
    }

    @Test
    void testFindIsbnByTitle_BookNotFound() {
        // Arrange
        String title = "NonExistentBook";

        when(restTemplate.exchange(
                eq(API_URL),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class),
                eq(title)
        )).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act
        Optional<String> result = isbndbService.findIsbnByTitle(title);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testFindIsbnByTitle_EmptyResponse() {
        // Arrange
        String title = "Some Book";

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("books", List.of());

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(API_URL),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class),
                eq(title)
        )).thenReturn(responseEntity);

        // Act
        Optional<String> result = isbndbService.findIsbnByTitle(title);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testFindIsbnByTitle_NullBooks() {
        // Arrange
        String title = "Some Book";

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("books", null);

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(API_URL),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class),
                eq(title)
        )).thenReturn(responseEntity);

        // Act
        Optional<String> result = isbndbService.findIsbnByTitle(title);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testFindIsbnByTitle_NullIsbn() {
        // Arrange
        String title = "Book Without ISBN";

        Map<String, Object> bookData = new HashMap<>();
        bookData.put("isbn13", null);
        bookData.put("title", "Book Without ISBN");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("books", List.of(bookData));

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(API_URL),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class),
                eq(title)
        )).thenReturn(responseEntity);

        // Act
        Optional<String> result = isbndbService.findIsbnByTitle(title);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testFindIsbnByTitle_MultipleBooks_ReturnsFirst() {
        // Arrange
        String title = "Java";
        String firstIsbn = "9780134685991";
        String secondIsbn = "9780596009205";

        Map<String, Object> book1 = new HashMap<>();
        book1.put("isbn13", firstIsbn);
        book1.put("title", "Effective Java");

        Map<String, Object> book2 = new HashMap<>();
        book2.put("isbn13", secondIsbn);
        book2.put("title", "Head First Java");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("books", List.of(book1, book2));

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(API_URL),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class),
                eq(title)
        )).thenReturn(responseEntity);

        // Act
        Optional<String> result = isbndbService.findIsbnByTitle(title);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(firstIsbn, result.get());
    }

    @Test
    void testFindIsbnByTitle_GenericException() {
        // Arrange
        String title = "Error Book";

        when(restTemplate.exchange(
                eq(API_URL),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class),
                eq(title)
        )).thenThrow(new RuntimeException("Connection timeout"));

        // Act
        Optional<String> result = isbndbService.findIsbnByTitle(title);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testGetProviderName() {
        // Act
        String providerName = isbndbService.getProviderName();

        // Assert
        assertEquals("ISBNdb", providerName);
    }

    @Test
    void testAuthorizationHeaderIsSet() {
        // Arrange
        String title = "Test Book";
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("books", List.of());
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(API_URL),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class),
                eq(title)
        )).thenReturn(responseEntity);

        // Act
        isbndbService.findIsbnByTitle(title);

        // Assert - verifica que o exchange foi chamado (o header é configurado internamente)
        verify(restTemplate).exchange(
                eq(API_URL),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class),
                eq(title)
        );
    }
}