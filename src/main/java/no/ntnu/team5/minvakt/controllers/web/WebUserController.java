package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by alan on 13/01/2017.
 */

@Controller
@RequestMapping("/user")
public class WebUserController {
    @Autowired
    private AccessContextFactory accessor;

    @RequestMapping("/{username}")
    public String show(Verifier verifier, @PathVariable("username") String username) {
        verifier.ensure(Verifier.isUser(username));

        return "profile";
    }

//    @ModelAttribute("user")
//    public List<Variety> populateVarieties() {
//        return this.varietyService.findAll();
//    }
}
