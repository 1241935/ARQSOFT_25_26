package pt.psoft.g1.psoftg1.shared.services;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("base65")
public class Base65IdGenerator implements IdGenerator {

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_+";
    private static final int BASE = ALPHABET.length();

    @Override
    public String generateId() {


            long timestamp = System.currentTimeMillis();
            StringBuilder sb = new StringBuilder();

            //Parte do timestamp (ex: 20251020112345)
            sb.append(timestamp);

            //Parte aleatória: 6 dígitos em base65
            long randomPart = (long) (Math.random() * Math.pow(BASE, 6));
            sb.append('-').append(encodeBase65(randomPart, 6));

            return sb.toString();


    }

    private String encodeBase65(long value, int length) {
        char[] buf = new char[length];
        for (int i = length - 1; i >= 0; i--) {
            buf[i] = ALPHABET.charAt((int) (value % BASE));
            value /= BASE;
        }
        return new String(buf);
    }
}
