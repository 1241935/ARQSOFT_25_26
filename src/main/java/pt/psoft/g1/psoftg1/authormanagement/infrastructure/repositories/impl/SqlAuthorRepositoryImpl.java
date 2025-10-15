package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.assemblers.SqlAuthorAssembler;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.SqlDataModels.SqlAuthor;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class SqlAuthorRepositoryImpl implements AuthorRepository {

    private final SqlAuthorJpaRepository sqlAuthorJpaRepository;
    private final SqlAuthorAssembler assembler;

    @Override
    public Optional<Author> findByAuthorNumber(Long authorNumber) {
        return Optional.empty();
    }

    @Override
    public List<Author> searchByNameNameStartsWith(String name) {
        return assembler.toDomainList(sqlAuthorJpaRepository.searchByNameNameStartsWith(name));
    }

    @Override
    public List<Author> searchByNameName(String name) {
        return assembler.toDomainList(sqlAuthorJpaRepository.searchByNameName(name));
    }

    @Override
    public Author save(Author author) {
        sqlAuthorJpaRepository.save(assembler.toEntity(author));
        return author;
    }

    @Override
    public Iterable<Author> findAll() {
        return assembler.toDomainList((List<SqlAuthor>) sqlAuthorJpaRepository.findAll());
    }

    @Override
    public Page<AuthorLendingView> findTopAuthorByLendings(Pageable pageableRules) {
        return null;
    }

    @Override
    public void delete(Author author) {
        sqlAuthorJpaRepository.delete(assembler.toEntity(author));
    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(Long authorNumber) {
        return List.of();
    }

}

