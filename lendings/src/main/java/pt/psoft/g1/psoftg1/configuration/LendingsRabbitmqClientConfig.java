package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.shared.model.BookEvents;
import pt.psoft.g1.psoftg1.shared.model.LendingEvents;

/**
 * RabbitMQ configuration for the Lendings microservice.
 *
 * This microservice:
 * - SUBSCRIBES to events from Books microservice (book.created, book.updated, book.deleted)
 * - PUBLISHES events for Lending operations (lending.created, lending.returned)
 */
@Profile("!test")
@Configuration
public class LendingsRabbitmqClientConfig {

    // ==================== EXCHANGES ====================

    @Bean
    public DirectExchange lendingsExchange() {
        return new DirectExchange("LMS.lendings");
    }

    @Bean
    public DirectExchange booksExchange() {
        return new DirectExchange("LMS.books");
    }

    // ==================== MESSAGE CONVERTER ====================

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    // ==================== QUEUES FOR RECEIVING BOOK EVENTS ====================

    @Configuration
    static class BookSubscriberConfig {

        @Bean(name = "bookCreatedQueue")
        public Queue bookCreatedQueue() {
            return QueueBuilder.durable("lendings.book.created").build();
        }

        @Bean(name = "bookUpdatedQueue")
        public Queue bookUpdatedQueue() {
            return QueueBuilder.durable("lendings.book.updated").build();
        }

        @Bean(name = "bookDeletedQueue")
        public Queue bookDeletedQueue() {
            return QueueBuilder.durable("lendings.book.deleted").build();
        }

        @Bean
        public Binding bindingBookCreated(
                @Qualifier("booksExchange") DirectExchange booksExchange,
                @Qualifier("bookCreatedQueue") Queue bookCreatedQueue) {
            return BindingBuilder.bind(bookCreatedQueue)
                    .to(booksExchange)
                    .with(BookEvents.BOOK_CREATED);
        }

        @Bean
        public Binding bindingBookUpdated(
                @Qualifier("booksExchange") DirectExchange booksExchange,
                @Qualifier("bookUpdatedQueue") Queue bookUpdatedQueue) {
            return BindingBuilder.bind(bookUpdatedQueue)
                    .to(booksExchange)
                    .with(BookEvents.BOOK_UPDATED);
        }

        @Bean
        public Binding bindingBookDeleted(
                @Qualifier("booksExchange") DirectExchange booksExchange,
                @Qualifier("bookDeletedQueue") Queue bookDeletedQueue) {
            return BindingBuilder.bind(bookDeletedQueue)
                    .to(booksExchange)
                    .with(BookEvents.BOOK_DELETED);
        }
    }

    // ==================== QUEUES FOR PUBLISHING LENDING EVENTS ====================

    @Configuration
    static class LendingPublisherConfig {

        @Bean(name = "lendingCreatedQueue")
        public Queue lendingCreatedQueue() {
            return QueueBuilder.durable("lendings.lending.created").build();
        }

        @Bean(name = "lendingReturnedQueue")
        public Queue lendingReturnedQueue() {
            return QueueBuilder.durable("lendings.lending.returned").build();
        }

        @Bean
        public Binding bindingLendingCreated(
                @Qualifier("lendingsExchange") DirectExchange lendingsExchange,
                @Qualifier("lendingCreatedQueue") Queue lendingCreatedQueue) {
            return BindingBuilder.bind(lendingCreatedQueue)
                    .to(lendingsExchange)
                    .with(LendingEvents.LENDING_CREATED);
        }

        @Bean
        public Binding bindingLendingReturned(
                @Qualifier("lendingsExchange") DirectExchange lendingsExchange,
                @Qualifier("lendingReturnedQueue") Queue lendingReturnedQueue) {
            return BindingBuilder.bind(lendingReturnedQueue)
                    .to(lendingsExchange)
                    .with(LendingEvents.LENDING_RETURNED);
        }
    }
}

