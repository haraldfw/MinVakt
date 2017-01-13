package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.data.access.AvailabilityAccess;
import no.ntnu.team5.minvakt.data.access.ShiftAccess;
import no.ntnu.team5.minvakt.data.access.UserAccess;
import no.ntnu.team5.minvakt.db.Availability;
import no.ntnu.team5.minvakt.security.auth.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Kenan on 1/13/2017.
 */

@RestController
@RequestMapping("/api/available")
public class AvailabilityController {

    @Autowired
    private UserAccess userAccess;

    @Autowired
    private ShiftAccess shiftAccess;

    @Autowired
    private AvailabilityAccess availabilityAccess;

    @RequestMapping()
    public List<Availability> getAllAvailable(
            @CookieValue("acces_token") String token
    ) {
        JWT.valid(token);
        return availabilityAccess.listAvailable();
    }

}
