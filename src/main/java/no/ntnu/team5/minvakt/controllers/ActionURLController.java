package no.ntnu.team5.minvakt.controllers;

import no.ntnu.team5.minvakt.data.access.ActionURLAccess;
import no.ntnu.team5.minvakt.data.access.ShiftAccess;
import no.ntnu.team5.minvakt.db.Notification;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.security.auth.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by gards on 13-Jan-17.
 */

@RestController
@RequestMapping(value="/notifications")
public class ActionURLController {

    @Autowired
    ActionURLAccess actionURLAccess;

    @Autowired
    ShiftAccess shiftAccess;

    @RequestMapping(value="/{actionURL}", method=RequestMethod.POST)
    public boolean getActionURL(@PathVariable String actionURL, @CookieValue("access_token") String token){
        JWT.valid(token, JWT.hasRole("admin"));

        Notification notification = actionURLAccess.fromActionURL(actionURL);
        User newOwner = notification.getUser();

        String[] urlSegments = notification.getActionUrl().split("_"); //EKSEMPEL-URL: localhost:8080/notifications/4t5thj65ed33_69
        int shiftID = Integer.parseInt(urlSegments[1]);                     //Får da 69. hehehehe
        Shift shift = shiftAccess.getShiftFromId(shiftID);

        return shiftAccess.transferOwnership(shift, newOwner);
    }

    /*har bruk for senere:
     * JWT.or(token, JWT.isUser(newOwner.getUsername()), JWT.hasRole("admin"));
     * For å passe en notification til admin
     *
     */
}
