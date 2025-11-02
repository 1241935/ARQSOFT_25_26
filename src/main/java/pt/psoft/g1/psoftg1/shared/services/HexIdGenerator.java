package pt.psoft.g1.psoftg1.shared.services;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("hex")
public class HexIdGenerator implements IdGenerator {

    @Override
    public String generateId() {

        long timestamp = System.currentTimeMillis();

        int randomNumber = (int) (Math.random() * 1_000_000); // Random part

        String hexPart = String.format("%05x", randomNumber); // Hex converting

        return timestamp + "-" + hexPart; // Final ID
    }
}
