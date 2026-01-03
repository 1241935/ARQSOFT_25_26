package pt.psoft.g1.psoftg1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pt.psoft.g1.psoftg1.shared.services.IdGenerator;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PsoftG1ApplicationTests {

    @MockBean
    private IdGenerator idGenerator;

	@Test
	void contextLoads() {
		assertTrue(true);
	}

}
