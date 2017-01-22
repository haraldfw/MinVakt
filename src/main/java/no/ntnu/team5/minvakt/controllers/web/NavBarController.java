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
        if (verify == null) return null;
        return verify.claims.getSubject();
    }

    @ModelAttribute("name")
    public String name(Verifier verify) {
        if (verify == null) return null;
        return (String) verify.claims.get("name");
    }


    @ModelAttribute("navbar")
    public NavbarModel getNavbarModel(Verifier verify) {
        if (verify == null) return null;

        NavbarModel model = new NavbarModel();
        String username = verify.claims.getSubject();
        model.setUsername(username);

        accessor.with(accessContext -> {
            model.setNotificationModels(accessContext.notification.toModel(accessContext.notification.fromUsername(username)));
            Object id = accessContext.user.getImageIdFromUsername(username);
            if (id != null)
                model.setProfileImageId((Integer) accessContext.user.getImageIdFromUsername(username));
        });

        return model;
    }
}
