package no.ntnu.team5.minvakt.dataaccess;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by alan on 12/01/2017.
 */
public abstract class Access<T> {
    @Autowired
    protected DbAccess db;

    public boolean save(T t){
        db.transaction(session -> {
            session.save(t);
            session.flush();
        });

        return true; //FIXME(erl)
    }
}
