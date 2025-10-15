package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl.assemblers.SqlFineAssembler;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.model.SqlDataModels.SqlFine;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.FineRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SqlFineRepositoryImpl implements FineRepository {

    private  final SqlFineJpaRepository sqlFineRepository;
    private final SqlFineAssembler sqlFineAssembler;

    @Override
    public Optional<Fine> findByLendingNumber(String lendingNumber) {
        return sqlFineRepository.findByLendingNumber(lendingNumber);
    }

    @Override
    public Iterable<Fine> findAll() {
        return sqlFineAssembler.toDomainList(sqlFineRepository.findAll());
    }

    @Override
    public Fine save(Fine fine) {
        sqlFineRepository.save(sqlFineAssembler.toEntity(fine));
        return  fine;
    }
}

interface SqlFineJpaRepository extends JpaRepository<SqlFine, Integer> {

    @Query("SELECT f " +
            "FROM SqlFine f " +
            "JOIN SqlLending l ON f.lending.pk = l.pk " +
            "WHERE l.lendingNumber.lendingNumber = :lendingNumber")
    Optional<Fine> findByLendingNumber(String lendingNumber);

}