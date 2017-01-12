package no.ntnu.team5.minvakt.security.jwt;

import io.jsonwebtoken.Claims;

/**
 * Created by alan on 12/01/2017.
 */
public class ClaimsWrapper {
    Claims claims;

    public ClaimsWrapper(Claims claims){
        this.claims = claims;
    }

    public <T> boolean has(String key, T value){
        return claims.get(key).equals(value);
    }
}
