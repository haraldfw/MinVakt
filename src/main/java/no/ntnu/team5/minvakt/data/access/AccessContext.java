package no.ntnu.team5.minvakt.data.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by alan on 15/01/2017.
 */

/**
 * A context where one can use all access sub-classes, the sub-classes
 * are initialized with the same {@see DbAccess} object.
 * <p>
 * Instances of this class can be retrieved from {@see AccessContextFactory}.
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
    public ImageAccess image;

    @Autowired
    public NotificationAccess notification;

    @Autowired
    public CompetenceAccess competence;

    @Autowired
    public AvailabilityAccess availability;

    void init() {
        user.setDb(db);
        shift.setDb(db);
        competence.setDb(db);
        availability.setDb(db);
        notification.setDb(db);
        image.setDb(db);

        user.setContext(this);
        shift.setContext(this);
        competence.setContext(this);
        availability.setContext(this);
        notification.setContext(this);
        image.setContext(this);
    }

    /**
     * Direct access to the underlying {@see DbAccess} object.
     *
     * @return The {@see DbAccess} object
     */
    public DbAccess getDb() {
        return db;
    }

    /**
     * Close the underlying {@see DbAccess} object, must be called when done with the context.
     */
    public void close() {
        db.close();
    }
}
