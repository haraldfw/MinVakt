package no.ntnu.team5.minvakt.security.auth.verify;

import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by alan on 14/01/2017.
 */

public class Verifier {
    public final Claims claims;

    public Verifier(Claims claims) {
        this.claims = claims;
    }

    /**
     * A helper method that runs a predicate from a {@see no.ntnu.team5.minvakt.security.auth.verify.Verification}.
     * @param ver The {@see no.ntnu.team5.minvakt.security.auth.verify.Verification} predicate to run.
     * @return Whether the verification predicate was successful.
     */
    public boolean verify(Verification ver) {
        return ver.predicate(claims);
    }

    /**
     * A helper method that runs a predicate from a {@see no.ntnu.team5.minvakt.security.auth.verify.Verification}.
     * @param ver The {@see no.ntnu.team5.minvakt.security.auth.verify.Verification} predicate to run.
     * @return The claims belonging to this {@see no.ntnu.team5.minvakt.security.auth.verify.Verifier} context
     */
    public Claims ensure(Verification ver) {
        if (verify(ver)) {
            return claims;
        } else {
            throw new ForbiddenException();
        }
    }

    /**
     * A logical AND operation for {@code Verifications}.
     * @param verifications The list of {@code Verification}.
     * @return A {@code Verification} whose predicate returns{@code true} if and only if all predicates in {@code verifications} are {@code true}.
     */
    public static Verification and(Verification... verifications) {
        return claims -> Stream.of(verifications).allMatch(ver -> ver.predicate(claims));
    }

    /**
     * A logical OR operation for {@code Verifications}.
     * @param verifications The list of {@code Verification}.
     * @return A {@code Verification} whose predicate returns {@code true} if one predicate in {@code verifications} is {@code true}.
     */
    public static Verification or(Verification... verifications) {
        return claims -> Stream.of(verifications).anyMatch(ver -> ver.predicate(claims));
    }

    /**
     * A {@code Verification} that verifies whether the claim's subject is {@code username}
     * @param username The username to verify is present.
     * @return A {@code Verification} whose predicate returns {@code true} if {@code claims.getSubject()} is equal to {@code username}.
     */
    public static Verification isUser(String username) {
        return claims -> claims.getSubject().equals(username);
    }

    /**
     * A {@code Verification} that verifies whether the claim's roles includes {@code role}
     * @param role The role to ensure is present.
     * @return A {@code Verification} whose predicate returns {@code true} if {@code claims} roles includes {@code role}.
     */
    public static Verification hasRole(String role) {
        return claims -> {
            List<String> user_competance = (List<String>) claims.get("competance");

            return user_competance.contains(role);
        };
    }
}
