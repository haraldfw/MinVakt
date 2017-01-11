package no.ntnu.team5.minvakt.models;

import java.sql.Date;

/**
 * Created by Harald Floor Wilhelmsen on 11.01.2017.
 */
public class ShiftModel {
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
