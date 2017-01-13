package no.ntnu.team5.minvakt.data.access;

import no.ntnu.team5.minvakt.db.User;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by alan on 11/01/2017.
 */

@Component
@Scope("singleton")
public class UserAccess extends Access<User> {
    public User fromID(int userId) {
        return db.transaction(session -> {
            Query query =  session.createQuery("from User where id = :id");
            query.setParameter("id", userId);
            return (User) query.uniqueResult();
        });
    }

    public User fromUsername(String username) {
        return db.transaction(session -> {
            Query query = session.createQuery("from User where username = :username");
            query.setParameter("username", username);
            return (User) query.uniqueResult();
        });
    }
}
