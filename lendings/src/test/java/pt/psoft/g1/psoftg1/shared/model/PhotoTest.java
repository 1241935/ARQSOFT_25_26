package pt.psoft.g1.psoftg1.shared.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class PhotoTest {

    // ==========================================
    // Constructor Tests
    // ==========================================

    @Test
    @DisplayName("When valid path is provided, then Photo is instantiated")
    void whenValidPath_thenPhotoIsInstantiated() {
        // Arrange
        Path validPath = Paths.get("/photos/user123.jpg");

        // Act
        Photo photo = new Photo(validPath);

        // Assert
        assertNotNull(photo);
        assertEquals(validPath.toString(), photo.getPhotoFile());
    }

    @Test
    @DisplayName("When relative path is provided, then Photo is instantiated")
    void whenRelativePath_thenPhotoIsInstantiated() {
        // Arrange
        Path relativePath = Paths.get("uploads/photos/image.png");

        // Act
        Photo photo = new Photo(relativePath);

        // Assert
        assertNotNull(photo);
        assertEquals(relativePath.toString(), photo.getPhotoFile());
    }

    @Test
    @DisplayName("When absolute path is provided, then Photo is instantiated")
    void whenAbsolutePath_thenPhotoIsInstantiated() {
        // Arrange
        Path absolutePath = Paths.get("/home/user/documents/photos/vacation.jpg");

        // Act
        Photo photo = new Photo(absolutePath);

        // Assert
        assertNotNull(photo);
        assertEquals(absolutePath.toString(), photo.getPhotoFile());
    }

    @Test
    @DisplayName("When Windows-style path is provided, then Photo is instantiated")
    void whenWindowsStylePath_thenPhotoIsInstantiated() {
        // Arrange
        Path windowsPath = Paths.get("C:\\Users\\John\\Pictures\\photo.jpg");

        // Act
        Photo photo = new Photo(windowsPath);

        // Assert
        assertNotNull(photo);
        assertEquals(windowsPath.toString(), photo.getPhotoFile());
    }

    @Test
    @DisplayName("When path with multiple directories is provided, then Photo is instantiated")
    void whenPathWithMultipleDirectories_thenPhotoIsInstantiated() {
        // Arrange
        Path multiDirPath = Paths.get("/var/www/html/uploads/images/users/profiles/avatar.jpg");

        // Act
        Photo photo = new Photo(multiDirPath);

        // Assert
        assertNotNull(photo);
        assertEquals(multiDirPath.toString(), photo.getPhotoFile());
    }

    // ==========================================
    // Getter and Setter Tests
    // ==========================================


    @Test
    @DisplayName("When getPhotoFile called, then returns correct path")
    void whenGetPhotoFileCalled_thenReturnsCorrectPath() {
        // Arrange
        Path expectedPath = Paths.get("/photos/test.jpg");
        Photo photo = new Photo(expectedPath);

        // Act
        String actualPath = photo.getPhotoFile();

        // Assert
        assertEquals(expectedPath.toString(), actualPath);
    }

    @Test
    @DisplayName("When setPhotoFile called with valid string, then photoFile is updated")
    void whenSetPhotoFileWithValidString_thenPhotoFileIsUpdated() {
        // Arrange
        Path initialPath = Paths.get("/photos/old.jpg");
        String newPhotoFile = "/photos/new.jpg";
        Photo photo = new Photo(initialPath);

        // Act
        photo.setPhotoFile(newPhotoFile);

        // Assert
        assertEquals(newPhotoFile, photo.getPhotoFile());
    }

    @Test
    @DisplayName("When setPhotoFile called with different valid string, then photoFile is updated")
    void whenSetPhotoFileWithDifferentValidString_thenPhotoFileIsUpdated() {
        // Arrange
        Path initialPath = Paths.get("/photos/image1.png");
        String newPhotoFile = "/uploads/images/image2.png";
        Photo photo = new Photo(initialPath);

        // Act
        photo.setPhotoFile(newPhotoFile);

        // Assert
        assertEquals(newPhotoFile, photo.getPhotoFile());
    }

    @Test
    @DisplayName("When setPhotoFile called multiple times, then photoFile reflects last value")
    void whenSetPhotoFileCalledMultipleTimes_thenPhotoFileReflectsLastValue() {
        // Arrange
        Path initialPath = Paths.get("/photos/first.jpg");
        Photo photo = new Photo(initialPath);

        // Act
        photo.setPhotoFile("/photos/second.jpg");
        photo.setPhotoFile("/photos/third.jpg");
        String finalPhotoFile = "/photos/fourth.jpg";
        photo.setPhotoFile(finalPhotoFile);

        // Assert
        assertEquals(finalPhotoFile, photo.getPhotoFile());
    }


    // ==========================================
    // Path Conversion Tests
    // ==========================================


    @Test
    @DisplayName("When Path is converted to String, then correct string representation is stored")
    void whenPathConvertedToString_thenCorrectStringIsStored() {
        // Arrange
        Path path = Paths.get("/photos/converted.jpg");
        String expectedString = path.toString();

        // Act
        Photo photo = new Photo(path);

        // Assert
        assertEquals(expectedString, photo.getPhotoFile());
    }

    @Test
    @DisplayName("When Path with special characters is provided, then correct string is stored")
    void whenPathWithSpecialCharacters_thenCorrectStringIsStored() {
        // Arrange
        Path pathWithSpaces = Paths.get("/photos/my photo 2024.jpg");

        // Act
        Photo photo = new Photo(pathWithSpaces);

        // Assert
        assertEquals(pathWithSpaces.toString(), photo.getPhotoFile());
    }

    @Test
    @DisplayName("When Path with unicode characters is provided, then correct string is stored")
    void whenPathWithUnicodeCharacters_thenCorrectStringIsStored() {
        // Arrange
        Path unicodePath = Paths.get("/photos/фото_João_李明.jpg");

        // Act
        Photo photo = new Photo(unicodePath);

        // Assert
        assertEquals(unicodePath.toString(), photo.getPhotoFile());
    }


    // ==========================================
    // Edge Case Tests
    // ==========================================


    @Test
    @DisplayName("When empty path is provided, then Photo is instantiated")
    void whenEmptyPath_thenPhotoIsInstantiated() {
        // Arrange
        Path emptyPath = Paths.get("");

        // Act
        Photo photo = new Photo(emptyPath);

        // Assert
        assertNotNull(photo);
        assertEquals(emptyPath.toString(), photo.getPhotoFile());
    }

    @Test
    @DisplayName("When single character path is provided, then Photo is instantiated")
    void whenSingleCharacterPath_thenPhotoIsInstantiated() {
        // Arrange
        Path singleCharPath = Paths.get("a");

        // Act
        Photo photo = new Photo(singleCharPath);

        // Assert
        assertNotNull(photo);
        assertEquals(singleCharPath.toString(), photo.getPhotoFile());
    }

    @Test
    @DisplayName("When very long path is provided, then Photo is instantiated")
    void whenVeryLongPath_thenPhotoIsInstantiated() {
        // Arrange
        String longDirectory = "a/".repeat(100);
        Path longPath = Paths.get(longDirectory + "photo.jpg");

        // Act
        Photo photo = new Photo(longPath);

        // Assert
        assertNotNull(photo);
        assertEquals(longPath.toString(), photo.getPhotoFile());
    }

    @Test
    @DisplayName("When path with dots is provided, then Photo is instantiated")
    void whenPathWithDots_thenPhotoIsInstantiated() {
        // Arrange
        Path pathWithDots = Paths.get("../../photos/../images/./photo.jpg");

        // Act
        Photo photo = new Photo(pathWithDots);

        // Assert
        assertNotNull(photo);
        assertEquals(pathWithDots.toString(), photo.getPhotoFile());
    }

    @Test
    @DisplayName("When path with only filename is provided, then Photo is instantiated")
    void whenPathWithOnlyFilename_thenPhotoIsInstantiated() {
        // Arrange
        Path filenamePath = Paths.get("photo.jpg");

        // Act
        Photo photo = new Photo(filenamePath);

        // Assert
        assertNotNull(photo);
        assertEquals(filenamePath.toString(), photo.getPhotoFile());
    }


    // ==========================================
    // File Extension Tests
    // ==========================================


    @Test
    @DisplayName("When JPG file path is provided, then Photo is instantiated")
    void whenJpgFilePath_thenPhotoIsInstantiated() {
        // Arrange
        Path jpgPath = Paths.get("/photos/image.jpg");

        // Act
        Photo photo = new Photo(jpgPath);

        // Assert
        assertNotNull(photo);
        assertTrue(photo.getPhotoFile().endsWith(".jpg"));
    }

    @Test
    @DisplayName("When PNG file path is provided, then Photo is instantiated")
    void whenPngFilePath_thenPhotoIsInstantiated() {
        // Arrange
        Path pngPath = Paths.get("/photos/image.png");

        // Act
        Photo photo = new Photo(pngPath);

        // Assert
        assertNotNull(photo);
        assertTrue(photo.getPhotoFile().endsWith(".png"));
    }

    @Test
    @DisplayName("When GIF file path is provided, then Photo is instantiated")
    void whenGifFilePath_thenPhotoIsInstantiated() {
        // Arrange
        Path gifPath = Paths.get("/photos/animation.gif");

        // Act
        Photo photo = new Photo(gifPath);

        // Assert
        assertNotNull(photo);
        assertTrue(photo.getPhotoFile().endsWith(".gif"));
    }

    @Test
    @DisplayName("When file with no extension is provided, then Photo is instantiated")
    void whenFileWithNoExtension_thenPhotoIsInstantiated() {
        // Arrange
        Path noExtensionPath = Paths.get("/photos/photofile");

        // Act
        Photo photo = new Photo(noExtensionPath);

        // Assert
        assertNotNull(photo);
        assertEquals(noExtensionPath.toString(), photo.getPhotoFile());
    }

    @Test
    @DisplayName("When file with multiple dots is provided, then Photo is instantiated")
    void whenFileWithMultipleDots_thenPhotoIsInstantiated() {
        // Arrange
        Path multiDotPath = Paths.get("/photos/my.photo.final.version.jpg");

        // Act
        Photo photo = new Photo(multiDotPath);

        // Assert
        assertNotNull(photo);
        assertEquals(multiDotPath.toString(), photo.getPhotoFile());
    }
}
