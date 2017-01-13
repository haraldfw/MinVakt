package no.ntnu.team5.minvakt.security.jwt;

import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by alan on 12/01/2017.
 */
public class HasCompetance {
    public Claims oneOf(String token, String ...competances){
        return JWT.hasAccess(token, (claims, user) -> {
            List<String> user_competance = (List<String>) claims.get("competance");

            return Stream.of(competances).anyMatch(user_competance::contains);
        });
    }

    public Claims allOf(String token, String ...competances){
        return JWT.hasAccess(token, (claims, user) -> {
            List<String> user_competance = (List<String>) claims.get("competance");

            return Stream.of(competances).allMatch(user_competance::contains);
        });
    }
}
