package pt.psoft.g1.psoftg1.genremanagement.services;

import pt.psoft.g1.psoftg1.genremanagement.model.GenreSync;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.List;
import java.util.Optional;

/**
 * Service for Genre operations in the Lendings microservice.
 * Works with GenreSync - synchronized copies from the Books microservice.
 */
public interface GenreService {
    Iterable<GenreSync> findAll();
    GenreSync save(GenreSync genre);
    Optional<GenreSync> findByGenre(String name);
    List<GenreLendingsDTO> getAverageLendings(GetAverageLendingsQuery query, Page page);
    List<GenreLendingsPerMonthDTO> getLendingsPerMonthLastYearByGenre();
    List<GenreLendingsPerMonthDTO> getLendingsAverageDurationPerMonth(String startDate, String endDate);
}
