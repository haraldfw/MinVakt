package no.ntnu.team5.minvakt.data.access;

import no.ntnu.team5.minvakt.db.User;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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

    public List<String> getUsernames() {
        return db.transaction(session -> {
            return session.createQuery("select username from User").list();
        });
    }

    public String generateUsername(String firstName, String lastName) {

        List<String> usernames = getUsernames();
        String username;

        int attempts = 0;
        do {
            username = generateOneUsername(firstName, lastName, attempts);
            attempts++;
        } while (usernames.contains(username));
        return username;
    }

    private String generateOneUsername(String firstName, String lastName, int attempts) {
        firstName = sanitize(firstName.toLowerCase());
        lastName = sanitize(lastName.toLowerCase());

        final int fnInitialChars = 2;
        final int lnInitialChars = 2;

        return expandingSearch(
                firstName,
                lastName,
                attempts < fnInitialChars ? fnInitialChars - attempts : 1 + attempts,
                lnInitialChars + attempts);
    }

    private String expandingSearch(String firstName, String lastName, int firstName_chars, int lastName_chars) {
        String reg = "[ -]";
        firstName = firstName.replaceAll(reg, "");
        lastName = lastName.replaceAll(reg, "");

        return firstName.substring(0, firstName_chars) + lastName.substring(0, lastName_chars);
    }

    private String sanitize(String s) {
        s = s.replace("æ", "a").replace("ø", "o").replace("å", "a");
        s = s.replaceAll("'", "");
        return s;
    }
}
