package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.db.Image;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.ImageModel;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Harald Floor Wilhelmsen on 20.01.2017.
 */
@RestController
@RequestMapping(value = "/api/images")
public class ImageController {

    @Autowired
    private AccessContextFactory accessContextFactory;

    @Authorize
    @PostMapping("/upload")
    public String imageUpload(@RequestBody ImageModel imageModel,
                              Verifier verifier) {
        System.out.println("Content:");
        System.out.println(imageModel.getB64Content());
        if (imageModel.getB64Content().length() != 0) {

            Image image;
            image = new Image(imageModel.getB64Content(), imageModel.getContentType());
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
