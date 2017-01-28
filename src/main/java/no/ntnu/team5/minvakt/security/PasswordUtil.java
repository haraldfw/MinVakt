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
    private static final int SALT_SIZE = 512 / 8;

    private static final SecureRandom secureRandom = new SecureRandom();

    private PasswordUtil() {
    }

    /**
     * Generates a random string in base64
     *
     * @return The b64-encoded string
     */
    public static String generateSalt() {
        byte[] salt = new byte[SALT_SIZE];
        secureRandom.nextBytes(salt);
        return Base64.encodeBase64String(salt);
    }

    /**
     * Generates a sha512 hash from the given password and salt
     *
     * @param password Password to hash
     * @param salt     Salt to add to password
     * @return The hashed password
     */
    public static String generatePasswordHash(String password, String salt) {
        return DigestUtils.sha512Hex(salt + password);
    }

    /**
     * Ensures that the given password-hash matches the given password and salt
     *
     * @param userInput Password entered by user
     * @param hash      Password-hash from database
     * @param salt      Salt from database
     * @return
     */
    public static boolean verifyPassword(String userInput, String hash, String salt) {
        String inputHashed = generatePasswordHash(userInput, salt);
        return inputHashed.equals(hash);
    }

    /**
     * Generates a response-object from the given credentials
     *
     * @param user     User to check if password matches with
     * @param password Password to check match with
     * @param remember If the user ticked the "Remember me" box
     * @return The response object
     */
    public static LoginResponse login(User user, String password, boolean remember) {
        boolean isVerified = user != null && PasswordUtil.verifyPassword(password, user.getPasswordHash(), user.getSalt());

        LoginResponse lr = new LoginResponse();
        if (isVerified) {
            lr.setSuccess(true);
            String token = JWT.generate(user, remember);
            lr.setToken(token);
        } else {
            lr.setSuccess(false);
        }

        return lr;
    }

    /**
     * Sets the password-hash for the given user with the given password.
     *
     * @param user     User to set password for
     * @param password Password to set for the user
     * @return True if password is 8 or more chars.
     */
    public static boolean setPassword(User user, String password) {
        if (password.length() < 8) return false;

        String newSalt = PasswordUtil.generateSalt();
        String newHash = PasswordUtil.generatePasswordHash(password, newSalt);

        user.setSalt(newSalt);
        user.setPasswordHash(newHash);

        return true;
    }
}
