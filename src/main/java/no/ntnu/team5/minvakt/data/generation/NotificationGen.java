package no.ntnu.team5.minvakt.data.generation;

import no.ntnu.team5.minvakt.data.access.Access;
import no.ntnu.team5.minvakt.data.access.ShiftAccess;
import no.ntnu.team5.minvakt.db.Notification;
import no.ntnu.team5.minvakt.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by gards on 13-Jan-17.
 */

@Component
@Scope("singleton")
public class NotificationGen extends Access<Notification>{

    /*
     * Du har fått en forespørsel om å bytte følgende vakt med Per Rompenes:
     * id: 69 -> Fredag 13/01/17 07:00 til 15:00
     * _Godta_  _Avslå_
     */

    /*
     * Du har fått en forespørsel om å ta følgende vakt:
     * id: 70 -> Fredag 13/01/17 18:00 til 02:00
     * _Godta_  _Avslå_
     */

    @Autowired
    ShiftAccess shiftAccess;

    public void generateMessageNotification(User toUser, String message){
        Notification notification = new Notification(message);
        notification.setUser(toUser);
        Date date = Calendar.getInstance().getTime(); //Today
        long plusOneWeek = date.getTime() + 7000000L;
        date.setTime(plusOneWeek);
        notification.setExpiry(date);

        save(notification);
    }

    public void generateTransferNotification(User toUser, String message, String actionURL, int shiftID){
        Notification notification = new Notification(message);
        notification.setUser(toUser);
        notification.setActionUrl(actionURL);
        Date expiry = new Date();
        expiry.setTime(shiftAccess.getShiftFromId(shiftID).getEndTime().getTime() + 1000000L); // 1 dag etter skiftet er over
        notification.setExpiry(expiry);

        save(notification);
    }
}
