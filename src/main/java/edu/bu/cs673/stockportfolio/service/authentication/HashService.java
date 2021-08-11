package edu.bu.cs673.stockportfolio.service.authentication;

import org.fissore.slf4j.FluentLogger;
import org.fissore.slf4j.FluentLoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**********************************************************************************************************************
 * Generate a unique value for the users password that can be safely persisted. For simplicity, 
 * this services uses the SHA-1 cryptographic hash function and 5000 rounds to produce 
 * a 128 bit hashed value.
 *
 *********************************************************************************************************************/
@Component
public class HashService {
    private final FluentLogger log = FluentLoggerFactory.getLogger(HashService.class);

    /**
     * <h3>Generates a hash value</h3>
     * 
     * <p>This method generates a hash value based on the data and a salt value.</p>
     * 
     * @param data the data to be hashed (ie.password).
     * @param salt the salt value - the randomly generated string.
     * @return the hash value.
     */
    public String getHashedValue(String data, String salt) {
        byte[] hashedValue = null;

        KeySpec keySpec = new PBEKeySpec(
                data.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), 5000, 128);

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hashedValue = factory.generateSecret(keySpec).getEncoded();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            log.error().log(e.getMessage());
        }

        return Base64.getEncoder().encodeToString((hashedValue));
    }
}
