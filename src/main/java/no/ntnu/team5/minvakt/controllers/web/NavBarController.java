package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Created by alan on 19/01/2017.
 */
public class NavBarController {

    @ModelAttribute("username")
    public String username(Verifier verify) {
        System.out.println("Heiho " + verify);

        return verify.claims.getSubject();
    }
}
