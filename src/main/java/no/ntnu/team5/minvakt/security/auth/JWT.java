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

    /**
     * Generate a date - 30 minutes if {@code remember} is {@code false} otherwise 1 year - in the future,
     * @param remember
     * @return
     */
    static private Date expieryDate(boolean remember) {
        Instant ins;
        if (remember) {
            ins = new Date().toInstant().plusSeconds(SECONDS_IN_YEAR);
        } else {
            ins = new Date().toInstant().plusSeconds(SECONDS_IN_30_MIN);
        }

        return Date.from(ins);
    }

    /**
     * Generates a new JWT token
     * @param user The user to generate a token for
     * @param remember Whether the user wishes to be remembered in the future or not.
     * @return The signed JWT token string
     */
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

    /**
     * Tries to verify a JWT token with out secret, if verification failes the error is logged and an empty {@see java.util.Optional} is returned
     * @param token The string to verify
     * @return {@see java.util.Optional} with the claims made by the now verified token; if token is not verified the {@see java.util.Optional} is empty
     */
    static public Optional<Claims> verify(String token) {
        try {
            return Optional.of(Jwts.parser().setSigningKey(Constants.SECURE_KEY).parseClaimsJws(token).getBody());
        } catch (JwtException je) {
            LogManager.getLogger().error(je);
            return Optional.empty();
        }
    }

    /**
     * Refreshes the expiry of an existing token.
     * @param claims The claims made by another token that will be included in the new token
     * @return The new signed token with all claims made by {@code claims} and a expiry 30 minutes in the future.
     */
    public static String refresh(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expieryDate(false))
                .signWith(SignatureAlgorithm.HS512, Constants.SECURE_KEY)
                .compact();
    }
}

