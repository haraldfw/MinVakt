package no.ntnu.team5.minvakt.security.auth;

import io.jsonwebtoken.Claims;
import no.ntnu.team5.minvakt.db.Competence;
import no.ntnu.team5.minvakt.db.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by alan on 19/01/2017.
 */


public class JWTTest {

    @Test
    public void keyTest() {
        User user = new User();
        user.setUsername("testuser");
        Set<Competence> competences = new HashSet<>();
        competences.add(new Competence("testC1"));
        competences.add(new Competence("testC2"));
        user.setCompetences(competences);

        String token = JWT.generate(user);

        Optional<Claims> ok = JWT.verify(token);
        Assert.assertTrue(ok.isPresent());

        Optional<Claims> fail = JWT.verify("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImNvbXBldGVuY2VzIjpbIkFkbWluIl19.KUFLyJz2mnjlXQKYO4D8GqkISwbsTCcNlxbxHtCKaVc");
        Assert.assertFalse(fail.isPresent());
    }
}
