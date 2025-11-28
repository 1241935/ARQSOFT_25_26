package pt.psoft.g1.psoftg1.authormanagement.services;

import java.util.List;
import java.util.Optional;

import pt.psoft.g1.psoftg1.authormanagement.api.AuthorViewAMQP;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;

public interface AuthorService {

    Iterable<Author> findAll();

    Optional<Author> findByAuthorNumber(Long authorNumber);

    List<Author> findByName(String name);

    Author create(CreateAuthorRequest resource);

    Author create(AuthorViewAMQP authorViewAMQP);

    Author partialUpdate(Long authorNumber, UpdateAuthorRequest resource, long desiredVersion);

    List<Book> findBooksByAuthorNumber(Long authorNumber);

    List<Author> findCoAuthorsByAuthorNumber(Long authorNumber);

    Optional<Author> removeAuthorPhoto(Long authorNumber, long desiredVersion);
}
