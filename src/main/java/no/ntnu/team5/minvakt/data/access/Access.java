package no.ntnu.team5.minvakt.data.access;

/**
 * Created by alan on 12/01/2017.
 */

public abstract class Access<T> {
    protected DbAccess db;

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
