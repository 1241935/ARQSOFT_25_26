package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.publishers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.publishers.LendingEventsPublisher;
import pt.psoft.g1.psoftg1.shared.model.LendingEvents;

/**
 * RabbitMQ implementation of LendingEventsPublisher.
 * Publishes lending events to the message broker for other microservices to consume.
 */
@Profile("!test")
@Service
@RequiredArgsConstructor
@Slf4j
public class LendingEventsRabbitmqPublisherImpl implements LendingEventsPublisher {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    @Qualifier("lendingsExchange")
    private DirectExchange lendingsExchange;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public LendingViewAMQP sendLendingCreated(Lending lending) {
        return sendLendingEvent(lending, 1L, LendingEvents.LENDING_CREATED);
    }

    @Override
    public LendingViewAMQP sendLendingReturned(Lending lending, Long currentVersion) {
        return sendLendingEvent(lending, currentVersion, LendingEvents.LENDING_RETURNED);
    }

    private LendingViewAMQP sendLendingEvent(Lending lending, Long currentVersion, String eventType) {
        log.info("Sending Lending event to AMQP Broker: {} - {}", eventType, lending.getLendingNumber());

        try {
            LendingViewAMQP lendingViewAMQP = new LendingViewAMQP(
                    lending.getLendingNumber(),
                    lending.getBook().getIsbn(),
                    lending.getReaderDetails().getReaderNumber(),
                    lending.getStartDate(),
                    lending.getLimitDate(),
                    lending.getReturnedDate()
            );
            lendingViewAMQP.setVersion(currentVersion);

            String lendingViewAMQPinString = objectMapper.writeValueAsString(lendingViewAMQP);

            this.template.convertAndSend(lendingsExchange.getName(), eventType, lendingViewAMQPinString);

            log.info("Successfully sent {} event for lending: {}", eventType, lending.getLendingNumber());
            return lendingViewAMQP;
        } catch (Exception ex) {
            log.error("Exception sending lending event: '{}'", ex.getMessage(), ex);
            return null;
        }
    }
}

