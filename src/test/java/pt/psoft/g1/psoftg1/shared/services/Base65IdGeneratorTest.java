package pt.psoft.g1.psoftg1.shared.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Base65IdGeneratorTest {

    private Base65IdGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new Base65IdGenerator();
    }

    @Test
    @DisplayName("Gera IDs no formato esperado: timestamp-base65")
    void generatesIdWithCorrectFormat() {
        String id = generator.generateId();

        // Deve corresponder a algo como: 1729437545176-QAZ3jK
        assertTrue(id.matches("\\d{13,}-[0-9A-Za-z\\-_\\+]{6}"),
                "O ID deve ter formato <timestamp>-<6 caracteres base65>");
    }

    @Test
    @DisplayName("Cada ID gerado deve ser único")
    void generatesUniqueIds() {
        String id1 = generator.generateId();
        String id2 = generator.generateId();

        assertNotEquals(id1, id2, "IDs consecutivos não devem ser iguais");
    }

    @Test
    @DisplayName("A parte base65 deve ter sempre 6 caracteres válidos")
    void base65PartHasValidLengthAndChars() {
        for (int i = 0; i < 1000; i++) {
            String id = generator.generateId();
            String base65Part = id.substring(id.indexOf('-') + 1);

            assertEquals(6, base65Part.length(), "A parte base65 deve ter 6 caracteres");

            // Validar que só contém caracteres do alfabeto permitido
            assertTrue(base65Part.matches("[0-9A-Za-z\\-_\\+]{6}"),
                    "A parte base65 contém caracteres inválidos");
        }
    }

    @Test
    @DisplayName("Gera IDs crescentes no tempo (timestamp primeiro)")
    void generatesIncreasingTimestamps() throws InterruptedException {
        String id1 = generator.generateId();
        Thread.sleep(2); // ligeira pausa
        String id2 = generator.generateId();

        long t1 = Long.parseLong(id1.split("-")[0]);
        long t2 = Long.parseLong(id2.split("-")[0]);

        assertTrue(t2 >= t1, "O timestamp deve ser crescente ao longo do tempo");
    }

    @Test
    @DisplayName("EncodeBase65 produz strings válidas e de tamanho correto")
    void encodeBase65ProducesValidString() throws Exception {
        var method = Base65IdGenerator.class.getDeclaredMethod("encodeBase65", long.class, int.class);
        method.setAccessible(true);

        String encoded = (String) method.invoke(generator, 12345L, 6);

        assertEquals(6, encoded.length(), "encodeBase65 deve gerar 6 caracteres");
        assertTrue(encoded.matches("[0-9A-Za-z\\-_\\+]{6}"), "encodeBase65 usa apenas caracteres válidos");
    }
}
