package no.ntnu.team5.minvakt.data.access;

/**
 * Created by alan on 12/01/2017.
 */

public abstract class Access<T> {
    private DbAccess db;

    protected DbAccess getDb() {
        if (db == null) {
            throw new RuntimeException("You need to use this object through a AccessContext, you can get one from AccessContextFactory. See UserController for an example.");
        }

        return db;
    }

    public boolean save(T t){
        db.transaction(session -> {
            session.save(t);
            session.flush();
        });

        return true; //FIXME(erl)
    }

    public void setDb(DbAccess db) {
        this.db = db;
    }
}
