package no.ntnu.team5.minvakt.dataaccess;

import no.ntnu.team5.minvakt.db.User;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by alan on 11/01/2017.
 */

@Component
public class UserAccess {

    @Autowired
    DbAccess db;

    public User fromID(int userId) {
        return db.transaction(session -> {
            Query query =  session.createQuery("from User where id = :id");
            query.setParameter("id", userId);
            return (User) query.uniqueResult();
        });
    }
    public User fromUsername(String username) {
        return db.transaction(session -> {
            Query query = session.createQuery("from user where username = :username");
            query.setParameter("username", username);
            return (User) query.uniqueResult();
        });
    }
}
