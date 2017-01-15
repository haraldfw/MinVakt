package no.ntnu.team5.minvakt.data.access;

import no.ntnu.team5.minvakt.db.Notification;
import org.hibernate.Query;

import java.util.List;

/**
 * Created by gards on 12-Jan-17.
 */

public class NotificationAccess extends Access<Notification> {
    protected NotificationAccess(DbAccess db) {
        super(db);
    }

    public List<Notification> fromUsername(String username) {
        return db.transaction(session -> {
            Query query = session.createQuery("from Notification noti where noti.user.username = :username and expiry >= current_date()");
            query.setParameter("username", username);
            return (List<Notification>) query.list();
        });
    }
}
