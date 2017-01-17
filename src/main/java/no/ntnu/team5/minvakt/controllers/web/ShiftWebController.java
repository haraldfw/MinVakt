package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by alan on 17/01/2017.
 */

@Controller
@RequestMapping("/shift")
public class ShiftWebController {
    @Autowired
    private AccessContextFactory accessor;

    @GetMapping
    public String site(Model model) {
        model.addAttribute("shifts", accessor.with(access -> {
            return access.shift.toModel(access.shift.getAllCurrentMonth());
        }));

        return "schedule";
    }
}
