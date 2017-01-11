package no.ntnu.team5.minvakt.encryption;

import org.apache.commons.codec.digest.DigestUtils;
import org.jasypt.digest.StandardStringDigester;
import org.jasypt.salt.RandomSaltGenerator;

/**
 * Created by Kenan on 1/10/2017.
 */
public class PasswordUtil {
    private static final int SALT_SIZE = 512;
    private static final String ENC_ALG = "SHA-2";


    private static final RandomSaltGenerator saltGenerator = new RandomSaltGenerator();

    public static String passwordEncrypterJasypt(String userPassword) {

        StandardStringDigester passwordEncrypter = new StandardStringDigester();
        passwordEncrypter.setAlgorithm(ENC_ALG);
        passwordEncrypter.setIterations(1000);
        passwordEncrypter.setSaltGenerator(saltGenerator);
        passwordEncrypter.setSaltSizeBytes(SALT_SIZE);
        passwordEncrypter.initialize();

        return passwordEncrypter.digest(userPassword);
    }



    public static String generatePasswordHash(String password, String salt) {
        return DigestUtils.sha512Hex(salt + password);
    }

    public static boolean verifyPassword(String userInput, String digest) {
        StandardStringDigester matchChecker = new StandardStringDigester();
        return matchChecker.matches(userInput, digest);
    }

}
