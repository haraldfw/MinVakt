package no.ntnu.team5.minvakt.security.auth.verify;

import io.jsonwebtoken.Claims;

/**
 * Created by alan on 13/01/2017.
 */

@FunctionalInterface
public interface Verification {
    boolean predicate(Claims claims);
}