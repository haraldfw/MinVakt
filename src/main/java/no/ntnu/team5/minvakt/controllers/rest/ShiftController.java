package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.data.access.Access;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.ShiftModel;
import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by alan on 11/01/2017.
 */

@RestController
@RequestMapping("/api/shift")
public class ShiftController {
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Integer> register(@RequestBody ShiftModel shiftModel){
        //FIXME: Should authorize as admin?
        int id = Access.with(access -> {
            User user = access.user.fromID(shiftModel.getUserId());
            Shift shift = new Shift(user, shiftModel.getStartTime(), shiftModel.getEndTime(), shiftModel.getAbsent().byteValue(), shiftModel.getStandardHours().byteValue(), null);

            access.shift.save(shift);
            return shift.getId();
        });

        return ResponseEntity.ok().body(id);
    }

    @Authorize
    @RequestMapping("/{year}/{month}/{day}")
    public List<Shift> getShifts(
            @PathVariable("year") int year,
            @PathVariable("month") int month,
            @PathVariable("day") int day) {

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        Date date = cal.getTime();

        return Access.with(access -> {
            return access.shift.getShiftsOnDate(date);
        });
    }
}

