package pt.psoft.g1.psoftg1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import pt.psoft.g1.psoftg1.shared.services.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties(FileStorageProperties.class)
public class LMSBooks {

    public static void main(String[] args) {
        SpringApplication.run(LMSBooks.class, args);
    }

}
