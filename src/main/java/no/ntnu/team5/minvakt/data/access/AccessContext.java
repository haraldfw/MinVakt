package no.ntnu.team5.minvakt.data.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by alan on 15/01/2017.
 */

@Component
@Scope("prototype")
public class AccessContext {
    @Autowired
    protected DbAccess db;

    @Autowired
    public UserAccess user;

    @Autowired
    public ShiftAccess shift;

    @Autowired
    public NotificationAccess notification;

    @Autowired
    public CompetenceAccess competence;

    @Autowired
    public AvailabilityAccess availability;

    @Autowired
    public ActionURLAccess actionURL;

    public void init() {
        user.setDb(db);
        shift.setDb(db);
        actionURL.setDb(db);
        competence.setDb(db);
        availability.setDb(db);
        notification.setDb(db);
    }

    public void close() {
        db.close();
    }
}
