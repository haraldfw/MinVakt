package no.ntnu.team5.minvakt.controllers;

import no.ntnu.team5.minvakt.dataaccess.ShiftAccess;
import no.ntnu.team5.minvakt.dataaccess.UserAccess;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.models.ShiftModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by alan on 11/01/2017.
 */

@RestController
@RequestMapping("/shift")
public class ShiftController {
    @Autowired
    UserAccess userAccess;

    @Autowired
    ShiftAccess shiftAccess;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Integer> register(ShiftModel reg){
        User user = userAccess.fromID(reg.getUserId());
        Shift shift = new Shift(user, reg.getStart(), reg.getEnd(), reg.getAbsent(), reg.getStandardHours());

        shiftAccess.save(shift);
        return ResponseEntity.ok().body(shift.getId());
    }
}

