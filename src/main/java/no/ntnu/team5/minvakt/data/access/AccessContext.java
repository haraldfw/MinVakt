package no.ntnu.team5.minvakt.data.access;

/**
 * Created by alan on 15/01/2017.
 */
public class AccessContext {
    protected DbAccess db;

    public final UserAccess user;
    public final ShiftAccess shift;
    public final NotificationAccess notification;
    public final CompetenceAccess competence;
    public final AvailabilityAccess availability;

    protected AccessContext() {
        db = new DbAccess();
        user = new UserAccess(db);
        shift = new ShiftAccess(db);
        notification = new NotificationAccess(db);
        competence = new CompetenceAccess(db);
        availability = new AvailabilityAccess(db);
    }

    public void close() {
        db.close();
    }
}
