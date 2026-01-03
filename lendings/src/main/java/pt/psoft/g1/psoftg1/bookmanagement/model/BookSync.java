package pt.psoft.g1.psoftg1.bookmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Local representation of a Book from the Books microservice.
 * This is a synchronized copy - the source of truth is in the Books microservice.
 * Used to decouple the Lendings microservice (Strangler Fig pattern).
 *
 * Only contains the essential data needed for lending operations.
 */
@Entity
@Table(name = "BOOK_SYNC")
public class BookSync {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;

    @NotNull
    @Column(unique = true, nullable = false)
    @Getter
    private String isbn;

    @NotNull
    @Column(nullable = false)
    @Getter
    private String title;

    @Getter
    @Setter
    private String genre;

    @Version
    @Getter
    @Setter
    private Long version;

    protected BookSync() {
        // for ORM only
    }

    public BookSync(String isbn, String title, String genre) {
        setIsbn(isbn);
        setTitle(title);
        setGenre(genre);
    }

    private void setIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN cannot be null or blank");
        }
        this.isbn = isbn;
    }

    private void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }
        this.title = title;
    }

    /**
     * Updates the local copy with data from the Books microservice.
     */
    public void updateFromSync(String title, String genre, Long version) {
        this.title = title;
        this.genre = genre;
        this.version = version;
    }

    @Override
    public String toString() {
        return title;
    }
}

