package pt.psoft.g1.psoftg1.authormanagement.api;

import java.nio.charset.StandardCharsets;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;

@Component
@RequiredArgsConstructor
public class AuthorEventRabbitmqReceiver {

    private final AuthorService authorService;

    @RabbitListener(queues = "autoDeleteQueue_Author_Created")
    public void receiveAuthorCreated(Message msg) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            AuthorViewAMQP authorViewAMQP = objectMapper.readValue(jsonReceived, AuthorViewAMQP.class);

            System.out.println(" [x] Received Author Created by AMQP: " + msg + ".");
            try {
                authorService.create(authorViewAMQP);
                System.out.println(" [x] Author Created by AMQP: " + authorViewAMQP.getName() + ".");
            } catch (Exception e) {
                System.out.println(" [x] Error creating author by AMQP: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(" [x] Error processing AMQP message: " + e.getMessage());
        }
    }
} 