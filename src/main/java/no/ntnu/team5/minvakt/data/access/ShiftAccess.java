package no.ntnu.team5.minvakt.data.access;

import no.ntnu.team5.minvakt.db.Competence;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.db.User;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Kenan on 1/12/2017.
 */

//TODO: Maybe added shift methods
@Component
@Scope("singleton")
public class ShiftAccess extends Access<Shift> {

    public boolean changeShift(Shift fromShift, Shift toShift) {
        Shift temp = new Shift();
        temp = fromShift;
        fromShift.setUser(toShift.getUser());
        toShift.setUser(temp.getUser());
        save(fromShift);
        save(toShift);
        return true; //FIXME: Send accept notification to admin functionality
    }
    //Changes start- and endtime for a selected shift
    //FIXME: Make so only systemadmin can use
    public boolean updateShiftTimes(Shift shift, Date start, Date end) {
        try {
            if (start == null || end == null)
                throw new NullPointerException("NPE");

            shift.setStartTime(start);
            shift.setEndTime(end);
            save(shift);
            return true;
        } catch(NullPointerException npe) {
            return false;
        }
    }
    //Changes abscense status for a seleceted shift
    public boolean addAbscence(Shift shift, byte abscene) {
        try{
            shift.setAbsent(abscene);
            save(shift);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public List<Shift> getShiftsOnDate(Date date) {
        return db.transaction(session -> {
            Query query = session.createQuery("from Shift where :date between startTime and endTime");
            query.setParameter("date", date);
            return (List<Shift>) query.list();
        });
    }
}
