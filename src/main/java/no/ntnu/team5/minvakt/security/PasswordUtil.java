package no.ntnu.team5.minvakt.security;

import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.LoginResponse;
import no.ntnu.team5.minvakt.security.auth.JWT;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.SecureRandom;

/**
 * Created by Kenan on 1/10/2017.
 */
public class PasswordUtil {
    // 512 is the number of bits, SALT_SIZE specifies the number of bytes.
    private static final int SALT_SIZE = 512/8;

    private static final SecureRandom secureRandom = new SecureRandom();

    private PasswordUtil() {
    }

    public static String generateSalt() {
        byte[] salt = new byte[SALT_SIZE];
        secureRandom.nextBytes(salt);
        return Base64.encodeBase64String(salt);
    }

    public static String generatePasswordHash(String password, String salt) {
        return DigestUtils.sha512Hex(salt + password);
    }

    public static boolean verifyPassword(String userInput, String hash, String salt) {
        String inputHashed = generatePasswordHash(userInput, salt);
        return inputHashed.equals(hash);
    }

    public static LoginResponse login(User user, String password) {
        boolean isVerified = PasswordUtil.verifyPassword(password, user.getPasswordHash(), user.getSalt());

        LoginResponse lr = new LoginResponse();
        if (isVerified) {
            lr.setSuccess(true);
            String token = JWT.generate(user);
            lr.setToken(token);
        } else {
            lr.setSuccess(false);
        }

        return lr;
    }
}
