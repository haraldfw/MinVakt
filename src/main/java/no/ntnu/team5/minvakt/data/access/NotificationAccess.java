package no.ntnu.team5.minvakt.data.access;

import no.ntnu.team5.minvakt.db.Notification;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by gards on 12-Jan-17.
 */

@Component
@Scope("prototype")
public class NotificationAccess extends Access<Notification> {
    public List<Notification> fromUsername(String username) {
        return db.transaction(session -> {
            Query query = session.createQuery("from Notification noti where noti.user.username = :username and expiry >= current_date()");
            query.setParameter("username", username);
            return (List<Notification>) query.list();
        });
    }
}
