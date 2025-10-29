package pt.psoft.g1.psoftg1.bookmanagement.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Primary
public class IsbndbService implements LibraryApi {

    private static final String API_URL = "https://api2.isbndb.com/books/{title}";

    private final RestTemplate restTemplate;
    private final String apiKey;

    public IsbndbService(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${isbndb.api.key}") String apiKey
    ) {
        this.restTemplate = restTemplateBuilder.build();
        this.apiKey = apiKey;
    }

    @Override
    public Optional<String> findIsbnByTitle(String title) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", apiKey);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    API_URL,
                    HttpMethod.GET,
                    entity,
                    Map.class,
                    title
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                // Parse da resposta JSON
                Map<String, Object> body = response.getBody();
                List<Map<String, Object>> books = (List<Map<String, Object>>) body.get("books");

                if (books != null && !books.isEmpty()) {
                    String isbn = (String) books.get(0).get("isbn13");
                    return Optional.ofNullable(isbn);
                }
            }

        } catch (HttpClientErrorException.NotFound e) {
            // livro não encontrado
        } catch (Exception e) {
            // logar exceção, se quiseres
        }

        return Optional.empty();
    }

    @Override
    public String getProviderName() {
        return "ISBNdb";
    }

}