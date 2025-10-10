package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.assemblers;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.assemblers.SqlAuthorAssembler;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.SqlDataModels.SqlAuthor;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.SqlDataModels.SqlBook;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SqlBookAssembler {

    private final SqlAuthorAssembler authorAssembler;

    public SqlBookAssembler(SqlAuthorAssembler authorAssembler) {
        this.authorAssembler = authorAssembler;
    }

    // Converter do domínio para Sql (Book → SqlBook)
    public SqlBook toEntity(Book book) {
        if (book == null) {
            return null;
        }

        List<SqlAuthor> sqlAuthors = authorAssembler.toEntityList(book.getAuthors());

        return new SqlBook(
                book.getIsbn(),
                book.getTitle().toString(),
                book.getDescription(),
                book.getGenre(),
                sqlAuthors,
                ""
        );
    }

    // Converter de JPA para domínio (SqlBook → Book)
    public Book toDomain(SqlBook entity) {
        if (entity == null) {
            return null;
        }

        List<Author> authors = authorAssembler.toDomainList(entity.getAuthors());

        return new Book(
                entity.getIsbn(),
                entity.getTitle().toString(),
                entity.getDescription(),
                entity.getGenre(),
                authors,
                ""
        );
    }

    // Converter listas
    public List<SqlBook> toEntityList(List<Book> books) {
        if (books == null) {
            return null;
        }
        return books.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public List<Book> toDomainList(List<SqlBook> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}
