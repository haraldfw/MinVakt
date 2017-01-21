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

    public static boolean setPassword(User user, String password) {
        if (password.length() < 8) return false;

        String newSalt = PasswordUtil.generateSalt();
        String newHash = PasswordUtil.generatePasswordHash(password, newSalt);

        user.setSalt(newSalt);
        user.setPasswordHash(newHash);

        return true;
    }

//    public static void main(String[] args) {
//        dummyPassword("det");
//        dummyPassword("det1");
//        dummyPassword("det2");
//    }

    private static void dummyPassword(String salt) {
        System.out.println(generatePasswordHash("passord", salt));
    }
}
