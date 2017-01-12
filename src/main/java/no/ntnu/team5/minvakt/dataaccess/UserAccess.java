package no.ntnu.team5.minvakt.dataaccess;

import no.ntnu.team5.minvakt.db.User;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public List<User> getUsernames() {
        return db.transaction(session -> {
            Query query = session.createQuery("select user_id, username from User");
            return (List<User>) query.list();
        });
    }

    private String generateUsername(String firstName, String lastName) {
        final int minLength = 5;
        final int maxLength = 12;

        int methodsTried = 0;
        List<User> users = getUsernames();
        if (users.isEmpty()) {
            return generateOneUsername(firstName, lastName, methodsTried);
        }
        String username = users.get(0).getUsername();
        while (usernameExists(username, users)) {
            username = generateOneUsername(firstName, lastName, methodsTried);
            methodsTried++;
        }
        return username;
    }

    private String generateOneUsername(String firstName, String lastName, int attempts) {
        firstName = sanitize(firstName.toLowerCase());
        lastName = sanitize(lastName.toLowerCase());

        // TODO increase fn_chars and ln_chars alternating
        return expandingSearch(firstName, lastName, 1 + attempts, 1 + attempts);
    }

    private String expandingSearch(String firstName, String lastName, int firstName_chars, int lastName_chars) {
        List<String> names = new ArrayList<>();

        String reg = "[ -]";
        names.addAll(Arrays.asList(firstName.split(reg)));
        names.addAll(Arrays.asList(lastName.split(reg)));
        return "";
    }

    private String sanitize(String s) {
        s = s.replace("æ", "a").replace("ø", "o").replace("å", "a");
        s = s.replaceAll("'", "");
        return s;
    }

    private boolean usernameExists(String username, List<User> users) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
