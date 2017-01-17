package no.ntnu.team5.minvakt.data.access;

import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.model.ShiftModel;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Kenan on 1/12/2017.
 */

//TODO: Maybe added shift methods

@Component
@Scope("prototype")
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

    public boolean addAbscence(Shift shift, byte abscense) {
        shift.setAbsent(abscense);
        return save(shift);
    }

    public List<Shift> getShiftsOnDate(Date date) {
        //FIXME
//        Calendar
//        LocalDate start = new LocalDate(date.getYear(), );

        return db.transaction(session -> {
            Query query = session.createQuery("from Shift sh where (:start_date <= sh.endTime) and (:end_date >= sh.startTime)");
//            query.setParameter("start_date", );
            query.setParameter("end_date", date);
            return (List<Shift>) query.list();
        });
    }

    public List<Shift> getShiftsFromDateToDate(Date dateFrom, Date dateTo) {
        return db.transaction(session -> {
            Query query = session.createQuery("from Shift where :dateFrom < startTime and :dateTo > endTime");
            query.setParameter("dateFrom", dateFrom);
            query.setParameter("dateTo", dateTo);
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

    public ShiftModel toModel(Shift shift) {
        ShiftModel model = new ShiftModel();
        model.setAbsent((int) shift.getAbsent());
        model.setEndTime(shift.getEndTime());
        model.setStartTime(shift.getStartTime());
        model.setStandardHours((int) shift.getStandardHours());
        model.setUserId(shift.getUser().getId());

        return model;
    }

    public Shift getShiftFromId(int id) {
        return db.transaction(session -> {
            Query query = session.createQuery("from Shift where id = :id");
            query.setParameter("id", id);
            return (Shift) query.uniqueResult();
        });
    }
    public void makeUnavailable(Date dateFrom, Date dateTo) {
        List<Shift> shiftsBetweenDates = getShiftsFromDateToDate(dateFrom, dateTo);
        for(Shift s: shiftsBetweenDates) {
            s.setAbsent((byte)1);
        }
    }
}
