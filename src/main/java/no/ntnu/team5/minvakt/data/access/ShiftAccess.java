package no.ntnu.team5.minvakt.data.access;

import no.ntnu.team5.minvakt.db.Shift;
import org.hibernate.Query;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Kenan on 1/12/2017.
 */

//TODO: Maybe added shift methods
public class ShiftAccess extends Access<Shift> {

    protected ShiftAccess(DbAccess db) {
        super(db);
    }

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
    public boolean addAbscence(Shift shift, byte abscense) {
        try{
            shift.setAbsent(abscense);
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
    public List<Shift> getShiftsForAUser(String username) {
        return db.transaction(session -> {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 7);
            Date date = cal.getTime();
            Query query = session.createQuery("from Shift shift where shift.user.username = :username and shift.startTime > current_date and shift.startTime < :date");
            query.setParameter("username", username);
            query.setParameter("date", date);
            return (List<Shift>) query.list();
        });
    }
    public Shift getShiftFromId(int id) {
        return db.transaction(session -> {
            Query query = session.createQuery("from Shift where id = :id");
            query.setParameter("id", id);
            return (Shift) query.uniqueResult();
        });

    }
}
