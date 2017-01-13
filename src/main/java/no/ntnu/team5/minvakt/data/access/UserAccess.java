package no.ntnu.team5.minvakt.data.access;

import no.ntnu.team5.minvakt.db.Competence;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.UserModel;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by alan on 11/01/2017.
 */

@Component
@Scope("singleton")
public class UserAccess extends Access<User> {
    public User fromID(int userId) {
        return db.transaction(session -> {
            Query query = session.createQuery("from User where id = :id");
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

    public List<String> getUsernames() {
        return db.transaction(session -> {
            return session.createQuery("select username from User").list();
        });
    }

    public UserModel toModel(User user) {
        UserModel model = new UserModel();
        model.setId(user.getId());
        model.setUsername(user.getUsername());
        model.setFirstName(user.getFirstName());
        model.setLastName(user.getLastName());
        model.setEmail(user.getEmail());
        model.setPhonenumber(user.getPhonenumber());
        model.setEmploymentPercentage(user.getEmploymentPercentage());
        model.setCompetances(
                user.getCompetences().stream()
                        .map(Competence::getName)
                        .collect(Collectors.toList()));

        return model;
    }
}
