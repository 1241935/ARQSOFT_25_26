package pt.psoft.g1.psoftg1.shared.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

class NameTest {

    // ==========================================
    // Constructor Tests
    // ==========================================


    @Test
    @DisplayName("When valid name is provided, then Name is instantiated")
    void whenValidName_thenNameIsInstantiated() {
        // Arrange
        String validName = "John Doe";

        // Act
        Name name = new Name(validName);

        // Assert
        assertNotNull(name);
        assertEquals(validName, name.toString());
    }

    @Test
    @DisplayName("When alphanumeric name is provided, then Name is instantiated")
    void whenAlphanumericName_thenNameIsInstantiated() {
        // Arrange
        String alphanumericName = "JohnDoe123";

        // Act
        Name name = new Name(alphanumericName);

        // Assert
        assertNotNull(name);
        assertEquals(alphanumericName, name.toString());
    }

    @Test
    @DisplayName("When name with spaces is provided, then Name is instantiated")
    void whenNameWithSpaces_thenNameIsInstantiated() {
        // Arrange
        String nameWithSpaces = "John Doe Junior";

        // Act
        Name name = new Name(nameWithSpaces);

        // Assert
        assertNotNull(name);
        assertEquals(nameWithSpaces, name.toString());
    }

    @Test
    @DisplayName("When single character name is provided, then Name is instantiated")
    void whenSingleCharacterName_thenNameIsInstantiated() {
        // Arrange
        String singleChar = "A";

        // Act
        Name name = new Name(singleChar);

        // Assert
        assertNotNull(name);
        assertEquals(singleChar, name.toString());
    }


    // ==========================================
    // Null Validation Tests
    // ==========================================


    @Test
    @DisplayName("When null name is provided, then throws IllegalArgumentException")
    void whenNullName_thenThrowsException() {
        // Arrange
        String expectedMessage = "Name cannot be null";
        String nullName = null;

        // Act + Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Name(nullName)
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("When setName called with null, then throws IllegalArgumentException")
    void whenSetNameWithNull_thenThrowsException() {
        // Arrange
        String expectedMessage = "Name cannot be null";
        String initialName = "John";
        Name name = new Name(initialName);

        // Act + Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                name.setName(null)
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        // Verify original name is preserved
        assertEquals(initialName, name.toString());
    }


    // ==========================================
    // Blank Validation Tests
    // ==========================================


    @Test
    @DisplayName("When empty string is provided, then throws IllegalArgumentException")
    void whenEmptyString_thenThrowsException() {
        // Arrange
        String expectedMessage = "Name cannot be blank, nor only white spaces";
        String emptyName = "";

        // Act + Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Name(emptyName)
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("When blank string with spaces is provided, then throws IllegalArgumentException")
    void whenBlankStringWithSpaces_thenThrowsException() {
        // Arrange
        String expectedMessage = "Name cannot be blank, nor only white spaces";
        String blankName = "   ";

        // Act + Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Name(blankName)
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("When blank string with tabs is provided, then throws IllegalArgumentException")
    void whenBlankStringWithTabs_thenThrowsException() {
        // Arrange
        String expectedMessage = "Name cannot be blank, nor only white spaces";
        String blankName = "\t\t";

        // Act + Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Name(blankName)
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("When blank string with mixed whitespace is provided, then throws IllegalArgumentException")
    void whenBlankStringWithMixedWhitespace_thenThrowsException() {
        // Arrange
        String expectedMessage = "Name cannot be blank, nor only white spaces";
        String blankName = " \t \n ";

        // Act + Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Name(blankName)
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


    // ==========================================
    // Alphanumeric Validation Tests
    // ==========================================


    @Test
    @DisplayName("When name with special characters is provided, then throws IllegalArgumentException")
    void whenNameWithSpecialCharacters_thenThrowsException() {
        // Arrange
        String expectedMessage = "Name can only contain alphanumeric characters";
        String nameWithSpecialChars = "John@Doe";

        // Act + Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Name(nameWithSpecialChars)
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("When name with symbols is provided, then throws IllegalArgumentException")
    void whenNameWithSymbols_thenThrowsException() {
        // Arrange
        String expectedMessage = "Name can only contain alphanumeric characters";
        String nameWithSymbols = "John#Doe$";

        // Act + Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Name(nameWithSymbols)
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("When name with punctuation is provided, then throws IllegalArgumentException")
    void whenNameWithPunctuation_thenThrowsException() {
        // Arrange
        String expectedMessage = "Name can only contain alphanumeric characters";
        String nameWithPunctuation = "John.Doe!";

        // Act + Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Name(nameWithPunctuation)
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("When name with only numbers is provided, then Name is instantiated")
    void whenNameWithOnlyNumbers_thenNameIsInstantiated() {
        // Arrange
        String numericName = "123456";

        // Act
        Name name = new Name(numericName);

        // Assert
        assertNotNull(name);
        assertEquals(numericName, name.toString());
    }


    // ==========================================
    // SetName Tests
    // ==========================================


    @Test
    @DisplayName("When setName called with valid name, then name is updated")
    void whenSetNameWithValidName_thenNameIsUpdated() {
        // Arrange
        String initialName = "John";
        String newName = "Jane";
        Name name = new Name(initialName);

        // Act
        name.setName(newName);

        // Assert
        assertEquals(newName, name.toString());
    }

    @Test
    @DisplayName("When setName called with different valid name, then name is updated")
    void whenSetNameWithDifferentValidName_thenNameIsUpdated() {
        // Arrange
        String initialName = "Alice";
        String newName = "Bob123";
        Name name = new Name(initialName);

        // Act
        name.setName(newName);

        // Assert
        assertEquals(newName, name.toString());
    }

    @Test
    @DisplayName("When setName called with empty string, then throws IllegalArgumentException")
    void whenSetNameWithEmptyString_thenThrowsException() {
        // Arrange
        String expectedMessage = "Name cannot be blank, nor only white spaces";
        String initialName = "John";
        Name name = new Name(initialName);

        // Act + Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                name.setName("")
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(initialName, name.toString());
    }

    @Test
    @DisplayName("When setName called with invalid characters, then throws IllegalArgumentException")
    void whenSetNameWithInvalidCharacters_thenThrowsException() {
        // Arrange
        String expectedMessage = "Name can only contain alphanumeric characters";
        String initialName = "John";
        Name name = new Name(initialName);

        // Act + Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                name.setName("Invalid@Name")
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(initialName, name.toString());
    }


    // ==========================================
    // ToString Tests
    // ==========================================


    @Test
    @DisplayName("When toString called, then returns correct name")
    void whenToStringCalled_thenReturnsCorrectName() {
        // Arrange
        String expectedName = "John Doe";
        Name name = new Name(expectedName);

        // Act
        String actualName = name.toString();

        // Assert
        assertEquals(expectedName, actualName);
    }

    @Test
    @DisplayName("When toString called after setName, then returns updated name")
    void whenToStringCalledAfterSetName_thenReturnsUpdatedName() {
        // Arrange
        String initialName = "John";
        String newName = "Jane";
        Name name = new Name(initialName);

        // Act
        name.setName(newName);
        String actualName = name.toString();

        // Assert
        assertEquals(newName, actualName);
    }


    // ==========================================
    // Edge Case Tests
    // ==========================================


    @Test
    @DisplayName("When very long name is provided, then Name is instantiated")
    void whenVeryLongName_thenNameIsInstantiated() {
        // Arrange
        String longName = "A".repeat(150); // Maximum length according to column definition

        // Act
        Name name = new Name(longName);

        // Assert
        assertNotNull(name);
        assertEquals(longName, name.toString());
    }

    @Test
    @DisplayName("When name with leading and trailing spaces is provided, then Name is instantiated")
    void whenNameWithLeadingAndTrailingSpaces_thenNameIsInstantiated() {
        // Arrange
        String nameWithSpaces = "  John Doe  ";

        // Act
        Name name = new Name(nameWithSpaces);

        // Assert
        assertNotNull(name);
        assertEquals(nameWithSpaces, name.toString());
    }

    @Test
    @DisplayName("When name is exactly at boundary length, then Name is instantiated")
    void whenNameAtBoundaryLength_thenNameIsInstantiated() {
        // Arrange
        String boundaryName = "A".repeat(149) + "B"; // 150 chars

        // Act
        Name name = new Name(boundaryName);

        // Assert
        assertNotNull(name);
        assertEquals(150, name.toString().length());
    }
}
