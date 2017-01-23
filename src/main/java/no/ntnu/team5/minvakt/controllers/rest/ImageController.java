package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.db.Image;
import no.ntnu.team5.minvakt.model.ImageModel;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @GetMapping("/get/{image_id}")
    public ResponseEntity<String> serveImage(@PathVariable("image_id") int image_id) {
        ImageModel imageModel = accessContextFactory.with(accessContext -> {
            Image img = accessContext.image.getById(image_id);
            if(img == null) {
                return null;
            }
            return accessContext.image.toModel(img);
        });

        if (imageModel == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, imageModel.getContentType())
                .body(imageModel.getB64Content());
    }

}
