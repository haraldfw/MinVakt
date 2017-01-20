package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.model.NavbarModel;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Created by alan on 19/01/2017.
 */
public class NavBarController {

    @Autowired
    private AccessContextFactory accessor;

    @ModelAttribute("username")
    public String username(Verifier verify) {
        return verify.claims.getSubject();
    }

    @ModelAttribute("navbar")
    public NavbarModel getNavbarModel(Verifier verify) {
        NavbarModel model = new NavbarModel();
        String username = verify.claims.getSubject();
        model.setUsername(username);

        accessor.with(accessContext -> {
            model.setNotificationModels(accessContext.notification.toModel(accessContext.notification.fromUsername(username)));
        });

        return model;
    }
}
