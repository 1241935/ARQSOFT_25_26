package pt.psoft.g1.psoftg1.genremanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Local representation of a Genre from the Books microservice.
 * This is a synchronized copy - the source of truth is in the Books microservice.
 * Used to decouple the Lendings microservice (Strangler Fig pattern).
 */
@Entity
@Table(name = "GENRE_SYNC")
public class GenreSync {

    private static final int GENRE_MAX_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;

    @Size(min = 1, max = GENRE_MAX_LENGTH, message = "Genre name must be between 1 and 100 characters")
    @Column(unique = true, nullable = false, length = GENRE_MAX_LENGTH)
    @Getter
    private String genre;

    @Version
    @Getter
    @Setter
    private Long version;

    protected GenreSync() {
        // for ORM only
    }

    public GenreSync(String genre) {
        setGenre(genre);
    }

    private void setGenre(String genre) {
        if (genre == null)
            throw new IllegalArgumentException("Genre cannot be null");
        if (genre.isBlank())
            throw new IllegalArgumentException("Genre cannot be blank");
        if (genre.length() > GENRE_MAX_LENGTH)
            throw new IllegalArgumentException("Genre has a maximum of " + GENRE_MAX_LENGTH + " characters");
        this.genre = genre;
    }

    /**
     * Updates the local copy with data from the Books microservice.
     */
    public void updateFromSync(String genre, Long version) {
        setGenre(genre);
        this.version = version;
    }

    @Override
    public String toString() {
        return genre;
    }
}

