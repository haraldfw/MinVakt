package no.ntnu.team5.minvakt.security.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import no.ntnu.team5.minvakt.Constants;
import no.ntnu.team5.minvakt.db.Competence;
import no.ntnu.team5.minvakt.db.User;
import org.apache.logging.log4j.LogManager;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by alan on 11/01/2017.
 */

public class JWT {

    private static final int SECONDS_IN_YEAR = 525_600 * 60;
    private static final int SECONDS_IN_30_MIN = 60 * 30;

    static private Date expieryDate(boolean remember) {
        Instant ins;
        if (remember) {
            ins = new Date().toInstant().plusSeconds(SECONDS_IN_YEAR);
        } else {
            ins = new Date().toInstant().plusSeconds(SECONDS_IN_30_MIN);
        }

        return Date.from(ins);
    }

    static public String generate(User user, boolean remember) {
        //TODO: Add some claims(iss, aud)?
        HashMap<String, Object> claims = new HashMap<>();

        claims.put("competance", user.getCompetences()
                .stream()
                .map(Competence::getName)
                .collect(Collectors.toList()));

        claims.put("name", user.getFirstName() + ' ' + user.getLastName());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setExpiration(expieryDate(remember))
                .signWith(SignatureAlgorithm.HS512, Constants.SECURE_KEY)
                .compact();
    }

    static public Optional<Claims> verify(String token) {
        try {
            return Optional.of(Jwts.parser().setSigningKey(Constants.SECURE_KEY).parseClaimsJws(token).getBody());
        } catch (JwtException je) {
            LogManager.getLogger().error(je);
            return Optional.empty();
        }
    }

    public static String refresh(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expieryDate(false))
                .signWith(SignatureAlgorithm.HS512, Constants.SECURE_KEY)
                .compact();
    }
}

