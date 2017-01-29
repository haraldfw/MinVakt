package no.ntnu.team5.minvakt.data.access;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Harald Floor Wilhelmsen on 10.01.2017.
 */

@Component
@Scope("prototype")
@Transactional
public class DbAccess {

    @Autowired
    SessionFactory sessionFactory;

    private Session sess;

    /**
     * Get the underlying {@see Session} object.
     *
     * @return The {@see Session} object.
     */
    public Session getSession() {
        if (sess == null) {
            sess = sessionFactory.openSession();
        }

        return sess;
    }

    /**
     * A helper method that creates an transaction and calls {@code consumer} with the session.
     *
     * @param consumer The function to run within the transaction.
     * @param <T>      The return type of {@code consumer}.
     * @return The value returned by {@code consumer}.
     */
    public <T> T transaction(Function<Session, T> consumer) {
        Session sess = getSession();
        Transaction tx = sess.beginTransaction();
        T t = consumer.apply(sess);
        tx.commit();
        return t;
    }

    /**
     * A helper method that creates an transaction and calls {@code cunsumer} with the session.
     *
     * @param consumer The function to run within the transaction.
     */
    public void transaction(Consumer<Session> consumer) {
        Session sess = getSession();
        Transaction tx = sess.beginTransaction();
        consumer.accept(sess);
        tx.commit();
    }

    /**
     * Closes the session used by the DbAccess context.
     */
    public void close() {
        if (sess != null) {
            sess.close();
        }
    }
}
