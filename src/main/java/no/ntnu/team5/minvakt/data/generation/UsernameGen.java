package no.ntnu.team5.minvakt.data.generation;

import no.ntnu.team5.minvakt.data.access.AccessContextFactory;
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
    private AccessContextFactory accessor;

    /**
     * Generates a username from the given first and last name
     *
     * @param firstName First name of user
     * @param lastName  Last name of user
     * @return The generated username
     */
    public String generateUsername(String firstName, String lastName) {
        List<String> usernames = accessor.with(access -> {
            return access.user.getUsernames();
        });

        firstName = sanitize(firstName.toLowerCase());
        lastName = sanitize(lastName.toLowerCase());

        String username;
        int firstnameIter = 0;
        int lastnameIter = 0;
        boolean changeFirstname = false;
        do {
            username = expandingGeneration(firstName, lastName, firstnameIter, lastnameIter);

            if (changeFirstname) {
                firstnameIter++;
            } else {
                lastnameIter++;
            }
            changeFirstname = !changeFirstname;
        } while (usernames.contains(username));
        return username;
    }

    /**
     * Generates a username from the given name and specifications of chars to add.
     *
     * @param firstName    First name of user
     * @param lastName     Last name of user
     * @param firstnameAdd Numbers of chars to add to standard amount of chars from first name
     * @param lastnameAdd  Numbers of chars to add to standard amount of chars from last name
     * @return The generated username
     */
    private String expandingGeneration(String firstName, String lastName, int firstnameAdd,
                                       int lastnameAdd) {

        final int fnInitialChars = 2;
        final int lnInitialChars = 2;

        int firstNameChars = fnInitialChars + firstnameAdd;
        int lastNameChars = lnInitialChars + lastnameAdd;

        if (firstName.length() < firstNameChars) {
            firstNameChars = firstName.length();
        }

        while (lastName.length() < lastNameChars) {
            lastName = lastName.concat(String.valueOf((int) (Math.random() * 10)));
        }

        // firstnamechars + lastnamechars
        return firstName.substring(0, firstNameChars).concat(lastName.substring(0, lastNameChars));
    }

    /**
     * Removes special chars from norwegian language and dash and apostrophe
     * @param string String to sanitize
     * @return The sanitized string
     */
    private String sanitize(String string) {
        string = string.replace("æ", "a").replace("ø", "o").replace("å", "a");
        string = string.replaceAll("[ -']", "");
        return string;
    }
}
