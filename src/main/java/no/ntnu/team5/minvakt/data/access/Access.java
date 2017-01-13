package no.ntnu.team5.minvakt.data.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by alan on 12/01/2017.
 */

@Component
@Scope("singleton")
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
