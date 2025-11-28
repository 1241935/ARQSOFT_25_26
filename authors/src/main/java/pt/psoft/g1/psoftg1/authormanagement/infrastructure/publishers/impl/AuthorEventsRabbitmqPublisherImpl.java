package pt.psoft.g1.psoftg1.authormanagement.infrastructure.publishers.impl;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorViewAMQP;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorViewAMQPMapper;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.publishers.AuthorEventsPublisher;
import pt.psoft.g1.psoftg1.shared.model.AuthorEvents;

@Service
@RequiredArgsConstructor
public class AuthorEventsRabbitmqPublisherImpl implements AuthorEventsPublisher {

    @Autowired
    private RabbitTemplate template;
    @Autowired
    private DirectExchange direct;
    private final AuthorViewAMQPMapper authorViewAMQPMapper;

    @Override
    public void sendAuthorCreated(Author author) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            AuthorViewAMQP authorViewAMQP = authorViewAMQPMapper.toAuthorViewAMQP(author);
            authorViewAMQP.setVersion(author.getVersion());

            String jsonString = objectMapper.writeValueAsString(authorViewAMQP);

            this.template.convertAndSend(direct.getName(), AuthorEvents.AUTHOR_CREATED, jsonString);

            System.out.println(" [x] Sent author created event: '" + jsonString + "'");
        }
        catch(Exception ex) {
            System.out.println(" [x] Exception sending author event: '" + ex.getMessage() + "'");
        }
    }
} 