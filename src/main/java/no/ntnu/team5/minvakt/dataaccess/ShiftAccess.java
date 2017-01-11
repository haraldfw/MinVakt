package no.ntnu.team5.minvakt.dataaccess;

import no.ntnu.team5.minvakt.db.Shift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by alan on 11/01/2017.
 */

@Component
public class ShiftAccess {
    @Autowired
    DbAccess db;

    public boolean save(Shift shift){
        db.transaction(session -> {
            System.out.println(shift);
            session.save(shift);
            session.flush();
        });

        return true; //FIXME(erl)
    }
}
