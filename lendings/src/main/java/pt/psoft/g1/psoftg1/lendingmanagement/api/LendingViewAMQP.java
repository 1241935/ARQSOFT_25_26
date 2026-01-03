package pt.psoft.g1.psoftg1.lendingmanagement.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * DTO for publishing Lending events to other microservices via RabbitMQ.
 */
@Data
@NoArgsConstructor
public class LendingViewAMQP {

    private String lendingNumber;
    private String isbn;
    private String readerNumber;
    private String startDate;
    private String limitDate;
    private String returnedDate;
    private Long version;
    private Map<String, Object> _links = new HashMap<>();

    public LendingViewAMQP(String lendingNumber, String isbn, String readerNumber,
                           LocalDate startDate, LocalDate limitDate, LocalDate returnedDate) {
        this.lendingNumber = lendingNumber;
        this.isbn = isbn;
        this.readerNumber = readerNumber;
        this.startDate = startDate != null ? startDate.toString() : null;
        this.limitDate = limitDate != null ? limitDate.toString() : null;
        this.returnedDate = returnedDate != null ? returnedDate.toString() : null;
    }
}

