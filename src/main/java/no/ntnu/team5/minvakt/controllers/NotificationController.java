package no.ntnu.team5.minvakt.controllers;

import io.jsonwebtoken.Claims;
import no.ntnu.team5.minvakt.data.access.NotificationAccess;
import no.ntnu.team5.minvakt.db.Notification;
import no.ntnu.team5.minvakt.security.jwt.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by gards on 12-Jan-17.
 *
 */

@RestController
@RequestMapping(value = "/notifications")
public class NotificationController {
    @Autowired
    NotificationAccess notificationAccess;

    @RequestMapping(method = RequestMethod.GET)
    public Model notification(Model model, @CookieValue("access_token") String token){
        Claims claims = JWT.verify(token);
        List<Notification> notifications = notificationAccess.fromUsername(claims.getSubject());
        model.addAttribute("notifications", notifications);
        return model;
    }
}
