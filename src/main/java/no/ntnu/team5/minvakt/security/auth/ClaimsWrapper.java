package no.ntnu.team5.minvakt.security.auth;

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
        Object o = claims.get(key);

        if (o == null){
            return false;
        } else {
            return o.equals(value);
        }
    }

    public Object get(String key) {
        return claims.get(key);
    }
}
