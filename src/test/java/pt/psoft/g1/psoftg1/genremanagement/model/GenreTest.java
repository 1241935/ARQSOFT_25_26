package pt.psoft.g1.psoftg1.genremanagement.model;

import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.genremanagement.factories.GenreFactory;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenreTest {

    @Test
    void ensureGenreMustNotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> new Genre(null,null));
    }

    @Test
    void ensureGenreMustNotBeBlank() {
        assertThrows(IllegalArgumentException.class, () -> new Genre("123",""));
    }


    /**
     * Text from <a href="https://www.lipsum.com/">Lorem Ipsum</a> generator.
     */
    @Test
    void ensureGenreMustNotBeOversize() {
        assertThrows(IllegalArgumentException.class, () -> new Genre("1","\n" +
                "\n" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam venenatis semper nisl, eget condimentum felis tempus vitae. Morbi tempus turpis a felis luctus, ut feugiat tortor mattis. Duis gravida nunc sed augue ultricies tempor. Phasellus ultrices in dolor id viverra. Sed vitae odio ut est vestibulum lacinia sed sed neque. Mauris commodo, leo in tincidunt porta, justo mi commodo arcu, non ultricies ipsum dolor a mauris. Pellentesque convallis vulputate nisl, vel commodo felis ornare nec. Aliquam tristique diam dignissim hendrerit auctor. Mauris nec dolor hendrerit, dignissim urna non, pharetra quam. Sed diam est, convallis nec efficitur eu, sollicitudin ac nibh. In orci leo, dapibus ut eleifend et, suscipit sit amet felis. Integer lectus quam, tristique posuere vulputate sed, tristique eget sem.\n" +
                "\n" +
                "Mauris ac neque porttitor, faucibus velit vel, congue augue. Vestibulum porttitor ipsum eu sem facilisis sagittis. Mauris dapibus tincidunt elit. Phasellus porttitor massa nulla, quis dictum lorem aliquet in. Integer sed turpis in mauris auctor viverra. Suspendisse faucibus tempus tellus, in faucibus urna dapibus at. Nullam dolor quam, molestie nec efficitur nec, bibendum a nunc.\n" +
                "\n" +
                "Maecenas quam arcu, euismod sit amet congue non, venenatis nec ipsum. Cras at posuere metus. Quisque facilisis, sem sit amet vestibulum porta, augue quam semper nulla, eu auctor orci purus vel felis. Fusce ultricies tristique tellus, sed rhoncus elit venenatis id. Aenean in lacus quis ipsum eleifend viverra at at lacus. Nulla finibus, risus ut venenatis posuere, lacus magna eleifend arcu, ut bibendum magna turpis eu lorem. Mauris sed quam eget libero vulputate pretium in in purus. Morbi nec faucibus mi, sit amet pretium tellus. Duis suscipit, tellus id fermentum ultricies, tellus elit malesuada odio, vitae tempor dui purus at ligula. Nam turpis leo, dignissim tristique mauris at, rutrum scelerisque est. Curabitur sed odio sit amet nisi molestie accumsan. Ut vulputate auctor tortor vel ultrices. Nam ut volutpat orci. Etiam faucibus aliquam iaculis.\n" +
                "\n" +
                "Mauris malesuada rhoncus ex nec consequat. Etiam non molestie libero. Phasellus rutrum elementum malesuada. Pellentesque et quam id metus iaculis hendrerit. Fusce molestie commodo tortor ac varius. Etiam ac justo ut lacus semper pretium. Curabitur felis mauris, malesuada accumsan pellentesque vitae, posuere non lacus. Donec sit amet dui finibus, dapibus quam quis, tristique massa. Phasellus velit ipsum, facilisis vel nisi eu, interdum vehicula ante. Nulla eget luctus nunc, nec ullamcorper lectus.\n" +
                "\n" +
                "Curabitur et nisi nisi. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Curabitur ultrices ultrices ante eu vestibulum. Phasellus imperdiet non ex sed rutrum. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Donec consequat mauris sed pulvinar sodales. Quisque a iaculis eros. Donec non tellus eget ligula eleifend posuere. Sed tincidunt, purus id eleifend fringilla, tellus erat tristique urna, at ullamcorper purus turpis ac risus. Maecenas non finibus diam. Aliquam erat volutpat. Morbi ultrices blandit arcu eu dignissim. Duis ac dapibus libero. Ut pretium libero sit amet velit viverra semper. Suspendisse vitae augue dui.\n" +
                "\n" +
                "Aliquam aliquam justo porttitor sapien faucibus sollicitudin. Sed iaculis accumsan urna, id elementum est rhoncus vitae. Maecenas rhoncus ultrices arcu eu semper. Integer pulvinar ultricies purus, sit amet scelerisque dui vehicula vel. Phasellus quis urna ac neque auctor scelerisque eget eget arcu. Sed convallis, neque consectetur venenatis ornare, nibh lorem mollis magna, vel vulputate libero ligula egestas ligula. Curabitur iaculis nisl nisi, ac ornare urna lacinia non. Cras sagittis risus sit amet interdum porta. Nam dictum, neque ut blandit feugiat, tortor libero hendrerit enim, at tempor justo velit scelerisque odio. Fusce a ipsum sit amet ligula maximus pharetra. Suspendisse rhoncus leo dolor, vulputate blandit mi ullamcorper ut. Etiam consequat non mi eu porta. Sed mattis metus fringilla purus auctor aliquam.\n" +
                "\n" +
                "Vestibulum quis mi at lorem laoreet bibendum eu porta magna. Etiam vitae metus a sapien sagittis dapibus et et ex. Vivamus sed vestibulum nibh. Etiam euismod odio massa, ac feugiat urna congue ac. Phasellus leo quam, lacinia at elementum vitae, viverra quis ligula. Quisque ultricies tellus nunc, id ultrices risus accumsan in. Vestibulum orci magna, mollis et vehicula non, bibendum et magna. Pellentesque ut nibh quis risus dignissim lacinia sed non elit. Morbi eleifend ipsum posuere velit sollicitudin, quis auctor urna ullamcorper. Praesent pellentesque non lacus eu scelerisque. Praesent quis eros sed orci tincidunt maximus. Quisque imperdiet interdum massa a luctus. Phasellus eget nisi leo.\n" +
                "\n" +
                "Nunc porta nisi eu dui maximus hendrerit eu quis est. Cras molestie lacus placerat, maximus libero hendrerit, eleifend nisi. Suspendisse potenti. Praesent nec mi ut turpis pharetra pharetra. Phasellus pharetra. "));
    }

    @Test
    void ensureGenreIsSet() {
        final var genre = new Genre("123","Some genre");
        assertEquals("Some genre", genre.toString());
    }

    @Test
    void whenValidParameters_thenGenreIsCreated() {
        // Arrange
        String pk = "1";
        String genreName = "Fiction";

        // Act
        Genre genre = new Genre(pk, genreName);

        // Assert
        assertNotNull(genre);
        assertEquals(pk, genre.getPk());
        assertEquals(genreName, genre.getGenre());
    }

    @Test
    void whenCreateGenreUsingFactory_thenGenreIsCreatedWithGeneratedPk() {
        // Arrange
        String genreName = "Fiction";

        GenreFactory factoryDouble = mock(GenreFactory.class);
        Genre genreDouble = mock(Genre.class);

        when(factoryDouble.create(genreName)).thenReturn(genreDouble);

        // Act
        Genre genre = factoryDouble.create(genreName);

        // Assert
        assertNotNull(genre);
        verify(factoryDouble).create(genreName);
    }

    @Test
    void whenNullGenre_thenThrowsIllegalArgumentException() {
        // Arrange
        String expectedMessage = "Genre cannot be null";
        String pk = "1";

        // Act + Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Genre(pk, null)
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void whenBlankGenre_thenThrowsIllegalArgumentException() {
        // Arrange
        String expectedMessage = "Genre cannot be blank";
        String pk = "1";

        // Act + Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Genre(pk, "")
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void whenBlankGenreWithSpaces_thenThrowsIllegalArgumentException() {
        // Arrange
        String expectedMessage = "Genre cannot be blank";
        String pk = "1";

        // Act + Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Genre(pk, "   ")
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void whenGenreExceedsMaxLength_thenThrowsIllegalArgumentException() {
        // Arrange
        String expectedMessage = "Genre has a maximum of 4096 characters";
        String pk = "1";
        String longGenre = "a".repeat(101);

        // Act + Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Genre(pk, longGenre)
        );

        // Assert
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void whenGenreAtMaxLength_thenGenreIsCreated() {
        // Arrange
        String pk = "1";
        String maxLengthGenre = "a".repeat(100);

        // Act
        Genre genre = new Genre(pk, maxLengthGenre);

        // Assert
        assertNotNull(genre);
        assertEquals(maxLengthGenre, genre.getGenre());
    }

    @Test
    void whenGenreAtMinLength_thenGenreIsCreated() {
        // Arrange
        String pk = "1";
        String minLengthGenre = "a";

        // Act
        Genre genre = new Genre(pk, minLengthGenre);

        // Assert
        assertNotNull(genre);
        assertEquals(minLengthGenre, genre.getGenre());
    }

    @Test
    void whenToString_thenReturnsGenreName() {
        // Arrange
        String pk = "1";
        String genreName = "Fiction";
        Genre genre = new Genre(pk, genreName);

        // Act
        String result = genre.toString();

        // Assert
        assertEquals(genreName, result);
    }

    @Test
    void whenValidGenreWithSpecialCharacters_thenGenreIsCreated() {
        // Arrange
        String pk = "1";
        String genreName = "Science-Fiction & Fantasy";

        // Act
        Genre genre = new Genre(pk, genreName);

        // Assert
        assertNotNull(genre);
        assertEquals(genreName, genre.getGenre());
    }

    @Test
    void whenValidGenreWithNumbers_thenGenreIsCreated() {
        // Arrange
        String pk = "1";
        String genreName = "20th Century Literature";

        // Act
        Genre genre = new Genre(pk, genreName);

        // Assert
        assertNotNull(genre);
        assertEquals(genreName, genre.getGenre());
    }

    @Test
    void whenValidGenreWithAccents_thenGenreIsCreated() {
        // Arrange
        String pk = "1";
        String genreName = "Ficção Científica";

        // Act
        Genre genre = new Genre(pk, genreName);

        // Assert
        assertNotNull(genre);
        assertEquals(genreName, genre.getGenre());
    }

    @Test
    void whenMultipleGenresCreated_thenEachHasUniquePk() {
        // Arrange
        String pk1 = "1";
        String pk2 = "2";
        String genreName1 = "Fiction";
        String genreName2 = "Non-Fiction";

        // Act
        Genre genre1 = new Genre(pk1, genreName1);
        Genre genre2 = new Genre(pk2, genreName2);

        // Assert
        assertNotEquals(genre1.getPk(), genre2.getPk());
        assertEquals(pk1, genre1.getPk());
        assertEquals(pk2, genre2.getPk());
    }

}