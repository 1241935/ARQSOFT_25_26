package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.assemblers.SqlAuthorAssembler;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.SqlDataModels.SqlAuthor;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class SqlAuthorRepositoryImpl implements AuthorRepository {

    private final SqlAuthorRepository sqlAuthorRepository;
    private final SqlAuthorAssembler assembler;

    @Override
    public Optional<Author> findByAuthorNumber(Long authorNumber) {
        return Optional.empty();
    }

    @Override
    public List<Author> searchByNameNameStartsWith(String name) {
        return assembler.toDomainList(sqlAuthorRepository.searchByNameNameStartsWith(name));
    }

    @Override
    public List<Author> searchByNameName(String name) {
        return assembler.toDomainList(sqlAuthorRepository.searchByNameName(name));
    }

    @Override
    public Author save(Author author) {
        sqlAuthorRepository.save(assembler.toEntity(author));
        return author;
    }

    @Override
    public Iterable<Author> findAll() {
        return assembler.toDomainList((List<SqlAuthor>) sqlAuthorRepository.findAll());
    }

    @Override
    public Page<AuthorLendingView> findTopAuthorByLendings(Pageable pageableRules) {
        return null;
    }

    @Override
    public void delete(Author author) {
        sqlAuthorRepository.delete(assembler.toEntity(author));
    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(Long authorNumber) {
        return List.of();
    }

}

interface SqlAuthorRepository extends CrudRepository<SqlAuthor, Long> {

    Optional<SqlAuthor> findByAuthorNumber(Long authorNumber);

    /*@Query("SELECT new pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView(a.name.name, COUNT(l.pk)) " +
            "FROM Book b " +
            "JOIN b.authors a " +
            "JOIN Lending l ON l.book.pk = b.pk " +
            "GROUP BY a.name " +
            "ORDER BY COUNT(l) DESC")
    Page<AuthorLendingView> findTopAuthorByLendings(Pageable pageable);*/

    /*@Query("SELECT DISTINCT coAuthor FROM Book b " +
            "JOIN b.authors coAuthor " +
            "WHERE b IN (SELECT b FROM Book b JOIN b.authors a WHERE a.authorNumber = :authorNumber) " +
            "AND coAuthor.authorNumber <> :authorNumber")
    List<Author> findCoAuthorsByAuthorNumber(Long authorNumber);*/

    @Query("SELECT a " +
            "FROM SqlAuthor a " +
            "WHERE a.name.name = :name")
    List<SqlAuthor> searchByNameName(String name);

    @Query("SELECT a " +
            "FROM SqlAuthor a " +
            "WHERE a.name.name LIKE :name%")
    List<SqlAuthor> searchByNameNameStartsWith(String name);

}

