package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@Configuration
public class RabbitmqClientConfig {

    @Bean
    public DirectExchange authorsExchange() {
        return new DirectExchange("LMS.authors");
    }

    @Bean(name = "autoDeleteQueue_Author_Created")
    public Queue autoDeleteQueue_Author_Created() {
        return new Queue("autoDeleteQueue_Author_Created", false, false, true);
    }

    @Bean
    public Binding binding1(DirectExchange authorsExchange, Queue autoDeleteQueue_Author_Created) {
        return BindingBuilder.bind(autoDeleteQueue_Author_Created)
                .to(authorsExchange)
                .with("author.created");
    }
} 