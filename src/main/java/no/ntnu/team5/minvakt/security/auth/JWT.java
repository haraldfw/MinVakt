package no.ntnu.team5.minvakt.security.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import no.ntnu.team5.minvakt.db.Competence;
import no.ntnu.team5.minvakt.db.User;
import org.apache.commons.codec.binary.Base64;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by alan on 11/01/2017.
 */

public class JWT {
    private static final int KEY_SIZE = 1024;
    private static final String SECURE_KEY;
    private static final int SECONDS_IN_YEAR = 525_600 * 60;
    private static final int SECONDS_IN_30_MIN = 60 * 30;

    static {
        byte[] salt = new byte[KEY_SIZE];
        new SecureRandom().nextBytes(salt);
        SECURE_KEY = Base64.encodeBase64String(salt);
    }

    static private Date expieryDate(boolean remember) {
        Instant ins;
        if (remember) {
            ins = new Date().toInstant().plusSeconds(SECONDS_IN_YEAR);
        } else {
            ins = new Date().toInstant().plusSeconds(SECONDS_IN_30_MIN);
        }

        Date date = Date.from(ins);

        System.out.println(date);
        return Date.from(ins);
    }

    static public String generate(User user, boolean remember) {
        //TODO: Add some claims(iss, aud)?
        HashMap<String, Object> claims = new HashMap<>();

        claims.put("competance", user.getCompetences()
                .stream()
                .map(Competence::getName)
                .collect(Collectors.toList()));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setExpiration(expieryDate(remember))
                .signWith(SignatureAlgorithm.HS512, SECURE_KEY)
                .compact();
    }

    static public Optional<Claims> verify(String token) {
        try {
            return Optional.of(Jwts.parser().setSigningKey(SECURE_KEY).parseClaimsJws(token).getBody());
        } catch (JwtException je) {
            return Optional.empty();
        }
    }
}

