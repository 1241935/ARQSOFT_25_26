package pt.psoft.g1.psoftg1.bookmanagement.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DTO for receiving Book events from the Books microservice via RabbitMQ.
 * Matches the BookViewAMQP structure from the Books microservice.
 */
@Data
@NoArgsConstructor
public class BookViewAMQP {

    private String isbn;
    private String title;
    private String description;
    private List<Long> authorIds;
    private String genre;
    private Long version;
    private Map<String, Object> _links = new HashMap<>();

    public BookViewAMQP(String isbn, String title, String description, List<Long> authorIds, String genre) {
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.authorIds = authorIds;
        this.genre = genre;
    }
}

