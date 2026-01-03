package pt.psoft.g1.psoftg1.genremanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.GenreSync;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreSyncRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for Genre operations in the Lendings microservice.
 * Works with GenreSync - synchronized copies from the Books microservice.
 */
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreSyncRepository genreSyncRepository;
    private final LendingRepository lendingRepository;

    @Override
    public Optional<GenreSync> findByGenre(String name) {
        return genreSyncRepository.findByGenre(name);
    }

    @Override
    public Iterable<GenreSync> findAll() {
        return genreSyncRepository.findAll();
    }

    @Override
    public GenreSync save(GenreSync genre) {
        return this.genreSyncRepository.save(genre);
    }

    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsPerMonthLastYearByGenre() {
        return lendingRepository.getLendingsPerMonthLastYearByGenre();
    }

    @Override
    public List<GenreLendingsDTO> getAverageLendings(GetAverageLendingsQuery query, Page page){
        if (page == null)
            page = new Page(1, 10);

        final var month = LocalDate.of(query.getYear(), query.getMonth(), 1);

        return lendingRepository.getAverageLendingsInMonth(month, page);
    }

    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsAverageDurationPerMonth(String start, String end){
        LocalDate startDate;
        LocalDate endDate;

        try {
            startDate = LocalDate.parse(start);
            endDate = LocalDate.parse(end);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Expected format is YYYY-MM-DD");
        }

        if(startDate.isAfter(endDate))
            throw new IllegalArgumentException("Start date cannot be after end date");

        final var list = lendingRepository.getLendingsAverageDurationPerMonth(startDate, endDate);

        if (list.isEmpty())
            throw new NotFoundException("No objects match the provided criteria");

        return list;
    }
}
