package pt.psoft.g1.psoftg1.bootstrapping;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.bookmanagement.model.BookSync;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookSyncRepository;
import pt.psoft.g1.psoftg1.genremanagement.model.GenreSync;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreSyncRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.shared.services.ForbiddenNameService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Bootstrapper for the Lendings microservice.
 *
 * This bootstrapper creates local synchronized copies of Books and Genres
 * (BookSync and GenreSync) that would normally come from the Books microservice
 * via RabbitMQ events. This is for testing/development purposes only.
 *
 * In production, these synced entities would be populated via RabbitMQ events
 * from the Books microservice.
 */
@Component
@RequiredArgsConstructor
@Profile("bootstrap")
@PropertySource({"classpath:config/library.properties"})
@Order(2)
public class Bootstrapper implements CommandLineRunner {
    @Value("${lendingDurationInDays}")
    private int lendingDurationInDays;
    @Value("${fineValuePerDayInCents}")
    private int fineValuePerDayInCents;

    private final GenreSyncRepository genreSyncRepository;
    private final BookSyncRepository bookSyncRepository;
    private final LendingRepository lendingRepository;
    private final ReaderRepository readerRepository;

    private final ForbiddenNameService forbiddenNameService;

    @Override
    @Transactional
    public void run(final String... args) {
        // Create local synchronized copies of genres and books
        // In production, these would come from the Books microservice via RabbitMQ
        createGenresSync();
        createBooksSync();
        loadForbiddenNames();
        createLendings();
        createPhotos();
    }

    /**
     * Creates local synchronized copies of Genres.
     * In production, these would be synced from the Books microservice.
     */
    private void createGenresSync() {
        if (genreSyncRepository.findByGenre("Fantasia").isEmpty()) {
            genreSyncRepository.save(new GenreSync("Fantasia"));
        }
        if (genreSyncRepository.findByGenre("Informação").isEmpty()) {
            genreSyncRepository.save(new GenreSync("Informação"));
        }
        if (genreSyncRepository.findByGenre("Romance").isEmpty()) {
            genreSyncRepository.save(new GenreSync("Romance"));
        }
        if (genreSyncRepository.findByGenre("Infantil").isEmpty()) {
            genreSyncRepository.save(new GenreSync("Infantil"));
        }
        if (genreSyncRepository.findByGenre("Thriller").isEmpty()) {
            genreSyncRepository.save(new GenreSync("Thriller"));
        }
    }

    /**
     * Creates local synchronized copies of Books.
     * In production, these would be synced from the Books microservice via RabbitMQ.
     *
     * Note: BookSync only contains the essential fields needed for lending operations
     * (isbn, title, genre). Author information is not needed in this microservice.
     */
    protected void createBooksSync() {
        // 1 - O País das Pessoas de Pernas Para o Ar
        if (bookSyncRepository.findByIsbn("9789720706386").isEmpty()) {
            BookSync book = new BookSync("9789720706386", "O País das Pessoas de Pernas Para o Ar", "Infantil");
            bookSyncRepository.save(book);
        }

        // 2 - Como se Desenha Uma Casa
        if (bookSyncRepository.findByIsbn("9789723716160").isEmpty()) {
            BookSync book = new BookSync("9789723716160", "Como se Desenha Uma Casa", "Infantil");
            bookSyncRepository.save(book);
        }

        // 3 - C e Algoritmos
        if (bookSyncRepository.findByIsbn("9789895612864").isEmpty()) {
            BookSync book = new BookSync("9789895612864", "C e Algoritmos", "Informação");
            bookSyncRepository.save(book);
        }

        // 4 - Introdução ao Desenvolvimento Moderno para a Web
        if (bookSyncRepository.findByIsbn("9782722203402").isEmpty()) {
            BookSync book = new BookSync("9782722203402", "Introdução ao Desenvolvimento Moderno para a Web", "Informação");
            bookSyncRepository.save(book);
        }

        // 5 - O Principezinho
        if (bookSyncRepository.findByIsbn("9789722328296").isEmpty()) {
            BookSync book = new BookSync("9789722328296", "O Principezinho", "Infantil");
            bookSyncRepository.save(book);
        }

        // 6 - A Criada Está a Ver
        if (bookSyncRepository.findByIsbn("9789895702756").isEmpty()) {
            BookSync book = new BookSync("9789895702756", "A Criada Está a Ver", "Thriller");
            bookSyncRepository.save(book);
        }

        // 7 - O Hobbit
        if (bookSyncRepository.findByIsbn("9789897776090").isEmpty()) {
            BookSync book = new BookSync("9789897776090", "O Hobbit", "Fantasia");
            bookSyncRepository.save(book);
        }

        // 8 - Histórias de Vigaristas e Canalhas
        if (bookSyncRepository.findByIsbn("9789896379636").isEmpty()) {
            BookSync book = new BookSync("9789896379636", "Histórias de Vigaristas e Canalhas", "Fantasia");
            bookSyncRepository.save(book);
        }

        // 9 - Histórias de Aventureiros e Patifes
        if (bookSyncRepository.findByIsbn("9789896378905").isEmpty()) {
            BookSync book = new BookSync("9789896378905", "Histórias de Aventureiros e Patifes", "Fantasia");
            bookSyncRepository.save(book);
        }

        // 10 - Windhaven
        if (bookSyncRepository.findByIsbn("9789896375225").isEmpty()) {
            BookSync book = new BookSync("9789896375225", "Windhaven", "Fantasia");
            bookSyncRepository.save(book);
        }
    }

    protected void loadForbiddenNames() {
        String fileName = "forbiddenNames.txt";
        forbiddenNameService.loadDataFromFile(fileName);
    }

    private void createLendings() {
        int i;
        int seq = 0;

        // Get books from local synchronized copies
        final var book1 = bookSyncRepository.findByIsbn("9789720706386");
        final var book2 = bookSyncRepository.findByIsbn("9789723716160");
        final var book3 = bookSyncRepository.findByIsbn("9789895612864");
        final var book4 = bookSyncRepository.findByIsbn("9782722203402");
        final var book5 = bookSyncRepository.findByIsbn("9789722328296");
        final var book6 = bookSyncRepository.findByIsbn("9789895702756");
        final var book7 = bookSyncRepository.findByIsbn("9789897776090");
        final var book8 = bookSyncRepository.findByIsbn("9789896379636");
        final var book9 = bookSyncRepository.findByIsbn("9789896378905");
        final var book10 = bookSyncRepository.findByIsbn("9789896375225");

        List<BookSync> books = new ArrayList<>();
        if (book1.isPresent() && book2.isPresent()
                && book3.isPresent() && book4.isPresent()
                && book5.isPresent() && book6.isPresent()
                && book7.isPresent() && book8.isPresent()
                && book9.isPresent() && book10.isPresent()) {
            books = List.of(book1.get(), book2.get(), book3.get(),
                    book4.get(), book5.get(), book6.get(), book7.get(),
                    book8.get(), book9.get(), book10.get());
        }

        final var readerDetails1 = readerRepository.findByReaderNumber("2024/1");
        final var readerDetails2 = readerRepository.findByReaderNumber("2024/2");
        final var readerDetails3 = readerRepository.findByReaderNumber("2024/3");
        final var readerDetails4 = readerRepository.findByReaderNumber("2024/4");
        final var readerDetails5 = readerRepository.findByReaderNumber("2024/5");
        final var readerDetails6 = readerRepository.findByReaderNumber("2024/6");

        List<ReaderDetails> readers = new ArrayList<>();
        if (readerDetails1.isPresent() && readerDetails2.isPresent() && readerDetails3.isPresent()
                && readerDetails4.isPresent() && readerDetails5.isPresent() && readerDetails6.isPresent()) {
            readers = List.of(readerDetails1.get(), readerDetails2.get(), readerDetails3.get(),
                    readerDetails4.get(), readerDetails5.get(), readerDetails6.get());
        }

        LocalDate startDate;
        LocalDate returnedDate;
        Lending lending;

        // Only create lendings if we have books and readers
        if (books.isEmpty() || readers.isEmpty()) {
            return;
        }

        // Lendings 1 through 3 (late, returned)
        for (i = 0; i < 3; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 1, 31 - i);
                returnedDate = LocalDate.of(2024, 2, 15 + i);
                lending = Lending.newBootstrappingLending(books.get(i), readers.get(i % readers.size()), 2024, seq, startDate, returnedDate, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        // Lendings 4 through 6 (overdue, not returned)
        for (i = 0; i < 3; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 3, 25 + i);
                lending = Lending.newBootstrappingLending(books.get(1 + i), readers.get((1 + i) % readers.size()), 2024, seq, startDate, null, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        // Lendings 7 through 9 (late, overdue, not returned)
        for (i = 0; i < 3; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 4, (1 + 2 * i));
                lending = Lending.newBootstrappingLending(books.get(3 / (i + 1)), readers.get(i % readers.size()), 2024, seq, startDate, null, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        // Lendings 10 through 12 (returned)
        for (i = 0; i < 3; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 5, (i + 1));
                returnedDate = LocalDate.of(2024, 5, (i + 2));
                lending = Lending.newBootstrappingLending(books.get(3 - i), readers.get((1 + i) % readers.size()), 2024, seq, startDate, returnedDate, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        // Lendings 13 through 18 (returned)
        for (i = 0; i < 6; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 5, (i + 2));
                returnedDate = LocalDate.of(2024, 5, (i + 2 * 2));
                lending = Lending.newBootstrappingLending(books.get(i % books.size()), readers.get(i % readers.size()), 2024, seq, startDate, returnedDate, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        // Lendings 19 through 24 (returned)
        for (i = 0; i < 6; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 5, (i + 8));
                returnedDate = LocalDate.of(2024, 5, (2 * i + 8));
                lending = Lending.newBootstrappingLending(books.get(i % books.size()), readers.get((1 + i % 4) % readers.size()), 2024, seq, startDate, returnedDate, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        // Lendings 25 through 30 (returned)
        for (i = 0; i < 6; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 5, (i + 18));
                returnedDate = LocalDate.of(2024, 5, (2 * i + 18));
                lending = Lending.newBootstrappingLending(books.get(i % books.size()), readers.get((i % 2 + 2) % readers.size()), 2024, seq, startDate, returnedDate, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        // Lendings 31 through 36 (not returned, not overdue)
        for (i = 0; i < 6; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 6, (i / 3 + 1));
                lending = Lending.newBootstrappingLending(books.get(i % books.size()), readers.get((i % 2 + 3) % readers.size()), 2024, seq, startDate, null, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        // Lendings 37 through 46 (not returned, not overdue)
        for (i = 0; i < 10; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 6, (2 + i / 4));
                lending = Lending.newBootstrappingLending(books.get(i % books.size()), readers.get((4 - i % 4) % readers.size()), 2024, seq, startDate, null, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }
    }

    private void createPhotos() {
        // Photos are handled by the Readers management
    }
}


