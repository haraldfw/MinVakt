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
        int firstnameIter = 0;
        int lastnameIter = 0;
        boolean changeFirstname = false;
        do {
            username = expandingGeneration(firstName, lastName, firstnameIter, lastnameIter, changeFirstname);

            if (changeFirstname) {
                firstnameIter++;
            } else {
                lastnameIter++;
            }
            changeFirstname = !changeFirstname;
        } while (usernames.contains(username));
        return username;
    }

    private String expandingGeneration(String firstName, String lastName, int firstnameAdd, int lastnameAdd, boolean changeFirstname) {
        firstName = sanitize(firstName.toLowerCase());
        lastName = sanitize(lastName.toLowerCase());

        final int fnInitialChars = 2;
        final int lnInitialChars = 2;

        int firstNameChars = fnInitialChars + firstnameAdd;
        int lastNameChars = lnInitialChars + lastnameAdd;

        // firstnamechars + lastnamechars
        return firstName.substring(0, firstNameChars).concat(lastName.substring(0, lastNameChars));
    }

    private String sanitize(String string) {
        string = string.replace("æ", "a").replace("ø", "o").replace("å", "a");
        string = string.replaceAll("[ -']", "");
        return string;
    }
}
