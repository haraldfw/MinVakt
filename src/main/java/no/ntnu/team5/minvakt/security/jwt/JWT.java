package no.ntnu.team5.minvakt.security.jwt;

import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import no.ntnu.team5.minvakt.dataaccess.wrapper.UserLookup;
import no.ntnu.team5.minvakt.db.User;
import org.apache.commons.codec.binary.Base64;

import java.security.SecureRandom;
import java.time.Instant;
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

    static private java.util.Date expieryDate(){
        Instant ins = new java.util.Date().toInstant().plusSeconds(60 * 30);
        java.util.Date date = java.util.Date.from(ins);

        System.out.println(date);
        return java.util.Date.from(ins);
    }

    static public String generate(User user){
        //TODO: Add some claims(iss, aud, roles)?
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(expieryDate())
                .signWith(SignatureAlgorithm.HS512, SECURE_KEY)
                .compact();
    }

    static public Claims verify(String token){
        try {
            return Jwts.parser().setSigningKey(SECURE_KEY).parseClaimsJws(token).getBody();
        } catch (ClaimJwtException ce){
            throw new ForbiddenException();
        }
    }

    public static Claims hasAccess(String token, BiFunction<ClaimsWrapper, UserLookup, Boolean> predicate) {
        Claims claims = verify(token);

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

