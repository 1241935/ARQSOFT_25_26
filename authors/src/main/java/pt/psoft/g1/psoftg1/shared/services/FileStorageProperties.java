package pt.psoft.g1.psoftg1.shared.services;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;

/**
 * <p>
 * code based on https://github.com/callicoder/spring-boot-file-upload-download-rest-api-example
 *
 *
 */
@ConfigurationProperties(prefix = "file")
@Component
@Data
@Validated
public class FileStorageProperties {

    @NotNull(message = "Upload directory must be configured in application.properties")
    private String uploadDir;
    private long photoMaxSize;
}
