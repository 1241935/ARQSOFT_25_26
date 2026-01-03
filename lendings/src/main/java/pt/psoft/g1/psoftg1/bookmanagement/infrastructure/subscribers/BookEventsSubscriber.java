package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.subscribers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookSyncService;

/**
 * RabbitMQ subscriber for Book events from the Books microservice.
 * Implements the Strangler Fig pattern by synchronizing book data locally.
 */
@Profile("!test")
@Component
@RequiredArgsConstructor
@Slf4j
public class BookEventsSubscriber {

    private final BookSyncService bookSyncService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "lendings.book.created")
    public void handleBookCreated(String message) {
        log.info("Received BOOK_CREATED event");
        try {
            BookViewAMQP bookView = objectMapper.readValue(message, BookViewAMQP.class);
            bookSyncService.createOrUpdateBook(bookView);
            log.info("Successfully synchronized book: {}", bookView.getIsbn());
        } catch (Exception e) {
            log.error("Error processing BOOK_CREATED event: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = "lendings.book.updated")
    public void handleBookUpdated(String message) {
        log.info("Received BOOK_UPDATED event");
        try {
            BookViewAMQP bookView = objectMapper.readValue(message, BookViewAMQP.class);
            bookSyncService.createOrUpdateBook(bookView);
            log.info("Successfully updated book: {}", bookView.getIsbn());
        } catch (Exception e) {
            log.error("Error processing BOOK_UPDATED event: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = "lendings.book.deleted")
    public void handleBookDeleted(String message) {
        log.info("Received BOOK_DELETED event");
        try {
            BookViewAMQP bookView = objectMapper.readValue(message, BookViewAMQP.class);
            bookSyncService.deleteBook(bookView.getIsbn());
            log.info("Successfully deleted book: {}", bookView.getIsbn());
        } catch (Exception e) {
            log.error("Error processing BOOK_DELETED event: {}", e.getMessage(), e);
        }
    }
}

