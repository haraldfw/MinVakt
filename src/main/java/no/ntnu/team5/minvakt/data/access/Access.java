package no.ntnu.team5.minvakt.data.access;

import org.hibernate.HibernateException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by alan on 12/01/2017.
 */

public abstract class Access<T, M> {
    private DbAccess db;
    private AccessContext context;

    abstract M toModel(T t);

    /**
     * Converts a list of Objects to their model equivalent using the object's toModel method.
     *
     * @param list List to convert
     * @return A list to the database-object's equivalent model object.
     */
    public List<M> toModel(Collection<T> list) {
        return list.stream().map(this::toModel).collect(Collectors.toList());
    }

    protected AccessContext getContext() {
        if (context == null) {
            illegalState();
        }

        return context;
    }

    protected DbAccess getDb() {
        if (db == null) {
            illegalState();
        }

        return db;
    }

    private void illegalState() {
        throw new IllegalStateException("You need to use this object through a AccessContext, you can get one from AccessContextFactory. See UserController for an example.");
    }

    /**
     * Saves the object to the database
     *
     * @param t object to save
     * @return True if success
     */
    public boolean save(T t) {
        try {
            getDb().transaction(session -> {
                session.save(t);
                session.flush();
            });
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Deletes an object from the database
     *
     * @param t object to delete
     * @return True if success
     */
    public boolean delete(T t) {
        try {
            getDb().transaction(session -> {
                session.delete(t);
                session.flush();
            });
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    void setDb(DbAccess db) {
        this.db = db;
    }

    void setContext(AccessContext context) {
        this.context = context;
    }
}
