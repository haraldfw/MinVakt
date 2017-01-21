package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.db.Image;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.ImageUpload;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by Harald Floor Wilhelmsen on 20.01.2017.
 */
@Controller
public class ImageWebController {

    @Autowired
    private AccessContextFactory accessContextFactory;

    @Authorize
    @PostMapping("/api/images/upload")
    public String imageUpload(@RequestParam("file") MultipartFile file,
                              Verifier verifier) {
        if (!file.isEmpty()) {

            Image image;
            try {
                image = new Image(file.getBytes(), file.getContentType());
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/user/profile";
            }
            accessContextFactory.with(accessContext -> {
                accessContext.image.save(image);
                User user = accessContext.user.fromUsername(verifier.claims.getSubject());

                Image prevImage = user.getImage();

                user.setImage(image);
                accessContext.user.save(user);

                if (prevImage != null) {
                    accessContext.image.delete(prevImage);
                }
            });

        }

        return "redirect:/user/profile";
    }
}
