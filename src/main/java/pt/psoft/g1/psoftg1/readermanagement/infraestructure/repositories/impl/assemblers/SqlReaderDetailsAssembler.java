package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl.assemblers;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.assemblers.SqlGenreAssembler;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.model.SqlDataModels.SqlReaderDetails;

import java.util.List;

@Component
public class SqlReaderDetailsAssembler {

    private final SqlGenreAssembler sqlGenreAssembler;

    public SqlReaderDetailsAssembler(SqlGenreAssembler sqlGenreAssembler) {
        this.sqlGenreAssembler = sqlGenreAssembler;
    }

    public SqlReaderDetails toEntity(ReaderDetails readerDetails) {
        if (readerDetails == null) {
            return null;
        }
        String readerNumber = readerDetails.getReaderNumber();
        String[] parts = readerNumber.split("/");
        int number = Integer.parseInt(parts[1]);
        return new SqlReaderDetails(number,readerDetails.getReader(), readerDetails.getBirthDate().toString(),readerDetails.getPhoneNumber(),
                readerDetails.isGdprConsent(), readerDetails.isMarketingConsent(),readerDetails.isThirdPartySharingConsent(),"",
                sqlGenreAssembler.toEntityList(readerDetails.getInterestList()));
    }

    public ReaderDetails toDomain(SqlReaderDetails readerDetails) {
        if (readerDetails == null) {
            return null;
        }
        String readerNumber = readerDetails.getReaderNumber();
        String[] parts = readerNumber.split("/");
        int number = Integer.parseInt(parts[1]);
        return new ReaderDetails(number,readerDetails.getReader(), readerDetails.getBirthDate().toString(),readerDetails.getPhoneNumber(),
                readerDetails.isGdprConsent(), readerDetails.isMarketingConsent(),readerDetails.isThirdPartySharingConsent(),"",
                sqlGenreAssembler.toDomainList(readerDetails.getInterestList()));
    }

    public List<SqlReaderDetails> toEntityList(List<ReaderDetails> readerDetails) {
        if (readerDetails == null) {
            return null;
        }
        return readerDetails.stream()
                .map(this::toEntity)
                .toList();
    }

    public List<ReaderDetails> toDomainList(List<SqlReaderDetails> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDomain)
                .toList();
    }
}
