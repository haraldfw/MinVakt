package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.model.ImageUpload;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Harald Floor Wilhelmsen on 20.01.2017.
 */
@Controller
public class ImageWebController {

    @Authorize("/")
    @GetMapping("/images/upload")
    public String show(Model model) {
         model.addAttribute("uploadModel", new ImageUpload());
         return "upload_form";
    }
}
