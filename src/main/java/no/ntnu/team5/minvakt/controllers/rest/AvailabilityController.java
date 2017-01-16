package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.db.Availability;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import org.springframework.beans.factory.annotation.Autowired;
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
    private AccessContextFactory accessor;

    @Authorize
    @RequestMapping()
    public List<Availability> getAllAvailable() {
        //FIXME: Should be admin?
        return accessor.with(access -> {
            return access.availability.listAvailable();
        });
    }
}
