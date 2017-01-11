package no.ntnu.team5.minvakt.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import no.ntnu.team5.minvakt.db.User;
import org.apache.commons.codec.binary.Base64;

import java.security.SecureRandom;

/**
 * Created by alan on 11/01/2017.
 */
public class JWT {
    private static final int KEY_SIZE = 1024;
    private static final String SECURE_KEY;

    static {
        byte[] salt = new byte[KEY_SIZE];
        new SecureRandom().nextBytes(salt);
        SECURE_KEY = Base64.encodeBase64String(salt);
    }

    static public String generate(User user){
        //TODO: Add some claims(iss, exp, aud, roles)?
        return Jwts.builder()
                .setSubject(user.getUsername())
                .signWith(SignatureAlgorithm.HS512, SECURE_KEY)
                .compact();
    }

    //TODO: verify token


}
