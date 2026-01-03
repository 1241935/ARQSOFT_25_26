package pt.psoft.g1.psoftg1.bookmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.bookmanagement.model.BookSync;

import java.util.Optional;

/**
 * Repository for the local synchronized copy of Books.
 * Used in the Strangler Fig pattern.
 */
@Repository
public interface BookSyncRepository extends JpaRepository<BookSync, Long> {

    Optional<BookSync> findByIsbn(String isbn);

    void deleteByIsbn(String isbn);
}

