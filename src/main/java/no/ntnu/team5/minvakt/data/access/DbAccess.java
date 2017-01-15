package no.ntnu.team5.minvakt.data.access;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Harald Floor Wilhelmsen on 10.01.2017.
 */

@Transactional
public class DbAccess {

    @Autowired
    SessionFactory sessionFactory;

    private Session sess = sessionFactory.openSession();

    public <T> T transaction(Function<Session, T> consumer){
        Transaction tx = sess.beginTransaction();
        T t = consumer.apply(sess);
        tx.commit();
        return t;
    }

    public void transaction(Consumer<Session> consumer){
        Transaction tx = sess.beginTransaction();
        consumer.accept(sess);
        tx.commit();
    }

    public void close() {
        sess.close();
    }
}
