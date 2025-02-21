package no.ntnu.team5.minvakt.data.access;

import no.ntnu.team5.minvakt.db.Competence;
import no.ntnu.team5.minvakt.db.Image;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.UserModel;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by alan on 11/01/2017.
 */

@Component
@Scope("prototype")
public class UserAccess extends Access<User, UserModel> {
    public User fromID(int userId) {
        return getDb().transaction(session -> {
            Query query = session.createQuery("from User where id = :id");
            query.setParameter("id", userId);
            return (User) query.uniqueResult();
        });
    }

    public User fromUsername(String username) {
        return getDb().transaction(session -> {
            Query query = session.createQuery("from User where username = :username");
            query.setParameter("username", username);
            return (User) query.uniqueResult();
        });
    }

    public List<String> getUsernames() {
        return getDb().transaction(session -> {
            return session.createQuery("select username from User").list();
        });
    }

    public List<String> getFirstNames() {
        return getDb().transaction(session -> {
            return session.createQuery("select firstName from User").list();
        });
    }

    public List<String> getLastNames() {
        return getDb().transaction(session -> {
            return session.createQuery("select lastName from User").list();
        });
    }

    public List<User> getAllContactInfo() {
        return getDb().transaction(session -> {
            return session.createQuery("from User order by firstName asc").list();
        });
    }

    public User getUserFromSecretKey(String username, String resetKey) {
        return getDb().transaction(session -> {
            Query query = session.createQuery(
                    "from User where username = :username and resetKey = :resetKey and :today < resetKeyExpiry");
            query.setParameter("username", username);
            query.setParameter("resetKey", resetKey);
            query.setParameter("today", new Date());
            return (User) query.uniqueResult();
        });
    }

    public UserModel toModel(User user) {
        UserModel model = new UserModel();
        model.setId(user.getId());
        model.setUsername(user.getUsername());
        model.setFirstName(user.getFirstName());
        model.setLastName(user.getLastName());
        model.setEmail(user.getEmail());
        model.setAddress(user.getAddress());
        model.setDateOfBirth(user.getDateOfBirth());
        model.setPhonenumber(user.getPhonenumber());
        model.setEmploymentPercentage(user.getEmploymentPercentage());
        model.setCompetences(
                user.getCompetences().stream()
                        .map(Competence::getName)
                        .collect(Collectors.toList()));

        return model;
    }

    public User fromEmail(String email) {
        return getDb().transaction(session -> {
            Query query = session.createQuery("from User where email = :email");
            query.setParameter("email", email);
            return (User) query.uniqueResult();
        });
    }

    public Image getImageFromUsername(String username) {
        return getDb().transaction(session -> {
            Query query = session.createQuery("select u.image from User u where username = :username");
            query.setParameter("username", username);
            return (Image) query.uniqueResult();
        });
    }
}
