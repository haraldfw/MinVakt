package no.ntnu.team5.minvakt.data.access;

import no.ntnu.team5.minvakt.db.Competence;
import no.ntnu.team5.minvakt.db.Notification;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.NotificationModel;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
        return getDb().transaction(session -> {
            Query query = session.createQuery("from Notification noti where noti.user.username = :username and closed = false");
            query.setParameter("username", username);
            return (List<Notification>) query.list();
        });
    }

    public Notification fromActionURL(String actionUrl) {
        return getDb().transaction(session -> {
            Query query = session.createQuery("from Notification where actionUrl = :action_url and closed = false");
            query.setParameter("action_url", actionUrl);
            return (Notification) query.uniqueResult();
        });
    }

    public List<Notification> fromCompetence(Competence competence) {
        return getDb().transaction(session -> {
            Query query = session.createQuery("from Notification where competence = :competence and closed = false");
            query.setParameter("competence", competence);
            return (List<Notification>) query.list();
        });
    }

    public Notification fromId(int notificationId) {
        return getDb().transaction(session -> {
            Query query = session.createQuery("from Notification where id = :notification_id and closed = false");
            query.setParameter("notification_id", notificationId);
            return (Notification) query.uniqueResult();
        });
    }

    public List<NotificationModel> convertToModel(List<Notification> notifications){
        List<NotificationModel> notificationModels = new ArrayList<>();
        for (Notification n: notifications){
            NotificationModel notificationModel = new NotificationModel();
            notificationModel.setId(n.getId());
            notificationModel.setActionUrl(n.getActionUrl());
            notificationModel.setExpiry(n.getExpiry());
            notificationModel.setMessage(n.getMessage());
            notificationModels.add(notificationModel);
        }
        return notificationModels;
    }

    public void closeNotification(Notification notification) {
        notification.setClosed(true);
        save(notification);
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

    public void generateMessageNotification(Competence competence, String message) {
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
