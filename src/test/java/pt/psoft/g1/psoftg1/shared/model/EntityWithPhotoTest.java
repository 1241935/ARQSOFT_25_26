package pt.psoft.g1.psoftg1.shared.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class EntityWithPhotoTest {

    // Concrete implementation for testing the abstract class
    private static class TestEntityWithPhoto extends EntityWithPhoto {
        // Simple concrete class for testing purposes
    }

    @Test
    @DisplayName("When valid photo URI is set, then photo is created")
    void whenValidPhotoURISet_thenPhotoIsCreated() {
        // Arrange
        String photoURI = "/photos/test.jpg";
        TestEntityWithPhoto entity = new TestEntityWithPhoto();

        // Act
        entity.setPhoto(photoURI);

        // Assert
        assertNotNull(entity.getPhoto());
        assertEquals(photoURI, entity.getPhoto().getPhotoFile());
    }

    @Test
    @DisplayName("When absolute path is set, then photo is created")
    void whenAbsolutePathSet_thenPhotoIsCreated() {
        // Arrange
        String absolutePath = "/home/user/photos/image.png";
        TestEntityWithPhoto entity = new TestEntityWithPhoto();

        // Act
        entity.setPhoto(absolutePath);

        // Assert
        assertNotNull(entity.getPhoto());
        assertEquals(absolutePath, entity.getPhoto().getPhotoFile());
    }

    @Test
    @DisplayName("When relative path is set, then photo is created")
    void whenRelativePathSet_thenPhotoIsCreated() {
        // Arrange
        String relativePath = "uploads/photos/image.jpg";
        TestEntityWithPhoto entity = new TestEntityWithPhoto();

        // Act
        entity.setPhoto(relativePath);

        // Assert
        assertNotNull(entity.getPhoto());
        assertEquals(relativePath, entity.getPhoto().getPhotoFile());
    }

    @Test
    @DisplayName("When null photo URI is set, then photo is null")
    void whenNullPhotoURISet_thenPhotoIsNull() {
        // Arrange
        TestEntityWithPhoto entity = new TestEntityWithPhoto();
        entity.setPhoto("/photos/initial.jpg"); // Set initial photo

        // Act
        entity.setPhoto(null);

        // Assert
        assertNull(entity.getPhoto());
    }

    @Test
    @DisplayName("When null URI is set on new entity, then photo remains null")
    void whenNullURISetOnNewEntity_thenPhotoRemainsNull() {
        // Arrange
        TestEntityWithPhoto entity = new TestEntityWithPhoto();

        // Act
        entity.setPhoto(null);

        // Assert
        assertNull(entity.getPhoto());
    }

    @Test
    @DisplayName("When invalid path is set, then photo is null")
    void whenInvalidPathSet_thenPhotoIsNull() {
        // Arrange
        String invalidPath = "\0invalid"; // Null character is invalid in paths
        TestEntityWithPhoto entity = new TestEntityWithPhoto();

        // Act
        entity.setPhoto(invalidPath);

        // Assert
        assertNull(entity.getPhoto());
    }

    @Test
    @DisplayName("When photo URI is updated, then photo is replaced")
    void whenPhotoURIUpdated_thenPhotoIsReplaced() {
        // Arrange
        String initialURI = "/photos/old.jpg";
        String newURI = "/photos/new.jpg";
        TestEntityWithPhoto entity = new TestEntityWithPhoto();
        entity.setPhoto(initialURI);

        // Act
        entity.setPhoto(newURI);

        // Assert
        assertNotNull(entity.getPhoto());
        assertEquals(newURI, entity.getPhoto().getPhotoFile());
    }

    @Test
    @DisplayName("When photo URI with spaces is set, then photo is created")
    void whenPhotoURIWithSpacesSet_thenPhotoIsCreated() {
        // Arrange
        String uriWithSpaces = "/photos/my photo.jpg";
        TestEntityWithPhoto entity = new TestEntityWithPhoto();

        // Act
        entity.setPhoto(uriWithSpaces);

        // Assert
        assertNotNull(entity.getPhoto());
        assertEquals(uriWithSpaces, entity.getPhoto().getPhotoFile());
    }

    @Test
    @DisplayName("When photo URI with unicode is set, then photo is created")
    void whenPhotoURIWithUnicodeSet_thenPhotoIsCreated() {
        // Arrange
        String unicodeURI = "/photos/фото.jpg";
        TestEntityWithPhoto entity = new TestEntityWithPhoto();

        // Act
        entity.setPhoto(unicodeURI);

        // Assert
        assertNotNull(entity.getPhoto());
        assertEquals(unicodeURI, entity.getPhoto().getPhotoFile());
    }

    @Test
    @DisplayName("When Windows-style path is set, then photo is created")
    void whenWindowsStylePathSet_thenPhotoIsCreated() {
        // Arrange
        String windowsPath = "C:\\Users\\John\\Photos\\photo.jpg";
        TestEntityWithPhoto entity = new TestEntityWithPhoto();

        // Act
        entity.setPhoto(windowsPath);

        // Assert
        assertNotNull(entity.getPhoto());
        assertEquals(windowsPath, entity.getPhoto().getPhotoFile());
    }

    @Test
    @DisplayName("When getPhoto is called on new entity, then returns null")
    void whenGetPhotoCalledOnNewEntity_thenReturnsNull() {
        // Arrange
        TestEntityWithPhoto entity = new TestEntityWithPhoto();

        // Act
        Photo photo = entity.getPhoto();

        // Assert
        assertNull(photo);
    }

    @Test
    @DisplayName("When getPhoto is called after setPhoto, then returns photo")
    void whenGetPhotoCalledAfterSetPhoto_thenReturnsPhoto() {
        // Arrange
        String photoURI = "/photos/test.jpg";
        TestEntityWithPhoto entity = new TestEntityWithPhoto();
        entity.setPhoto(photoURI);

        // Act
        Photo photo = entity.getPhoto();

        // Assert
        assertNotNull(photo);
        assertEquals(photoURI, photo.getPhotoFile());
    }

    @Test
    @DisplayName("When empty string is set as photo URI, then photo is created")
    void whenEmptyStringSetAsPhotoURI_thenPhotoIsCreated() {
        // Arrange
        String emptyURI = "";
        TestEntityWithPhoto entity = new TestEntityWithPhoto();

        // Act
        entity.setPhoto(emptyURI);

        // Assert
        assertNotNull(entity.getPhoto());
        assertEquals(emptyURI, entity.getPhoto().getPhotoFile());
    }

    @Test
    @DisplayName("When photo URI with dots is set, then photo is created")
    void whenPhotoURIWithDotsSet_thenPhotoIsCreated() {
        // Arrange
        String uriWithDots = "../photos/./image.jpg";
        TestEntityWithPhoto entity = new TestEntityWithPhoto();

        // Act
        entity.setPhoto(uriWithDots);

        // Assert
        assertNotNull(entity.getPhoto());
        assertEquals(uriWithDots, entity.getPhoto().getPhotoFile());
    }

    @Test
    @DisplayName("When very long photo URI is set, then photo is created")
    void whenVeryLongPhotoURISet_thenPhotoIsCreated() {
        // Arrange
        String longURI = "/photos/" + "a/".repeat(100) + "image.jpg";
        TestEntityWithPhoto entity = new TestEntityWithPhoto();

        // Act
        entity.setPhoto(longURI);

        // Assert
        assertNotNull(entity.getPhoto());
        assertEquals(longURI, entity.getPhoto().getPhotoFile());
    }

    @Test
    @DisplayName("When setPhotoInternal is called with valid URI, then photo is set")
    void whenSetPhotoInternalCalledWithValidURI_thenPhotoIsSet() {
        // Arrange
        String photoURI = "/photos/internal.jpg";
        TestEntityWithPhoto entity = new TestEntityWithPhoto();

        // Act
        entity.setPhotoInternal(photoURI);

        // Assert
        assertNotNull(entity.getPhoto());
        assertEquals(photoURI, entity.getPhoto().getPhotoFile());
    }

    @Test
    @DisplayName("When setPhotoInternal is called with null, then photo is null")
    void whenSetPhotoInternalCalledWithNull_thenPhotoIsNull() {
        // Arrange
        TestEntityWithPhoto entity = new TestEntityWithPhoto();
        entity.setPhotoInternal("/photos/initial.jpg");

        // Act
        entity.setPhotoInternal(null);

        // Assert
        assertNull(entity.getPhoto());
    }
}
