package no.ntnu.team5.minvakt.security.auth;

import no.ntnu.team5.minvakt.data.wrapper.UserLookup;

/**
 * Created by alan on 13/01/2017.
 */

@FunctionalInterface
public interface Verification {
    boolean predicate(ClaimsWrapper claims, UserLookup user);
}
