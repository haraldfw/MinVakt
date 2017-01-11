package no.ntnu.team5.minvakt.dataaccess;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Harald Floor Wilhelmsen on 10.01.2017.
 */

@Transactional
@Component
public class DbAccess {

    @Autowired
    SessionFactory sessionFactory;

    public Session getSession(){
        return sessionFactory.getCurrentSession();
    }

    public <T> T transaction(Function<Session, T> consumer){
        Session sess = getSession();
        Transaction tx = sess.beginTransaction();
        T t = consumer.apply(sess);
        tx.commit();
        return t;
    }

    public void transaction(Consumer<Session> consumer){
        Session sess = getSession();
        Transaction tx = sess.beginTransaction();
        consumer.accept(sess);
        tx.commit();
    }
}
