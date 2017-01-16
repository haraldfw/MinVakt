package no.ntnu.team5.minvakt.security.auth.verify;

import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by alan on 14/01/2017.
 */

public class Verifier {
    public Claims claims;

    public Verifier(Claims claims) {
        this.claims = claims;
    }

    public Claims ensure(Verification ver) {
        if (ver.predicate(claims)) {
            return claims;
        } else {
            throw new ForbiddenException();
        }
    }

    public static Verification and(String token, Verification... verifications) {
        return claims -> Stream.of(verifications).allMatch(ver -> ver.predicate(claims));
    }

    public static Verification or(Verification... verifications) {
        return claims -> Stream.of(verifications).anyMatch(ver -> ver.predicate(claims));
    }

    public static Verification isUser(String username) {
        return claims -> claims.getSubject().equals(username);
    }

    public static Verification hasRole(String role) {
        return claims -> {
            List<String> user_competance = (List<String>) claims.get("competance");

            return user_competance.contains(role);
        };
    }
}
