package no.ntnu.team5.minvakt.security.auth.verify;

import io.jsonwebtoken.Claims;

/**
 * Created by alan on 13/01/2017.
 */

@FunctionalInterface
public interface Verification {
    /**
     * Some predicate that indicates whether the {@code Verification} was successful.
     * @param claims Some claims the predicate can make its decision base on.
     * @return Whether the {@code Verification} was successful.
     */
    boolean predicate(Claims claims);
}