package no.ntnu.team5.minvakt.data.generation;

import no.ntnu.team5.minvakt.data.access.UserAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Harald Floor Wilhelmsen on 13.01.2017.
 */
@Component
@Scope("singleton")
public class UsernameGen {

    @Autowired
    private UserAccess userAccess;

    public String generateUsername(String firstName, String lastName) {

        List<String> usernames = userAccess.getUsernames();
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
