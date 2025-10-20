package pt.psoft.g1.psoftg1.bookmanagement.factories;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.services.IdGenerator;

import java.util.List;

@Component
public class BookFactory {

    private final IdGenerator idGenerator;

    public BookFactory(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public Book create(String isbn, String title, String description, Genre genre, List<Author> authors, String photoURI) {
        String pk = idGenerator.generateId();
        return new Book(pk, isbn, title, description, genre, authors, photoURI);
    }
}
