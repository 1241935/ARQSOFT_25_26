package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.assemblers.SqlAuthorAssembler;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.assemblers.SqlBookAssembler;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.SqlDataModels.SqlBook;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SqlBookRepositoryImpl implements BookRepository {

    private final SqlBookRepository sqlBookRepository;
    private final SqlBookAssembler bookJpaAssembler;
    private final EntityManager entityManager;
    private final SqlAuthorAssembler authorJpaAssembler;


    @Override
    public List<Book> findByGenre(String genre) {
        return List.of();
    }

    @Override
    public List<Book> findByTitle(String title) {
        return List.of();
    }

    @Override
    public List<Book> findByAuthorName(String authorName) {
        return List.of();
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return Optional.empty();
    }

    @Override
    public Page<BookCountDTO> findTop5BooksLent(LocalDate oneYearAgo, Pageable pageable) {
        return null;
    }

    @Override
    public List<Book> findBooksByAuthorNumber(Long authorNumber) {
        return List.of();
    }

    @Override
    public List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page page, SearchBooksQuery query) {
        return List.of();
    }

    @Override
    public Book save(Book book) {
        sqlBookRepository.save(bookJpaAssembler.toEntity(book));
        return book;
    }

    @Override
    public void delete(Book book) {
        sqlBookRepository.delete(bookJpaAssembler.toEntity(book));
    }
}

interface SqlBookRepository extends JpaRepository<SqlBook, Long> {}
