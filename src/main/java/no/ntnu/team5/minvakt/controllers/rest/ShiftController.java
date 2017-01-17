package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.ShiftModel;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by alan on 11/01/2017.
 */

@RestController
@RequestMapping("/api/shift")
public class ShiftController {
    @Autowired
    private AccessContextFactory accessor;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Integer> register(@RequestBody ShiftModel shiftModel){
        //FIXME: Should authorize as admin?
        int id = accessor.with(access -> {
            User user = access.user.fromID(shiftModel.getUserId());
            Shift shift = new Shift(user, shiftModel.getStartTime(), shiftModel.getEndTime(), shiftModel.getAbsent().byteValue(), shiftModel.getStandardHours().byteValue(), null);

            access.shift.save(shift);
            return shift.getId();
        });

        return ResponseEntity.ok().body(id);
    }

    @Authorize
    @RequestMapping("/{year}/{month}/{day}")
    public List<ShiftModel> getShifts(
            @PathVariable("year") int year,
            @PathVariable("month") int month,
            @PathVariable("day") int day) {

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        Date date = cal.getTime();

        return accessor.with(access -> {
            return access.shift.getShiftsOnDate(date)
                    .stream()
                    .map(access.shift::toModel)
                    .collect(Collectors.toList());
        });
    }
}

