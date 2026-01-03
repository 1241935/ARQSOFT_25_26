package pt.psoft.g1.psoftg1.shared.model;

/**
 * Constants for Lending events published by this microservice via RabbitMQ.
 */
public interface LendingEvents {
    String LENDING_CREATED = "LENDING_CREATED";
    String LENDING_RETURNED = "LENDING_RETURNED";
    String LENDING_OVERDUE = "LENDING_OVERDUE";
}

