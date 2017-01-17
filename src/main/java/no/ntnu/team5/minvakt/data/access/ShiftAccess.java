package no.ntnu.team5.minvakt.data.access;

import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.ShiftModel;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Kenan on 1/12/2017.
 */

//TODO: Maybe added shift methods

@Component
@Scope("prototype")
public class ShiftAccess extends Access<Shift> {
    private static final Calendar CALENDAR = Calendar.getInstance();

    static {
        CALENDAR.setFirstDayOfWeek(Calendar.MONDAY);
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

    public boolean transferOwnership(Shift shift, User newOwner){
        shift.setUser(newOwner);
        return save(shift);
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

        return getDb().transaction(session -> {
            Query query = session.createQuery("from Shift sh where (:start_date <= sh.endTime) and (:end_date >= sh.startTime)");
//            query.setParameter("start_date", );
            query.setParameter("end_date", date);
            return (List<Shift>) query.list();
        });
    }

    public List<Shift> getUsersShiftCurrentWeek(String username) {
        Date now = new Date();
        CALENDAR.setTime(now);

        int daysToAdd = 8 - CALENDAR.get(Calendar.DAY_OF_WEEK);

        CALENDAR.add(Calendar.DAY_OF_YEAR, daysToAdd);
        CALENDAR.set(Calendar.HOUR, 24);
        CALENDAR.set(Calendar.MINUTE, 60);
        CALENDAR.set(Calendar.SECOND, 60);
        CALENDAR.set(Calendar.MILLISECOND, 1000);

        System.out.println(CALENDAR.getTime());

        return getDb().transaction(session -> {
            Query query = session.createQuery("from Shift sh where sh.user.username = :username and :dateFrom < startTime and :dateTo > endTime");
            query.setParameter("username", username);
            query.setParameter("dateFrom", now);
            query.setParameter("dateTo", CALENDAR.getTime());
            return (List<Shift>) query.list();
        });
    }


    public List<Shift> getShiftsFromDateToDate(Date dateFrom, Date dateTo) {
        return getDb().transaction(session -> {
            Query query = session.createQuery("from Shift where :dateFrom < startTime and :dateTo > endTime");
            query.setParameter("dateFrom", dateFrom);
            query.setParameter("dateTo", dateTo);
            return (List<Shift>) query.list();
        });
    }

    public List<Shift> getShiftsForAUser(String username) {
        return getDb().transaction(session -> {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 15);
            Date date = cal.getTime();
            Query query = session.createQuery("from Shift shift where shift.user.username = :username and shift.startTime > current_date and shift.startTime < :date");
            query.setParameter("username", username);
            query.setParameter("date", date);
            return (List<Shift>) query.list();
        });
    }

    public static ShiftModel toModel(Shift shift) {
        ShiftModel model = new ShiftModel();
        model.setAbsent((int) shift.getAbsent());
        model.setEndTime(shift.getEndTime());
        model.setStartTime(shift.getStartTime());
        model.setStandardHours((int) shift.getStandardHours());
        model.setUserModel(UserAccess.toModel(shift.getUser()));

        return model;
    }

    public Shift getShiftFromId(int id) {
        return getDb().transaction(session -> {
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

    public List<Shift> getAllCurrentMonth() {
        CALENDAR.setTime(new Date());

        CALENDAR.set(Calendar.DAY_OF_MONTH, 1);
        CALENDAR.set(Calendar.HOUR_OF_DAY, 0);
        CALENDAR.set(Calendar.MINUTE, 0);
        CALENDAR.set(Calendar.SECOND, 0);
        CALENDAR.set(Calendar.MILLISECOND, 0);

        Date start = CALENDAR.getTime();
        System.out.println("Start date of month: " + start);

        CALENDAR.set(Calendar.DAY_OF_MONTH, CALENDAR.getActualMaximum(Calendar.DAY_OF_MONTH));
        CALENDAR.set(Calendar.HOUR_OF_DAY, 24);
        CALENDAR.set(Calendar.MINUTE, 60);
        CALENDAR.set(Calendar.SECOND, 60);
        CALENDAR.set(Calendar.MILLISECOND, 1000);

        Date end = CALENDAR.getTime();
        System.out.println("End date of month: " + start);

        return getDb().transaction(session -> {
            Query query = session.createQuery("from Shift where (:start <= endTime) and (:end >= startTime)");
            query.setParameter("start", start);
            query.setParameter("end", end);
            return query.list();
        });
    }

    public static List<ShiftModel> toModel(List<Shift> list) {
        return list.stream().map(ShiftAccess::toModel).collect(Collectors.toList());
    }
}
