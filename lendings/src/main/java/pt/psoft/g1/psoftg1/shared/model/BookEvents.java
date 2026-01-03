package pt.psoft.g1.psoftg1.shared.model;

/**
 * Constants for Book events received from the Books microservice via RabbitMQ.
 */
public interface BookEvents {
    String BOOK_CREATED = "BOOK_CREATED";
    String BOOK_UPDATED = "BOOK_UPDATED";
    String BOOK_DELETED = "BOOK_DELETED";
}

