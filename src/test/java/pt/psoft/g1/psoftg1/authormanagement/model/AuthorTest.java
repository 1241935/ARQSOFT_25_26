package pt.psoft.g1.psoftg1.authormanagement.model;

import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;
import pt.psoft.g1.psoftg1.shared.model.Photo;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    void whenValidParameters_thenAuthorIsCreated() {
        // Arrange
        String name = "José Saramago";
        String bio = "Portuguese Nobel Prize winner";
        String photoURI = "photo.jpg";

        // Act
        Author author = new Author(name, bio, photoURI);

        // Assert
        assertNotNull(author);
        assertEquals(name, author.getName());
        assertEquals(bio, author.getBio());
        assertNotNull(author.getPhoto());
        assertEquals(photoURI, author.getPhoto().getPhotoFile());
    }

    @Test
    void whenValidName_thenNameIsSet() {
        // Arrange
        String initialName = "José Saramago";
        String bio = "Portuguese Nobel Prize winner";
        String photoURI = "http://example.com/photo.jpg";
        Author author = new Author(initialName, bio, photoURI);

        String newName = "Fernando Pessoa";

        // Act
        author.setName(newName);

        // Assert
        assertEquals(newName, author.getName());
    }

    @Test
    void whenValidBio_thenBioIsSet() {
        // Arrange
        String name = "José Saramago";
        String initialBio = "Portuguese Nobel Prize winner";
        String photoURI = "http://example.com/photo.jpg";
        Author author = new Author(name, initialBio, photoURI);

        String newBio = "Updated biography text";

        // Act
        author.setBio(newBio);

        // Assert
        assertEquals(newBio, author.getBio());
    }

    @Test
    void whenApplyPatchWithValidVersionAndName_thenNameIsUpdated() {
        // Arrange
        String name = "José Saramago";
        String bio = "Portuguese Nobel Prize winner";
        String photoURI = "http://example.com/photo.jpg";
        Author author = new Author(name, bio, photoURI);

        UpdateAuthorRequest request = mock(UpdateAuthorRequest.class);
        when(request.getName()).thenReturn("Fernando Pessoa");
        when(request.getBio()).thenReturn(null);
        when(request.getPhotoURI()).thenReturn(null);

        long currentVersion = author.getVersion();

        // Act
        author.applyPatch(currentVersion, request);

        // Assert
        assertEquals("Fernando Pessoa", author.getName());
        assertEquals(bio, author.getBio());
    }

    @Test
    void whenApplyPatchWithValidVersionAndBio_thenBioIsUpdated() {
        // Arrange
        String name = "José Saramago";
        String bio = "Portuguese Nobel Prize winner";
        String photoURI = "http://example.com/photo.jpg";
        Author author = new Author(name, bio, photoURI);

        UpdateAuthorRequest request = mock(UpdateAuthorRequest.class);
        when(request.getName()).thenReturn(null);
        when(request.getBio()).thenReturn("Updated biography");
        when(request.getPhotoURI()).thenReturn(null);

        long currentVersion = author.getVersion();

        // Act
        author.applyPatch(currentVersion, request);

        // Assert
        assertEquals(name, author.getName());
        assertEquals("Updated biography", author.getBio());
    }

    @Test
    void whenApplyPatchWithValidVersionAndPhoto_thenPhotoIsUpdated() {
        // Arrange
        String name = "José Saramago";
        String bio = "Portuguese Nobel Prize winner";
        String photoURI = "photo.jpg";
        Author author = new Author(name, bio, photoURI);

        UpdateAuthorRequest request = mock(UpdateAuthorRequest.class);
        when(request.getName()).thenReturn(null);
        when(request.getBio()).thenReturn(null);
        when(request.getPhotoURI()).thenReturn("new-photo.jpg");

        long currentVersion = author.getVersion();

        // Act
        author.applyPatch(currentVersion, request);

        // Assert
        assertNotNull(author.getPhoto());
        assertEquals("new-photo.jpg", author.getPhoto().getPhotoFile());
    }

    @Test
    void whenApplyPatchWithAllFields_thenAllFieldsAreUpdated() {
        // Arrange
        String name = "José Saramago";
        String bio = "Portuguese Nobel Prize winner";
        String photoURI = "photo.jpg";
        Author author = new Author(name, bio, photoURI);

        UpdateAuthorRequest request = mock(UpdateAuthorRequest.class);
        when(request.getName()).thenReturn("Fernando Pessoa");
        when(request.getBio()).thenReturn("Portuguese poet");
        when(request.getPhotoURI()).thenReturn("new-photo.jpg");

        long currentVersion = author.getVersion();

        // Act
        author.applyPatch(currentVersion, request);

        // Assert
        assertEquals("Fernando Pessoa", author.getName());
        assertEquals("Portuguese poet", author.getBio());
        assertNotNull(author.getPhoto());
        assertEquals("new-photo.jpg", author.getPhoto().getPhotoFile());
    }

    @Test
    void whenApplyPatchWithInvalidVersion_thenThrowsStaleObjectStateException() {
        // Arrange
        String expectedMessage = "Object was already modified by another user";

        String name = "José Saramago";
        String bio = "Portuguese Nobel Prize winner";
        String photoURI = "photo.jpg";
        Author author = new Author(name, bio, photoURI);

        UpdateAuthorRequest request = mock(UpdateAuthorRequest.class);
        when(request.getName()).thenReturn("Fernando Pessoa");

        long currentVersion = author.getVersion();
        long wrongVersion = currentVersion + 1;

        // Act + Assert
        Exception exception = assertThrows(StaleObjectStateException.class, () ->
                author.applyPatch(wrongVersion, request)
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void whenApplyPatchWithNullFields_thenOriginalValuesAreKept() {
        // Arrange
        String name = "José Saramago";
        String bio = "Portuguese Nobel Prize winner";
        String photoURI = "photo.jpg";
        Author author = new Author(name, bio, photoURI);

        UpdateAuthorRequest request = mock(UpdateAuthorRequest.class);
        when(request.getName()).thenReturn(null);
        when(request.getBio()).thenReturn(null);
        when(request.getPhotoURI()).thenReturn(null);

        long currentVersion = author.getVersion();

        // Act
        author.applyPatch(currentVersion, request);

        // Assert
        assertEquals(name, author.getName());
        assertEquals(bio, author.getBio());
        assertNotNull(author.getPhoto());
        assertEquals(photoURI, author.getPhoto().getPhotoFile());
    }

    @Test
    void whenRemovePhotoWithValidVersion_thenPhotoIsRemoved() {
        // Arrange
        String name = "José Saramago";
        String bio = "Portuguese Nobel Prize winner";
        String photoURI = "http://example.com/photo.jpg";
        Author author = new Author(name, bio, photoURI);

        long currentVersion = author.getVersion();

        // Act
        author.removePhoto(currentVersion);

        // Assert
        assertNull(author.getPhoto());
    }

    @Test
    void whenRemovePhotoWithInvalidVersion_thenThrowsConflictException() {
        // Arrange
        String expectedMessage = "Provided version does not match latest version of this object";

        String name = "José Saramago";
        String bio = "Portuguese Nobel Prize winner";
        String photoURI = "http://example.com/photo.jpg";
        Author author = new Author(name, bio, photoURI);

        long currentVersion = author.getVersion();
        long wrongVersion = currentVersion + 1;

        // Act + Assert
        Exception exception = assertThrows(ConflictException.class, () ->
                author.removePhoto(wrongVersion)
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void whenGetId_thenReturnsAuthorNumber() {
        // Arrange
        String name = "José Saramago";
        String bio = "Portuguese Nobel Prize winner";
        String photoURI = "http://example.com/photo.jpg";
        Author author = new Author(name, bio, photoURI);

        // Act
        Long id = author.getId();

        // Assert
        assertEquals(author.getAuthorNumber(), id);
    }

    @Test
    void whenNewAuthorCreated_thenVersionIsInitialized() {
        // Arrange
        String name = "José Saramago";
        String bio = "Portuguese Nobel Prize winner";
        String photoURI = "http://example.com/photo.jpg";

        // Act
        Author author = new Author(name, bio, photoURI);

        // Assert
        assertNotNull(author.getVersion());
        assertEquals(0L, author.getVersion());
    }
}

