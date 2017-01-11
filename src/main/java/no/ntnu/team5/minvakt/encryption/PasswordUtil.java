package no.ntnu.team5.minvakt.encryption;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.jasypt.digest.StandardStringDigester;
import org.jasypt.salt.RandomSaltGenerator;

import java.security.SecureRandom;

/**
 * Created by Kenan on 1/10/2017.
 */
public class PasswordUtil {
    private static final int SALT_SIZE = 512;

    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateSalt() {
        byte[] salt = new byte[SALT_SIZE];
        secureRandom.nextBytes(salt);
        return Base64.encodeBase64String(salt);
    }

    public static String generatePasswordHash(String password, String salt) {
        return DigestUtils.sha256Hex(salt + password);
    }

    public static boolean verifyPassword(String userInput, String hash, String salt) {
        String inputHashed = generatePasswordHash(userInput, salt);
        return inputHashed.equals(hash);
    }
}
