package no.ntnu.team5.minvakt.security.auth;

import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import no.ntnu.team5.minvakt.data.wrapper.UserLookup;
import no.ntnu.team5.minvakt.db.Competence;
import no.ntnu.team5.minvakt.db.User;
import org.apache.commons.codec.binary.Base64;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        //TODO: Add some claims(iss, aud)?
        HashMap<String, Object> claims = new HashMap<>();

        claims.put("competance", user.getCompetences()
                .stream()
                .map(Competence::getName)
                .collect(Collectors.toList()));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setExpiration(expieryDate())
                .signWith(SignatureAlgorithm.HS512, SECURE_KEY)
                .compact();
    }

    static public Claims valid(String token, Verification ver){
        Claims claims = verify(token);

        if (ver.predicate(new ClaimsWrapper(claims), new UserLookup((String) claims.get("sub")))){
            return claims;
        } else {
            throw new ForbiddenException();
        }
    }

    static public Claims valid(String token){
        return verify(token);
    }

    public static Claims and(String token, Verification... verifications){
        Claims claims = verify(token);
        ClaimsWrapper cw = new ClaimsWrapper(claims);
        UserLookup ul = new UserLookup((String) claims.get("sub"));

        if (Stream.of(verifications).allMatch(ver -> ver.predicate(cw, ul))){
            return claims;
        } else {
            throw new ForbiddenException();
        }
    }

    public static Claims or(String token, Verification... verifications){
        Claims claims = verify(token);
        ClaimsWrapper cw = new ClaimsWrapper(claims);
        UserLookup ul = new UserLookup((String) claims.get("sub"));

        if (Stream.of(verifications).anyMatch(ver -> ver.predicate(cw, ul))){
            return claims;
        } else {
            throw new ForbiddenException();
        }
    }

    static public Claims verify(String token){
        try {
            return Jwts.parser().setSigningKey(SECURE_KEY).parseClaimsJws(token).getBody();
        } catch (ClaimJwtException ce){
            throw new ForbiddenException();
        }
    }

    public static Verification isUser(String username){
        return (claims, user) -> claims.has("sub", username);
    }

    public static Verification hasRole(String role){
        return (claims, user) -> {
            List<String> user_competance = (List<String>) claims.get("competance");

            return user_competance.contains(role);
        };
    }
}

