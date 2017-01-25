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

/**
 * Created by Kenan on 1/12/2017.
 */
//TODO: Maybe added shift methods

@Component
@Scope("prototype")
public class ShiftAccess extends Access<Shift, ShiftModel> {
    private static final Calendar CALENDAR = Calendar.getInstance();

    private CompetenceAccess competenceAccess;

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

    public boolean transferOwnership(Shift shift, User newOwner) {
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
        } catch (NullPointerException npe) {
            return false;
        }
    }

    public boolean addAbscence(Shift shift, boolean abscense) {
        shift.setAbsent(abscense);
        return save(shift);
    }

    public List<Shift> getShiftsOnDate(Date date) {
        return getDb().transaction(session -> {
            Query query = session.createQuery("from Shift sh " +
                    "where (:start_date <= sh.endTime) and (:end_date >= sh.startTime) " +
                    "order by startTime asc");
            query.setParameter("start_date", setTimeOfDate(date, 0, 0));
            query.setParameter("end_date", setTimeOfDate(date, 23, 59));
            return (List<Shift>) query.list();
        });
    }

    public List<Shift> getUsersShiftNextDays(String username, int days) {
        Date from = setTimeOfDate(new Date(), 0, 0);

        CALENDAR.setTime(from);
        CALENDAR.add(Calendar.DAY_OF_YEAR, days);
        CALENDAR.set(Calendar.HOUR_OF_DAY, 24);
        CALENDAR.set(Calendar.MINUTE, 59);
        Date to = CALENDAR.getTime();

        return getDb().transaction(session -> {
            Query query = session.createQuery("from Shift sh " +
                    "where sh.user.username = :username and :dateFrom < startTime " +
                    "and :dateTo > endTime order by startTime asc");
            query.setParameter("username", username);
            query.setParameter("dateFrom", from);
            query.setParameter("dateTo", to);
            return (List<Shift>) query.list();
        });
    }

    public List<Shift> getShiftsFromDateToDate(Date dateFrom, Date dateTo) {
        return getDb().transaction(session -> {
            Query query = session.createQuery("from Shift where :dateFrom < startTime " +
                    "and :dateTo > endTime order by startTime asc");
            query.setParameter("dateFrom", dateFrom);
            query.setParameter("dateTo", dateTo);
            return (List<Shift>) query.list();
        });
    }

    private Date setTimeOfDate(Date date, int hours, int minutes) {
        CALENDAR.setTime(date);
        CALENDAR.set(Calendar.HOUR_OF_DAY, hours);
        CALENDAR.set(Calendar.MINUTE, minutes);
        return CALENDAR.getTime();
    }

    public List<Shift> getShiftsOnDateDays(Date dateFrom, Date dateTo) {
        dateFrom = setTimeOfDate(dateFrom, 0, 0);
        dateTo = setTimeOfDate(dateTo, 23, 59);
        return getShiftsFromDateToDate(dateFrom, dateTo);
    }

    public List<Shift> getShiftOnDaysForUser(int userId, Date dateFrom, Date dateTo) {
        return getDb().transaction(session -> {
            Date from = setTimeOfDate(dateFrom, 0, 0);
            Date to = setTimeOfDate(dateTo, 23, 59);

            Query query = session.createQuery("from Shift " +
                    "where :dateFrom < startTime and :dateTo > endTime and user.id = :user_id order by startTime asc");
            query.setParameter("dateFrom", from);
            query.setParameter("dateTo", to);
            query.setParameter("user_id", userId);
            return (List<Shift>) query.list();
        });
    }

    public List<Shift> getShiftsForAUser(String username) {
        return getDb().transaction(session -> {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 15);
            Date date = cal.getTime();
            Query query = session.createQuery("from Shift shift " +
                    "where shift.user.username = :username and shift.startTime > current_date " +
                    "and shift.startTime < :date");
            query.setParameter("username", username);
            query.setParameter("date", date);
            return (List<Shift>) query.list();
        });
    }

    public ShiftModel toModel(Shift shift) {
        ShiftModel model = new ShiftModel();
        model.setId(shift.getId());
        model.setAbsent(shift.getAbsent());
        model.setEndTime(shift.getEndTime());
        model.setStartTime(shift.getStartTime());
        model.setStandardHours((int) shift.getStandardHours());
        model.setUserModel(getContext().user.toModel(shift.getUser()));
        model.setCompetences(getContext().competence.toModel(shift.getCompetences()));

        return model;
    }

    public Shift toShift(ShiftModel sm) {
        Shift shift = new Shift();
        shift.setUser(getContext().user.fromUsername(sm.getUserModel().getUsername()));
        shift.setStartTime(sm.getStartTime());
        shift.setEndTime(sm.getEndTime());
        shift.setAbsent(sm.getAbsent());
        shift.setStandardHours(sm.getStandardHours().byteValue());
        shift.setCompetences(getContext().competence.getFromNames(competenceAccess.getCompetenceNames(sm.getCompetences())));

        return shift;
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
        for (Shift s : shiftsBetweenDates) {
            s.setAbsent(true);
        }
    }

    public List<Shift> getAllCurrentWeekForUser(Date dateFrom, String username) {
        CALENDAR.setTime(dateFrom);
        CALENDAR.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        CALENDAR.set(Calendar.HOUR_OF_DAY, 0);
        CALENDAR.set(Calendar.MINUTE, 0);
        dateFrom = CALENDAR.getTime();
        CALENDAR.add(Calendar.DAY_OF_YEAR, 7);
        Date dateTo = CALENDAR.getTime();

        return getShiftsFromDateToDateForUser(dateFrom, dateTo, username);
    }

    public double getHoursInWeekForUser(Date currentDate, String username) {
        CALENDAR.setTime(currentDate);
        CALENDAR.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        CALENDAR.set(Calendar.HOUR_OF_DAY, 0);
        CALENDAR.set(Calendar.MINUTE, 0);
        Date dateFrom = CALENDAR.getTime();
        CALENDAR.add(CALENDAR.DAY_OF_YEAR, 7);
        Date dateTo = CALENDAR.getTime();

        return getShiftsFromDateToDateForUser(dateFrom, dateTo, username)
                .stream().mapToDouble(value -> value.getStandardHours()).sum();
    }

    public List<Shift> getShiftsFromDateToDateForUser(Date dateFrom, Date dateTo, String username) {
        return getDb().transaction(session -> {
            Query query = session.createQuery(
                    "from Shift " +
                            "where :dateFrom <= startTime " +
                            "and :dateTo >= endTime " +
                            "and user.username = :username " +
                            "order by startTime asc");
            query.setParameter("dateFrom", dateFrom);
            query.setParameter("dateTo", dateTo);
            query.setParameter("username", username);
            return (List<Shift>) query.list();
        });
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

    public List<Shift> getTheseThreeDays(String username) {
        CALENDAR.setTime(new Date());
        CALENDAR.set(Calendar.HOUR_OF_DAY, 0);
        CALENDAR.set(Calendar.MINUTE, 0);
        CALENDAR.add(Calendar.DAY_OF_YEAR, -1);
        Date fromDate = CALENDAR.getTime();

        CALENDAR.add(Calendar.DAY_OF_YEAR, 3);
        Date toDate = CALENDAR.getTime();
        return getShiftsFromDateToDateForUser(fromDate, toDate, username);
    }
}
