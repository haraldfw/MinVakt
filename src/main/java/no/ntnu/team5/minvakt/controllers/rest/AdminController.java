package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.Constants;
import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.data.access.UserAccess;
import no.ntnu.team5.minvakt.db.Competence;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.model.NewCompetence;
import no.ntnu.team5.minvakt.model.NewShift;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Kenan on 1/20/2017.
 */

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AccessContextFactory accessor;

    UserAccess userAccess;

    @Authorize
    @RequestMapping("/createshift")
    public void createShift(Verifier verify, @ModelAttribute("newShift") NewShift newShift) {
        verify.ensure(Verifier.hasRole(Constants.ADMIN));

        Set<Competence> comps = new HashSet<>();
        newShift.getCompetences().forEach(s -> comps.add(accessor.with(accessContext -> {
            return accessContext.competence.getFromName(s);
        })));

        accessor.with(access -> {
            Shift shift = new Shift();

            shift.setUser(userAccess.fromUsername(newShift.getUserModel().getUsername()));
            shift.setStartTime(newShift.getStartTime());
            shift.setEndTime(newShift.getEndTime());
            shift.setAbsent(newShift.getAbsent());
            shift.setStandardHours((byte) ChronoUnit.HOURS.between(newShift.getStartTime().toInstant(), newShift.getEndTime().toInstant()));
            shift.setCompetences(comps);

            access.shift.save(shift);
        });

    }

    @Authorize
    @RequestMapping("/createcompetence")
    public void createCompetence(Verifier verify, @ModelAttribute("newCompetence") NewCompetence newCompetence) {
        verify.ensure(Verifier.hasRole(Constants.ADMIN));

        accessor.with(access -> {
            Competence competence = new Competence(
                    newCompetence.getName()
            );

            access.competence.save(competence);

        });
    }
}
