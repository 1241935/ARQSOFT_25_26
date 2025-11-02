package pt.psoft.g1.psoftg1.shared.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HexIdGeneratorTest {

    private HexIdGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new HexIdGenerator();
    }

    @Test
    @DisplayName("Gera IDs no formato esperado: timestamp-hexadecimal")
    void generatesIdWithCorrectFormat() {
        String id = generator.generateId();

        assertTrue(id.matches("\\d{13,}-[0-9a-f]{5}"),
                "O ID deve ter formato <timestamp>-<5 caracteres hexadecimais>");
    }

    @Test
    @DisplayName("Cada ID gerado deve ser único")
    void generatesUniqueIds() {
        String id1 = generator.generateId();
        String id2 = generator.generateId();

        assertNotEquals(id1, id2, "IDs consecutivos não devem ser iguais");
    }

    @Test
    @DisplayName("A parte hexadecimal deve ter sempre 5 caracteres válidos (0-9, a-f)")
    void hexPartHasValidLengthAndChars() {
        for (int i = 0; i < 1000; i++) {
            String id = generator.generateId();
            String hexPart = id.substring(id.indexOf('-') + 1);

            assertEquals(5, hexPart.length(), "A parte hexadecimal deve ter 5 caracteres");
            assertTrue(hexPart.matches("[0-9a-f]{5}"), "A parte hexadecimal contém caracteres inválidos");
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
    @DisplayName("A parte hexadecimal deve representar um número entre 0 e F423F (999999 decimal)")
    void hexPartWithinValidRange() {
        for (int i = 0; i < 500; i++) {
            String id = generator.generateId();
            String hexPart = id.substring(id.indexOf('-') + 1);

            int value = Integer.parseInt(hexPart, 16);
            assertTrue(value >= 0 && value <= 999_999, "O valor hexadecimal deve estar dentro do intervalo esperado");
        }
    }
}
