package pt.psoft.g1.psoftg1.bookmanagement.model.SqlDataModels;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.StaleObjectStateException;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.SqlDataModels.SqlAuthor;
import pt.psoft.g1.psoftg1.bookmanagement.model.Description;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.model.Title;
import pt.psoft.g1.psoftg1.bookmanagement.services.UpdateBookRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.model.SqlDataModels.SqlGenre;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "SqlBook", uniqueConstraints = {
        @UniqueConstraint(name = "uc_book_isbn", columnNames = {"ISBN"})
})
public class SqlBook extends EntityWithPhoto {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    long pk;

    @Version
    @Getter
    private Long version;

    @Embedded
    SqlIsbn isbn;

    @Getter
    @Embedded
    @NotNull
    SqlTitle title;

    @Getter
    @ManyToOne
    @NotNull
    SqlGenre genre;

    @Getter
    @ManyToMany
    private List<SqlAuthor> authors = new ArrayList<>();

    @Embedded
    Description description;

    private void setTitle(String title) {this.title = new SqlTitle(title);}

    private void setIsbn(String isbn) {
        this.isbn = new SqlIsbn(isbn);
    }

    private void setDescription(String description) {this.description = new Description(description); }

    private void setGenre(SqlGenre genre) {this.genre = genre; }

    private void setAuthors(List<SqlAuthor> authors) {this.authors = authors; }

    public String getDescription(){ return this.description.toString(); }

    public SqlBook(String isbn, String title, String description, SqlGenre genre, List<SqlAuthor> authors, String photoURI) {
        setTitle(title);
        setIsbn(isbn);
        if(description != null)
            setDescription(description);
        if(genre==null)
            throw new IllegalArgumentException("Genre cannot be null");
        setGenre(genre);
        if(authors == null)
            throw new IllegalArgumentException("Author list is null");
        if(authors.isEmpty())
            throw new IllegalArgumentException("Author list is empty");

        setAuthors(authors);
        setPhotoInternal(photoURI);
    }

    protected SqlBook() {
        // got ORM only
    }

    public void removePhoto(long desiredVersion) {
        if(desiredVersion != this.version) {
            throw new ConflictException("Provided version does not match latest version of this object");
        }

        setPhotoInternal(null);
    }

    public void applyPatch(final Long desiredVersion, UpdateBookRequest request) {
        if (!Objects.equals(this.version, desiredVersion))
            throw new StaleObjectStateException("Object was already modified by another user", this.pk);

        String title = request.getTitle();
        String description = request.getDescription();
        //SqlGenre genre = request.getGenreObj();
        //List<SqlAuthor> authors = request.getAuthorObjList();
        String photoURI = request.getPhotoURI();
        if(title != null) {
            setTitle(title);
        }

        if(description != null) {
            setDescription(description);
        }

        if(genre != null) {
            setGenre(genre);
        }

        if(authors != null) {
            setAuthors(authors);
        }

        if(photoURI != null)
            setPhotoInternal(photoURI);

    }

    public String getIsbn(){
        return this.isbn.toString();
    }
}
