package no.ntnu.team5.minvakt.dataaccess.wrapper;

import no.ntnu.team5.minvakt.db.User;

/**
 * Created by alan on 12/01/2017.
 */
public class UserLookup {
    User user;
    String username;

    public UserLookup(String username){
        this.username = username;
    }

    public User get(){
        if (user == null){
            user = null; //TODO
        }

        return user;
    }
}
