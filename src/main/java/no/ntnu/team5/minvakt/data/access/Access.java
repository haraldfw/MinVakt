package no.ntnu.team5.minvakt.data.access;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by alan on 12/01/2017.
 */

public abstract class Access<T> {
    protected DbAccess db;

    protected Access(DbAccess db) {
        this.db = db;
    }

    public boolean save(T t){
        db.transaction(session -> {
            session.save(t);
            session.flush();
        });

        return true; //FIXME(erl)
    }

    public static void with(Consumer<AccessContext> consumer) {
        AccessContext ac = new AccessContext();
        consumer.accept(ac);
        ac.close();
    }

    public static <R> R with(Function<AccessContext, R> consumer) {
        AccessContext ac = new AccessContext();
        R r = consumer.apply(ac);
        ac.close();
        return r;
    }
}
