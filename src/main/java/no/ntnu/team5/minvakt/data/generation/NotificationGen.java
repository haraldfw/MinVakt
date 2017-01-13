package no.ntnu.team5.minvakt.data.generation;

import no.ntnu.team5.minvakt.data.access.Access;
import no.ntnu.team5.minvakt.db.Notification;
import no.ntnu.team5.minvakt.db.User;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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


    public boolean generateNotification(User toUser){

        Notification notification = new Notification(toUser,
                "God jul",
                null,
                new Date(20170115000000L));



        return save(notification);
    }
}
