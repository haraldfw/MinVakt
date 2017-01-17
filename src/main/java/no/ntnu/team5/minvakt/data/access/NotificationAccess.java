package no.ntnu.team5.minvakt.data.access;

import no.ntnu.team5.minvakt.db.Competence;
import no.ntnu.team5.minvakt.db.Notification;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.db.User;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
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

    public Notification fromActionURL(String actionUrl) {
        return db.transaction(session -> {
            Query query = session.createQuery("from Notification where actionUrl = :action_url and expiry >= current_date()");
            query.setParameter("action_url", actionUrl);
            return (Notification) query.uniqueResult();
        });
    }

    public List<Notification> fromCompetence(Competence competence) {
        return db.transaction(session -> {
            Query query = session.createQuery("from Notification where competence = :competence and expiry >= current_date()");
            query.setParameter("competence", competence);
            return (List<Notification>) query.list();
        });
    }


    public void generateMessageNotification(User toUser, String message) {
        Notification notification = new Notification(message);
        notification.setUser(toUser);
        Date date = Calendar.getInstance().getTime(); //Today
        long plusOneWeek = date.getTime() + 7000000L;
        date.setTime(plusOneWeek);
        notification.setExpiry(date);

        save(notification);
    }

    public void generateMessageNotification(Competence competence, String message){
        Notification notification = new Notification(message);
        notification.setCompetence(competence);
        Date date = Calendar.getInstance().getTime(); //Today
        long plusOneWeek = date.getTime() + 7000000L;
        date.setTime(plusOneWeek);
        notification.setExpiry(date);

        save(notification);
    }


    public void generateTransferNotification(Competence competence, String message, String actionURL, Shift shift) {
        Notification notification = new Notification(message);
        notification.setCompetence(competence);
        notification.setActionUrl(actionURL);
        Date expiry = new Date();
        expiry.setTime(shift.getEndTime().getTime() + 1000000L); // 1 dag etter skiftet er over
        notification.setExpiry(expiry);

        save(notification);
    }
}
