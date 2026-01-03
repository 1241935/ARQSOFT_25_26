package pt.psoft.g1.psoftg1.genremanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.genremanagement.model.GenreSync;

import java.util.Optional;

/**
 * Repository for the local synchronized copy of Genres.
 * Used in the Strangler Fig pattern.
 */
@Repository
public interface GenreSyncRepository extends JpaRepository<GenreSync, Long> {

    Optional<GenreSync> findByGenre(String genre);
}

