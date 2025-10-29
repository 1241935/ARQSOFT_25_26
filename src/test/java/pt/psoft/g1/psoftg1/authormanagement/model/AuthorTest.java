package pt.psoft.g1.psoftg1.authormanagement.model;

import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;
import pt.psoft.g1.psoftg1.shared.model.Photo;


import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {
    private final String validName = "João Alberto";
    private final String validBio = "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.";

    private final UpdateAuthorRequest request = new UpdateAuthorRequest(validName, validBio, null, null);
    private Author author;

    private static class EntityWithPhotoImpl extends EntityWithPhoto {
    }

    @BeforeEach
    void setUp() {
        author = new Author(validName, validBio, null);
    }


    // ===============================================
    //  Basic Validation
    // ===============================================
    @Test
    void ensureNameNotNull() {
        assertThrows(IllegalArgumentException.class, () -> new Author(null, validBio, null));
    }

    @Test
    void ensureBioNotNull() {
        assertThrows(IllegalArgumentException.class, () -> new Author(validName, null, null));
    }

    @Test
    void whenVersionIsStaleItIsNotPossibleToPatch() {
        final var subject = new Author(validName, validBio, null);

        assertThrows(StaleObjectStateException.class, () -> subject.applyPatch(999, request));
    }

    @Test
    void testCreateAuthorWithoutPhoto() {
        Author author = new Author(validName, validBio, null);
        assertNotNull(author);
        assertNull(author.getPhoto());
    }

    @Test
    void testCreateAuthorRequestWithPhoto() {
        CreateAuthorRequest request = new CreateAuthorRequest(validName, validBio, null, "photoTest.jpg");
        Author author = new Author(request.getName(), request.getBio(), "photoTest.jpg");
        assertNotNull(author);
        assertEquals(request.getPhotoURI(), author.getPhoto().getPhotoFile());
    }

    @Test
    void testCreateAuthorRequestWithoutPhoto() {
        CreateAuthorRequest request = new CreateAuthorRequest(validName, validBio, null, null);
        Author author = new Author(request.getName(), request.getBio(), null);
        assertNotNull(author);
        assertNull(author.getPhoto());
    }

    @Test
    void testEntityWithPhotoSetPhotoInternalWithValidURI() {
        EntityWithPhoto entity = new EntityWithPhotoImpl();
        String validPhotoURI = "photoTest.jpg";
        entity.setPhoto(validPhotoURI);
        assertNotNull(entity.getPhoto());
    }

    @Test
    void ensurePhotoCanBeNull_AkaOptional() {
        Author author = new Author(validName, validBio, null);
        assertNull(author.getPhoto());
    }

    @Test
    void ensureValidPhoto() {
        Author author = new Author(validName, validBio, "photoTest.jpg");
        Photo photo = author.getPhoto();
        assertNotNull(photo);
        assertEquals("photoTest.jpg", photo.getPhotoFile());
    }
    // ===============================================
    // TESTES TRANSPARENT-BOX
    // ===============================================


    @Test
    @DisplayName("Should create author with all valid parameters")
    void testConstructor_AllValidParameters() {
        Author newAuthor = new Author("Jane Smith", "Bio text", "photo.jpg");

        assertNotNull(newAuthor);
        assertEquals("Jane Smith", newAuthor.getName());
        assertEquals("Bio text", newAuthor.getBio());
        assertEquals("photo.jpg", newAuthor.getPhoto().getPhotoFile());
    }

    @Test
    @DisplayName("Should throw exception with empty name")
    void testConstructor_EmptyName() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Author("", validBio, "photo.jpg");
        });
    }

    @Test
    @DisplayName("Should throw exception with blank name")
    void testConstructor_BlankName() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Author("   ", validBio, "photo.jpg");
        });
    }

    @Test
    @DisplayName("Should throw exception with empty bio")
    void testConstructor_EmptyBio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Author(validName, "", "photo.jpg");
        });
    }


    @Test
    @DisplayName("Should update all fields with correct version")
    void testApplyPatch_AllFieldsWithCorrectVersion() {
        UpdateAuthorRequest updateRequest = new UpdateAuthorRequest(
                "Updated Bio",
                "Updated Name",
                null,
                "updated-photo.jpg"
        );

        long currentVersion = author.getVersion();
        author.applyPatch(currentVersion, updateRequest);

        assertEquals("Updated Name", author.getName());
        assertEquals("Updated Bio", author.getBio());
        assertEquals("updated-photo.jpg", author.getPhoto().getPhotoFile());
    }

    @Test
    @DisplayName("Should throw StaleObjectStateException with wrong version")
    void testApplyPatch_WrongVersion() {
        UpdateAuthorRequest updateRequest = new UpdateAuthorRequest(
                "Updated Name",
                null,
                null,
                null
        );

        long wrongVersion = author.getVersion() + 1;

        assertThrows(StaleObjectStateException.class, () -> {
            author.applyPatch(wrongVersion, updateRequest);
        });
    }

    @Test
    @DisplayName("Should update only name when only name is provided")
    void testApplyPatch_OnlyName() {
        UpdateAuthorRequest updateRequest = new UpdateAuthorRequest(
                null,
                "Only Name Updated",
                null,
                null
        );

        String originalBio = author.getBio();

        author.applyPatch(author.getVersion(), updateRequest);

        assertEquals("Only Name Updated", author.getName());
        assertEquals(originalBio, author.getBio());
    }

    @Test
    @DisplayName("Should update only bio when only bio is provided")
    void testApplyPatch_OnlyBio() {
        UpdateAuthorRequest updateRequest = new UpdateAuthorRequest(
                "Only Bio Updated",
                null,
                null,
                null
        );

        String originalName = author.getName();

        author.applyPatch(author.getVersion(), updateRequest);

        assertEquals(originalName, author.getName());
        assertEquals("Only Bio Updated", author.getBio());
    }

    @Test
    @DisplayName("Should update only photo when only photo is provided")
    void testApplyPatch_OnlyPhoto() {
        Author authorWithPhoto = new Author(validName, validBio, "original.jpg");

        UpdateAuthorRequest updateRequest = new UpdateAuthorRequest(
                null,
                null,
                null,
                "new-photo.jpg"
        );

        String originalName = authorWithPhoto.getName();
        String originalBio = authorWithPhoto.getBio();

        authorWithPhoto.applyPatch(authorWithPhoto.getVersion(), updateRequest);

        assertEquals(originalName, authorWithPhoto.getName());
        assertEquals(originalBio, authorWithPhoto.getBio());
        assertEquals("new-photo.jpg", authorWithPhoto.getPhoto().getPhotoFile());
    }

    @Test
    @DisplayName("Should not update when all fields are null")
    void testApplyPatch_AllFieldsNull() {
        UpdateAuthorRequest emptyRequest = new UpdateAuthorRequest(null, null, null, null);

        String originalName = author.getName();
        String originalBio = author.getBio();

        author.applyPatch(author.getVersion(), emptyRequest);

        assertEquals(originalName, author.getName());
        assertEquals(originalBio, author.getBio());
    }

    @Test
    @DisplayName("Should handle combination of null and non-null fields")
    void testApplyPatch_MixedNullAndNonNull() {
        Author authorWithPhoto = new Author(validName, validBio, "photo.jpg");

        UpdateAuthorRequest updateRequest = new UpdateAuthorRequest(
                null,
                "New Name",  // bio stays unchanged
                null,
                "new-photo.jpg"
        );

        String originalBio = authorWithPhoto.getBio();

        authorWithPhoto.applyPatch(authorWithPhoto.getVersion(), updateRequest);

        assertEquals("New Name", authorWithPhoto.getName());
        assertEquals(originalBio, authorWithPhoto.getBio());
        assertEquals("new-photo.jpg", authorWithPhoto.getPhoto().getPhotoFile());
    }

    @Test
    @DisplayName("Should throw exception when version is negative")
    void testApplyPatch_NegativeVersion() {
        UpdateAuthorRequest updateRequest = new UpdateAuthorRequest(
                "Updated Name",
                null,
                null,
                null
        );

        assertThrows(StaleObjectStateException.class, () -> {
            author.applyPatch(-1, updateRequest);
        });
    }
}

