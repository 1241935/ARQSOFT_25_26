package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.assemblers;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.SqlDataModels.SqlAuthor;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SqlAuthorAssembler {

    // Convert from domain model (Author) to Sql entity (SqlAuthor)
    public SqlAuthor toEntity(Author author) {
        if (author == null) {
            return null;
        }
        return new SqlAuthor(
                author.getName(),
                author.getBio(),
                "photo"
        );
    }

    // Convert from JPA entity (SqlAuthor) to domain model (Author)
    public Author toDomain(SqlAuthor entity) {
        if (entity == null) {
            return null;
        }
        return new Author(
                entity.getName(),
                entity.getBio(),
                "photo"
        );
    }

    // List conversions
    public List<SqlAuthor> toEntityList(List<Author> authors) {
        if (authors == null) {
            return null;
        }
        return authors.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public List<Author> toDomainList(List<SqlAuthor> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}
