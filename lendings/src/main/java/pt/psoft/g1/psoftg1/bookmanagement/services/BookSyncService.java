package pt.psoft.g1.psoftg1.bookmanagement.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.model.BookSync;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookSyncRepository;
import pt.psoft.g1.psoftg1.genremanagement.model.GenreSync;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreSyncRepository;

import java.util.Optional;

/**
 * Service for synchronizing Book data from the Books microservice.
 * Implements the Strangler Fig pattern - maintains a local copy of essential book data.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookSyncService {

    private final BookSyncRepository bookSyncRepository;
    private final GenreSyncRepository genreSyncRepository;

    /**
     * Creates or updates a local copy of a book from a RabbitMQ event.
     */
    @Transactional
    public BookSync createOrUpdateBook(BookViewAMQP bookView) {
        // Ensure genre exists locally
        ensureGenreExists(bookView.getGenre());

        Optional<BookSync> existingBook = bookSyncRepository.findByIsbn(bookView.getIsbn());

        if (existingBook.isPresent()) {
            BookSync book = existingBook.get();
            book.updateFromSync(bookView.getTitle(), bookView.getGenre(), bookView.getVersion());
            return bookSyncRepository.save(book);
        } else {
            BookSync newBook = new BookSync(
                    bookView.getIsbn(),
                    bookView.getTitle(),
                    bookView.getGenre()
            );
            newBook.setVersion(bookView.getVersion());
            return bookSyncRepository.save(newBook);
        }
    }

    /**
     * Deletes the local copy of a book.
     */
    @Transactional
    public void deleteBook(String isbn) {
        bookSyncRepository.findByIsbn(isbn).ifPresent(book -> {
            log.info("Deleting local book copy: {}", isbn);
            bookSyncRepository.delete(book);
        });
    }

    /**
     * Ensures a genre exists locally for Reader interests.
     */
    private void ensureGenreExists(String genreName) {
        if (genreName == null || genreName.isBlank()) {
            return;
        }

        Optional<GenreSync> existingGenre = genreSyncRepository.findByGenre(genreName);
        if (existingGenre.isEmpty()) {
            GenreSync genre = new GenreSync(genreName);
            genreSyncRepository.save(genre);
            log.info("Created local genre copy: {}", genreName);
        }
    }

    /**
     * Finds a book by ISBN from the local synchronized copy.
     */
    public Optional<BookSync> findByIsbn(String isbn) {
        return bookSyncRepository.findByIsbn(isbn);
    }
}

