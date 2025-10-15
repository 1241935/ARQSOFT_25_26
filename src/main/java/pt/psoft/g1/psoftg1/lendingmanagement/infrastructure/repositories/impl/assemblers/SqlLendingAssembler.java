package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl.assemblers;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.assemblers.SqlBookAssembler;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.SqlDataModels.SqlBook;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.model.SqlDataModels.SqlLending;
import pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl.assemblers.SqlReaderDetailsAssembler;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class SqlLendingAssembler {

    private final SqlBookAssembler bookAssembler;
    private final SqlReaderDetailsAssembler readerDetailsAssembler;

    public SqlLendingAssembler(SqlBookAssembler bookAssembler, SqlReaderDetailsAssembler readerDetailsAssembler) {
        this.bookAssembler = bookAssembler;
        this.readerDetailsAssembler = readerDetailsAssembler;
    }

    /**
     * Converte do domínio para entidade JPA
     */
    public SqlLending toEntity(Lending lending) {
        if(Objects.isNull(lending)) return null;

        String[] parts = lending.getLendingNumber().split("/");
        int number = Integer.parseInt(parts[0]);
        int duration = (int) ChronoUnit.DAYS.between(lending.getLimitDate(), lending.getStartDate());

        return new SqlLending(bookAssembler.toEntity(lending.getBook()),readerDetailsAssembler.toEntity(lending.getReaderDetails()),number,duration,lending.getFineValuePerDayInCents());
    }

    /**
     * Converte de entidade JPA para modelo de domínio
     */
    public Lending toDomain(SqlLending sqlLending) {
        if (Objects.isNull(sqlLending)) return null;

        String[] parts = sqlLending.getLendingNumber().split("/");
        int number = Integer.parseInt(parts[0]);
        int duration = (int) ChronoUnit.DAYS.between(sqlLending.getLimitDate(), sqlLending.getStartDate());

        return new Lending(bookAssembler.toDomain(sqlLending.getBook()),readerDetailsAssembler.toDomain(sqlLending.getReaderDetails()),number,duration,sqlLending.getFineValuePerDayInCents());
    }

    public List<SqlLending> toEntityList(List<Lending> lendings) {
        if (lendings == null) return null;
        return lendings.stream().map(this::toEntity).collect(Collectors.toList());
    }

    public List<Lending> toDomainList(List<SqlLending> entities) {
        if (entities == null) return null;
        return entities.stream().map(this::toDomain).collect(Collectors.toList());
    }

}
