package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.assemblers;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.model.SqlDataModels.SqlGenre;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SqlGenreAssembler {

    public SqlGenre toEntity(Genre genre) {
        if (genre == null) {
            return null;
        }
        return new SqlGenre(
                genre.getGenre()
        );
    }

    public Genre toDomain(SqlGenre sqlGenre) {
        if (sqlGenre == null) {
            return null;
        }
        return new Genre(
                sqlGenre.getGenre()
        );
    }

    public List<SqlGenre> toEntityList(List<Genre> genres) {
        if (genres == null) {
            return new ArrayList<>();  // or return null, depending on your preference
        }
        return genres.stream().map(this::toEntity).collect(Collectors.toList());
    }

    public List<Genre> toDomainList(List<SqlGenre> genres) {
        if (genres == null) {
            return new ArrayList<>();  // or return null, depending on your preference
        }
        return genres.stream().map(this::toDomain).collect(Collectors.toList());
    }
}
