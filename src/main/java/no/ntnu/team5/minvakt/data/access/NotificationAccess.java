package no.ntnu.team5.minvakt.data.access;

import no.ntnu.team5.minvakt.Constants;
import no.ntnu.team5.minvakt.db.Competence;
import no.ntnu.team5.minvakt.db.Notification;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.NotificationModel;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by gards on 12-Jan-17.
 */

@Component
@Scope("prototype")
public class NotificationAccess extends Access<Notification, NotificationModel> {
    public List<Notification> fromUsername(String username) {
        return getDb().transaction(session -> {
            Query query = session.createQuery("from Notification noti where (noti.user.username = :username and closed = false)");
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

    public void closeNotification(Notification notification) {
        notification.setClosed(true);
        save(notification);
    }

    public void generateMessageNotification(User toUser, String message) {
        Notification notification = new Notification(message);
        notification.setUser(toUser);
        notification.setClosed(false);

        save(notification);
    }

    public void generateTransferRequestNotification(String message, String actionUrl, User toUser) {
        Notification notification = new Notification(message);
        notification.setUser(toUser);
        notification.setActionUrl(actionUrl);
        notification.setClosed(false);

        save(notification);
    }

    public void generateTransferNotification(Competence competence, String message, String actionURL) {
        Notification notification = new Notification(message);
        notification.setCompetence(competence);
        notification.setActionUrl(actionURL);
        notification.setClosed(false);

        save(notification);
    }

    public void generateReleaseFromShiftRequestNotification(Competence competence, String message, String actionUrl, String redirectUrl){
        Notification notification = new Notification(message);
        notification.setCompetence(competence);
        notification.setActionUrl(actionUrl);
        notification.setRedirectUrl(redirectUrl);
        notification.setClosed(false);

        save(notification);
    }

    @Override
    NotificationModel toModel(Notification notification) {
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setActionUrl(notification.getActionUrl());
        notificationModel.setRedirectUrl(notification.getRedirectUrl());
        notificationModel.setMessage(notification.getMessage());
        notificationModel.setId(notification.getId());
        notificationModel.setTimestamp(notification.getTimestamp());

        return notificationModel;
    }
}
