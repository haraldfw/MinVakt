package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.data.access.ShiftAccess;
import no.ntnu.team5.minvakt.data.access.UserAccess;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.ShiftModel;
import no.ntnu.team5.minvakt.security.auth.JWT;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    UserAccess userAccess;

    @Autowired
    ShiftAccess shiftAccess;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Integer> register(@RequestBody ShiftModel shiftModel){
        User user = userAccess.fromID(shiftModel.getUserId());

        Shift shift = new Shift(user, shiftModel.getStartTime(), shiftModel.getEndTime(), shiftModel.getAbsent().byteValue(), shiftModel.getStandardHours().byteValue());

        shiftAccess.save(shift);
        return ResponseEntity.ok().body(shift.getId());
    }
    @RequestMapping(value = "/{year}/{month}/{day}")
    public ResponseEntity<List<Shift>> getShifts(
        @CookieValue("access_token") String token,
        @PathVariable("year") int year,
        @PathVariable("month") int month,
        @PathVariable("day") int day) {

        JWT.valid(token);
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        Date date = cal.getTime();

        return ResponseEntity.ok(shiftAccess.getShiftsOnDate(date));
    }

}

