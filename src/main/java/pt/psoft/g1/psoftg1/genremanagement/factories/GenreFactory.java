package pt.psoft.g1.psoftg1.genremanagement.factories;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.services.IdGenerator;

@Component
public class GenreFactory {

    private final IdGenerator idGenerator;

    public GenreFactory(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public Genre create(String genre) {
        String pk = idGenerator.generateId();
        return new Genre(pk, genre);
    }

}
