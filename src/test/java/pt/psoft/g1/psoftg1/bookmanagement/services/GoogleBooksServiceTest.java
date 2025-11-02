package pt.psoft.g1.psoftg1.bookmanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoogleBooksServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    private GoogleBooksService googleBooksService;

    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=intitle:{title}";

    @BeforeEach
    void setUp() {
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        googleBooksService = new GoogleBooksService(restTemplateBuilder);
    }

    @Test
    void testFindIsbnByTitle_Success() {
        // Arrange
        String title = "Clean Code";
        String expectedIsbn = "9780132350884";

        Map<String, String> isbn13 = Map.of("type", "ISBN_13", "identifier", expectedIsbn);
        Map<String, String> isbn10 = Map.of("type", "ISBN_10", "identifier", "0132350882");

        Map<String, Object> volumeInfo = new HashMap<>();
        volumeInfo.put("title", "Clean Code");
        volumeInfo.put("industryIdentifiers", List.of(isbn10, isbn13));

        Map<String, Object> item = Map.of("volumeInfo", volumeInfo);

        Map<String, Object> responseBody = Map.of("items", List.of(item));

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(restTemplate.getForEntity(API_URL, Map.class, title)).thenReturn(responseEntity);

        // Act
        Optional<String> result = googleBooksService.findIsbnByTitle(title);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedIsbn, result.get());
    }

    @Test
    void testFindIsbnByTitle_BookNotFound() {
        // Arrange
        String title = "Nonexistent";
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("items", null);

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        when(restTemplate.getForEntity(API_URL, Map.class, title)).thenReturn(responseEntity);

        // Act
        Optional<String> result = googleBooksService.findIsbnByTitle(title);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testFindIsbnByTitle_EmptyItemsList() {
        // Arrange
        String title = "Empty List";
        Map<String, Object> responseBody = Map.of("items", List.of());
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        when(restTemplate.getForEntity(API_URL, Map.class, title)).thenReturn(responseEntity);

        // Act
        Optional<String> result = googleBooksService.findIsbnByTitle(title);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testFindIsbnByTitle_NoIndustryIdentifiers() {
        // Arrange
        String title = "No ISBN";
        Map<String, Object> volumeInfo = Map.of("title", "Book without ISBN");
        Map<String, Object> item = Map.of("volumeInfo", volumeInfo);
        Map<String, Object> responseBody = Map.of("items", List.of(item));

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        when(restTemplate.getForEntity(API_URL, Map.class, title)).thenReturn(responseEntity);

        // Act
        Optional<String> result = googleBooksService.findIsbnByTitle(title);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testFindIsbnByTitle_NoIsbn13() {
        // Arrange
        String title = "Only ISBN10";

        Map<String, String> isbn10 = Map.of("type", "ISBN_10", "identifier", "0132350882");
        Map<String, Object> volumeInfo = Map.of("industryIdentifiers", List.of(isbn10));

        Map<String, Object> item = Map.of("volumeInfo", volumeInfo);
        Map<String, Object> responseBody = Map.of("items", List.of(item));

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        when(restTemplate.getForEntity(API_URL, Map.class, title)).thenReturn(responseEntity);

        // Act
        Optional<String> result = googleBooksService.findIsbnByTitle(title);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testFindIsbnByTitle_MultipleItems_UsesFirst() {
        // Arrange
        String title = "Java";
        String expectedIsbn = "9780134685991";

        Map<String, Object> book1 = Map.of(
                "volumeInfo", Map.of(
                        "industryIdentifiers", List.of(Map.of("type", "ISBN_13", "identifier", expectedIsbn))
                )
        );

        Map<String, Object> book2 = Map.of(
                "volumeInfo", Map.of(
                        "industryIdentifiers", List.of(Map.of("type", "ISBN_13", "identifier", "9780596009205"))
                )
        );

        Map<String, Object> responseBody = Map.of("items", List.of(book1, book2));
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        when(restTemplate.getForEntity(API_URL, Map.class, title)).thenReturn(responseEntity);

        // Act
        Optional<String> result = googleBooksService.findIsbnByTitle(title);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedIsbn, result.get());
    }

    @Test
    void testFindIsbnByTitle_Exception_ReturnsEmpty() {
        // Arrange
        String title = "Error";
        when(restTemplate.getForEntity(API_URL, Map.class, title))
                .thenThrow(new RuntimeException("Connection timeout"));

        // Act
        Optional<String> result = googleBooksService.findIsbnByTitle(title);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testGetProviderName() {
        assertEquals("Google Books", googleBooksService.getProviderName());
    }
}
