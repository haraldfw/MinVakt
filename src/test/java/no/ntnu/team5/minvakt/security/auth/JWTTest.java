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

        String token = JWT.generate(user, false);

        Optional<Claims> ok = JWT.verify(token);
        Assert.assertTrue(ok.isPresent());


        Optional<Claims> fail = JWT.verify("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImNvbXBldGFuY2UiOlsidGVzdEMxIiwidGVzdEMyIl0sImV4cCI6MTQ4NDkwMjE4OH0.m4e2l4fNQYz1YnaeKzD_0oVD0qFCptt00pbm18yxhaKwfOWIVtEJ7LeHfMhLpTxsBX-wb4mIidICNiKysyjzvw");
        Assert.assertFalse(fail.isPresent());
    }
}
