package no.ntnu.team5.minvakt.controllers.rest;

import no.ntnu.team5.minvakt.data.access.ShiftAccess;
import no.ntnu.team5.minvakt.data.access.UserAccess;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.ShiftModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}

