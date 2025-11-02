package pt.psoft.g1.psoftg1.bookmanagement.services;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Primary
public class GoogleBooksService implements LibraryApi {

    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=intitle:{title}";

    private final RestTemplate restTemplate;

    public GoogleBooksService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public Optional<String> findIsbnByTitle(String title) {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(API_URL, Map.class, title);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");

                if (items != null && !items.isEmpty()) {
                    Map<String, Object> volumeInfo = (Map<String, Object>) items.get(0).get("volumeInfo");
                    List<Map<String, String>> identifiers =
                            (List<Map<String, String>>) volumeInfo.get("industryIdentifiers");

                    if (identifiers != null) {
                        for (Map<String, String> id : identifiers) {
                            if ("ISBN_13".equals(id.get("type"))) {
                                return Optional.ofNullable(id.get("identifier"));
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            // Exception Handling
        }

        return Optional.empty();
    }

    @Override
    public String getProviderName() {
        return "Google Books";
    }
}
