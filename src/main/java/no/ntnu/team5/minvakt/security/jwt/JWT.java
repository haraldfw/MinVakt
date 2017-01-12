package no.ntnu.team5.minvakt.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import no.ntnu.team5.minvakt.dataaccess.wrapper.UserLookup;
import no.ntnu.team5.minvakt.db.User;
import org.apache.commons.codec.binary.Base64;

import java.security.SecureRandom;
import java.util.function.BiFunction;

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

    static public Claims verify(String token){
        return Jwts.parser().setSigningKey(SECURE_KEY).parseClaimsJws(token).getBody();
    }

    public static Claims hasAccess(String token, BiFunction<ClaimsWrapper, UserLookup, Boolean> predicate) throws ForbiddenException {
        Claims claims;
        try {
             claims = verify(token);
        } catch (SignatureException se){
            throw new ForbiddenException();
        }

        if (predicate.apply(new ClaimsWrapper(claims), new UserLookup((String) claims.get("sub")))){
            // User supplied predicate eq true
            return claims;
        } else {
            throw new ForbiddenException();
        }
    }

    public static Claims isUser(String token, String username){
        return hasAccess(token, (claims, user) -> claims.has("sub", username));
    }
}

