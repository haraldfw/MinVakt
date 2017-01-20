package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.db.Image;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import javax.websocket.server.PathParam;

/**
 * Created by Harald Floor Wilhelmsen on 20.01.2017.
 */
@RestController
@RequestMapping(value = "/api/images")
public class ImageController {

    @Autowired
    private AccessContextFactory accessContextFactory;

    @Authorize("/")
    @GetMapping("/get/{image_id}")
    public ResponseEntity<byte[]> serveImage(@PathVariable("image_id") int image_id) {
        Image image = accessContextFactory.with(accessContext -> {
            return accessContext.image.getById(image_id);
        });
        if(image == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        System.out.println("poop");
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, image.getType())
                .body(image.getContent());
    }


    @Authorize("/")
    @PostMapping("/upload")
    public String imageUpload(@RequestParam("file") MultipartFile file,
                              RedirectAttributes redirectAttributes,
                              Verifier verifier) {

        accessContextFactory.with(accessContext -> {
            try {
                Image image = new Image(file.getBytes(), file.getContentType());
                accessContext.image.save(image);
//                User user = accessContext.user.fromUsername(verifier.claims.getSubject());
//                user.setImageId(image.getId());
//                accessContext.user.save(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }
}
