package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl.assemblers;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.model.SqlDataModels.SqlFine;

import java.util.List;

@Component
public class SqlFineAssembler {

    private final SqlLendingAssembler lendingAssembler;

    private SqlFineAssembler(SqlLendingAssembler lendingAssembler) {
        this.lendingAssembler = lendingAssembler;
    }

    public Fine toDomain(SqlFine sqlFine) {
        if (sqlFine == null) {
            return null;
        }
        return new Fine(lendingAssembler.toDomain(sqlFine.getLending()));
    }

    public SqlFine toEntity(Fine fine) {
        if (fine == null) {
            return null;
        }
        return new SqlFine(lendingAssembler.toEntity(fine.getLending()));
    }

    public List<Fine> toDomainList(List<SqlFine> sqlFines) {
        if (sqlFines == null) {
            return null;
        }
        return sqlFines.stream()
                .map(this::toDomain)
                .toList();
    }

    public List<SqlFine> toEntityList(List<Fine> fines) {
        if (fines == null) {
            return null;
        }
        return fines.stream()
                .map(this::toEntity)
                .toList();
    }
}
