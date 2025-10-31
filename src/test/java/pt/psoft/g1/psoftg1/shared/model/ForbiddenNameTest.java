package pt.psoft.g1.psoftg1.shared.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class ForbiddenNameTest {

    @Test
    @DisplayName("When valid name is provided, then ForbiddenName is instantiated")
    void whenValidName_thenForbiddenNameIsInstantiated() {
        // Arrange
        String forbiddenWord = "badword";

        // Act
        ForbiddenName forbiddenName = new ForbiddenName(forbiddenWord);

        // Assert
        assertNotNull(forbiddenName);
        assertEquals(forbiddenWord, forbiddenName.getForbiddenName());
    }

    @Test
    @DisplayName("When single character name is provided, then ForbiddenName is instantiated")
    void whenSingleCharacterName_thenForbiddenNameIsInstantiated() {
        // Arrange
        String singleChar = "a";

        // Act
        ForbiddenName forbiddenName = new ForbiddenName(singleChar);

        // Assert
        assertNotNull(forbiddenName);
        assertEquals(singleChar, forbiddenName.getForbiddenName());
    }

    @Test
    @DisplayName("When long name is provided, then ForbiddenName is instantiated")
    void whenLongName_thenForbiddenNameIsInstantiated() {
        // Arrange
        String longWord = "verylongforbiddenwordthatshouldbeavoided";

        // Act
        ForbiddenName forbiddenName = new ForbiddenName(longWord);

        // Assert
        assertNotNull(forbiddenName);
        assertEquals(longWord, forbiddenName.getForbiddenName());
    }

    @Test
    @DisplayName("When name with spaces is provided, then ForbiddenName is instantiated")
    void whenNameWithSpaces_thenForbiddenNameIsInstantiated() {
        // Arrange
        String nameWithSpaces = "bad word";

        // Act
        ForbiddenName forbiddenName = new ForbiddenName(nameWithSpaces);

        // Assert
        assertNotNull(forbiddenName);
        assertEquals(nameWithSpaces, forbiddenName.getForbiddenName());
    }

    @Test
    @DisplayName("When name with special characters is provided, then ForbiddenName is instantiated")
    void whenNameWithSpecialCharacters_thenForbiddenNameIsInstantiated() {
        // Arrange
        String nameWithSpecialChars = "@#$%";

        // Act
        ForbiddenName forbiddenName = new ForbiddenName(nameWithSpecialChars);

        // Assert
        assertNotNull(forbiddenName);
        assertEquals(nameWithSpecialChars, forbiddenName.getForbiddenName());
    }

    @Test
    @DisplayName("When getForbiddenName is called, then returns correct name")
    void whenGetForbiddenNameCalled_thenReturnsCorrectName() {
        // Arrange
        String expectedName = "badword";
        ForbiddenName forbiddenName = new ForbiddenName(expectedName);

        // Act
        String actualName = forbiddenName.getForbiddenName();

        // Assert
        assertEquals(expectedName, actualName);
    }

    @Test
    @DisplayName("When setForbiddenName is called with valid name, then name is updated")
    void whenSetForbiddenNameWithValidName_thenNameIsUpdated() {
        // Arrange
        String initialName = "oldword";
        String newName = "newword";
        ForbiddenName forbiddenName = new ForbiddenName(initialName);

        // Act
        forbiddenName.setForbiddenName(newName);

        // Assert
        assertEquals(newName, forbiddenName.getForbiddenName());
    }

    @Test
    @DisplayName("When setForbiddenName is called with different name, then name is updated")
    void whenSetForbiddenNameWithDifferentName_thenNameIsUpdated() {
        // Arrange
        String initialName = "word1";
        String newName = "word2";
        ForbiddenName forbiddenName = new ForbiddenName(initialName);

        // Act
        forbiddenName.setForbiddenName(newName);

        // Assert
        assertEquals(newName, forbiddenName.getForbiddenName());
    }

    @Test
    @DisplayName("When no-arg constructor is called, then ForbiddenName is instantiated")
    void whenNoArgConstructorCalled_thenForbiddenNameIsInstantiated() {
        // Act
        ForbiddenName forbiddenName = new ForbiddenName();

        // Assert
        assertNotNull(forbiddenName);
    }

    @Test
    @DisplayName("When no-arg constructor is used and name is set, then name is stored correctly")
    void whenNoArgConstructorUsedAndNameSet_thenNameIsStoredCorrectly() {
        // Arrange
        String expectedName = "testword";
        ForbiddenName forbiddenName = new ForbiddenName();

        // Act
        forbiddenName.setForbiddenName(expectedName);

        // Assert
        assertEquals(expectedName, forbiddenName.getForbiddenName());
    }

    @Test
    @DisplayName("When uppercase name is provided, then ForbiddenName is instantiated")
    void whenUppercaseName_thenForbiddenNameIsInstantiated() {
        // Arrange
        String uppercaseName = "BADWORD";

        // Act
        ForbiddenName forbiddenName = new ForbiddenName(uppercaseName);

        // Assert
        assertNotNull(forbiddenName);
        assertEquals(uppercaseName, forbiddenName.getForbiddenName());
    }

    @Test
    @DisplayName("When mixed case name is provided, then ForbiddenName is instantiated")
    void whenMixedCaseName_thenForbiddenNameIsInstantiated() {
        // Arrange
        String mixedCaseName = "BaDWoRd";

        // Act
        ForbiddenName forbiddenName = new ForbiddenName(mixedCaseName);

        // Assert
        assertNotNull(forbiddenName);
        assertEquals(mixedCaseName, forbiddenName.getForbiddenName());
    }

    @Test
    @DisplayName("When numeric name is provided, then ForbiddenName is instantiated")
    void whenNumericName_thenForbiddenNameIsInstantiated() {
        // Arrange
        String numericName = "12345";

        // Act
        ForbiddenName forbiddenName = new ForbiddenName(numericName);

        // Assert
        assertNotNull(forbiddenName);
        assertEquals(numericName, forbiddenName.getForbiddenName());
    }

    @Test
    @DisplayName("When alphanumeric name is provided, then ForbiddenName is instantiated")
    void whenAlphanumericName_thenForbiddenNameIsInstantiated() {
        // Arrange
        String alphanumericName = "badword123";

        // Act
        ForbiddenName forbiddenName = new ForbiddenName(alphanumericName);

        // Assert
        assertNotNull(forbiddenName);
        assertEquals(alphanumericName, forbiddenName.getForbiddenName());
    }

    @Test
    @DisplayName("When name with unicode characters is provided, then ForbiddenName is instantiated")
    void whenNameWithUnicodeCharacters_thenForbiddenNameIsInstantiated() {
        // Arrange
        String unicodeName = "słowo";

        // Act
        ForbiddenName forbiddenName = new ForbiddenName(unicodeName);

        // Assert
        assertNotNull(forbiddenName);
        assertEquals(unicodeName, forbiddenName.getForbiddenName());
    }

    @Test
    @DisplayName("When setForbiddenName is called multiple times, then last value is stored")
    void whenSetForbiddenNameCalledMultipleTimes_thenLastValueIsStored() {
        // Arrange
        ForbiddenName forbiddenName = new ForbiddenName("word1");

        // Act
        forbiddenName.setForbiddenName("word2");
        forbiddenName.setForbiddenName("word3");
        String finalName = "word4";
        forbiddenName.setForbiddenName(finalName);

        // Assert
        assertEquals(finalName, forbiddenName.getForbiddenName());
    }
}
