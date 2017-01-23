package no.ntnu.team5.minvakt.data.access;

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

    public boolean save(T t) {
        getDb().transaction(session -> {
            session.save(t);
            session.flush();
        });

        return true; //FIXME(erl)
    }

    public boolean delete(T t) {
        getDb().transaction(session -> {
            session.delete(t);
            session.flush();
        });

        return true; //FIXME(erl)
    }

    void setDb(DbAccess db) {
        this.db = db;
    }

    void setContext(AccessContext context) {
        this.context = context;
    }
}
