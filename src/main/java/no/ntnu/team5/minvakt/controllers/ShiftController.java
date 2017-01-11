package no.ntnu.team5.minvakt.controllers;

import no.ntnu.team5.minvakt.dataaccess.ShiftAccess;
import no.ntnu.team5.minvakt.dataaccess.UserAccess;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;

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
    public ResponseEntity<Integer> register(RegisterStat reg){
        User user = userAccess.fromID(reg.getUserId());
        Shift shift = new Shift(user, reg.getStart(), reg.getEnd(), reg.getAbsent(), reg.getStandardHours());

        shiftAccess.save(shift);
        return ResponseEntity.ok().body(shift.getId());
    }
}

class RegisterStat{
    private int userId;
    private Date start;
    private Date end;
    private byte absent;
    private byte standardHours;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public byte getAbsent() {
        return absent;
    }

    public void setAbsent(byte absent) {
        this.absent = absent;
    }

    public byte getStandardHours() {
        return standardHours;
    }

    public void setStandardHours(byte standardHours) {
        this.standardHours = standardHours;
    }
}