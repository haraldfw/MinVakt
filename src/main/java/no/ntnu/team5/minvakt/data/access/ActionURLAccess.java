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
@Scope("singleton")
public class ActionURLAccess extends Access<Notification> {
    public Notification fromActionURL(String actionURL) {
        return db.transaction(session -> {
            Query query =  session.createQuery("from Notification where actionURL = :action_URL");
            query.setParameter("action_URL", actionURL);
            return (Notification) query.uniqueResult();
        });
    }
}
