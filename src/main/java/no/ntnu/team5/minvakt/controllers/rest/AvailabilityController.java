package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.model.AvailabilityModel;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<AvailabilityModel> getAllAvailable() {
        //FIXME: Should be admin?
        return accessor.with(access -> {
            return access.availability.listAvailable()
                    .stream()
                    .map(access.availability::toModel)
                    .collect(Collectors.toList());
        });
    }

    @RequestMapping("/{user}/week")
    public List<AvailabilityModel> getAvailibilityCurrentWeek(@PathVariable("user") String username) {
        return accessor.with(access -> {
            return access.availability.getAllCurrentWeekForUser(new Date(), username)
                    .stream()
                    .map(access.availability::toModel)
                    .collect(Collectors.toList());
        });
    }

    /**
     * Get availability for a week with a given start date
     *
     * @param username the user that we gets shifts for
     * @param year     startYear
     * @param month    startMonth
     * @param day      startDay
     * @return a list of availability for a week for a user with a given start date
     */

    //@Authorize
    @RequestMapping("/{user}/{year}/{month}/{day}/week")
    public List<AvailabilityModel> getAvailabilityWeek(@PathVariable("user") String username,
                                                       @PathVariable("year") int year,
                                                       @PathVariable("month") int month,
                                                       @PathVariable("day") int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day); //TODO: check samme som ShiftController: getShiftsWeek
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date fromDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date toDate = calendar.getTime();

        return accessor.with(access -> {
            return access.availability.getAvailabilityFromDateToDateForUser(fromDate, toDate, username)
                    .stream()
                    .map(access.availability::toModel)
                    .collect(Collectors.toList());
        });
    }
}
