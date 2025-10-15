package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.authormanagement.model.SqlDataModels.SqlAuthor;

import java.util.List;
import java.util.Optional;

@Repository
public interface SqlAuthorJpaRepository extends JpaRepository<SqlAuthor, Long> {

    Optional<SqlAuthor> findByAuthorNumber(Long authorNumber);

    /*@Query("SELECT new pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView(a.name.name, COUNT(l.pk)) " +
            "FROM Book b " +
            "JOIN b.authors a " +
            "JOIN Lending l ON l.book.pk = b.pk " +
            "GROUP BY a.name " +
            "ORDER BY COUNT(l) DESC")
    Page<AuthorLendingView> findTopAuthorByLendings(Pageable pageable);*/

    /*@Query("SELECT DISTINCT coAuthor FROM Book b " +
            "JOIN b.authors coAuthor " +
            "WHERE b IN (SELECT b FROM Book b JOIN b.authors a WHERE a.authorNumber = :authorNumber) " +
            "AND coAuthor.authorNumber <> :authorNumber")
    List<Author> findCoAuthorsByAuthorNumber(Long authorNumber);*/

    @Query("SELECT a " +
            "FROM SqlAuthor a " +
            "WHERE a.name.name = :name")
    List<SqlAuthor> searchByNameName(String name);

    @Query("SELECT a " +
            "FROM SqlAuthor a " +
            "WHERE a.name.name LIKE :name%")
    List<SqlAuthor> searchByNameNameStartsWith(String name);

}
