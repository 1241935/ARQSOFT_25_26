package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.assemblers.SqlGenreAssembler;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.model.SqlDataModels.SqlGenre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsPerMonthDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SqlGenreRepositoryImpl implements GenreRepository {


    private final SqlGenreJpaRepository sqlGenreJpaRepository;
    private final SqlGenreAssembler assembler;

    @Override
    public Iterable<Genre> findAll() {
        return null;
    }

    @Override
    public Optional<Genre> findByString(String genreName) {
        return sqlGenreJpaRepository.findByString(genreName)
                .map(assembler::toDomain);
    }

    @Override
    public Genre save(Genre genre) {
        sqlGenreJpaRepository.save(assembler.toEntity(genre));
        return genre;
    }

    @Override
    public Page<GenreBookCountDTO> findTop5GenreByBookCount(Pageable pageable) {
        return null;
    }

    @Override
    public List<GenreLendingsDTO> getAverageLendingsInMonth(LocalDate month, pt.psoft.g1.psoftg1.shared.services.Page page) {
        return List.of();
    }

    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsPerMonthLastYearByGenre() {
        return List.of();
    }

    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsAverageDurationPerMonth(LocalDate startDate, LocalDate endDate) {
        return List.of();
    }

    @Override
    public void delete(Genre genre) {
        sqlGenreJpaRepository.delete(assembler.toEntity(genre));
    }
}

interface SqlGenreJpaRepository extends JpaRepository<SqlGenre, Long> {

    @Query("SELECT g FROM SqlGenre g WHERE g.genre = :genreName" )
    Optional<SqlGenre> findByString(@Param("genreName")@NotNull String genre);
}
