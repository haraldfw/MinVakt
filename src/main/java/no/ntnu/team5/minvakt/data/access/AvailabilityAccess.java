package no.ntnu.team5.minvakt.data.access;

import groovy.lang.Tuple2;
import no.ntnu.team5.minvakt.db.Availability;
import no.ntnu.team5.minvakt.db.User;
import no.ntnu.team5.minvakt.model.AvailabilityModel;
import no.ntnu.team5.minvakt.utils.DateInterval;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by Kenan on 1/12/2017.
 */

@Component
@Scope("prototype")
public class AvailabilityAccess extends Access<Availability, AvailabilityModel> {
    private static final Calendar calendar = Calendar.getInstance();

    static {
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
    }

    /**
     * Creates an availability on the specified date interval for the given user
     *
     * @param user User to create availability for
     * @param from Date and time to start availability
     * @param to   Date and time to end availability
     * @return True if success
     */
    public boolean makeAvailable(User user, Date from, Date to) {
        Availability av = new Availability();
        av.setUser(user);
        av.setStartTime(from);
        av.setEndTime(to);

        return save(av);
    }

    /**
     * Gets a list of all availabilities from now and one week into the future.
     *
     * @return The list of availability-objects
     */
    public List<Availability> listAvailable() {
        return getDb().transaction(session -> {
            Date dateFrom = Calendar.getInstance().getTime();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 7);
            Date dateTo = cal.getTime();
            Query query = session.createQuery("from Availability where current_date between :dateFrom and :dateTo");
            query.setParameter("dateFrom", dateFrom);
            query.setParameter("dateTo", dateTo);
            return (List<Availability>) query.list();
        });
    }

    /**
     * Gets a list of all availabilities for the given week and user.
     *
     * @param date     Date in the week to get availabilities from
     * @param username Username of the user to get availabilities for.
     * @return The list of availability-objects for the given user.
     */
    public List<Availability> getAllCurrentWeekForUser(Date date, String username) {
        calendar.setTime(date);

        calendar.setFirstDayOfWeek(Calendar.MONDAY);//TODO: sjekk om kan fjernes
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date fromDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date toDate = calendar.getTime();

        return getAvailabilityFromDateToDateForUser(fromDate, toDate, username);
    }

    /**
     * Gets availabilities for the given datetime interval and username
     *
     * @param fromDate Start of date interval
     * @param toDate   End of date interval
     * @param username User to get availabilities for
     * @return The list of availability objects for the given user in the given interval
     */
    public List<Availability> getAvailabilityFromDateToDateForUser(Date fromDate, Date toDate, String username) {
        return getDb().transaction(session -> {
            Query query = session.createQuery("from Availability where :fromDate <= startTime and :toDate >= endTime " +
                    "and user.username = :username order by startTime asc");
            query.setParameter("fromDate", fromDate);
            query.setParameter("toDate", toDate);
            query.setParameter("username", username);
            return (List<Availability>) query.list();
        });
    }

    /**
     * Gets a list of users that are available in the given date interval
     *
     * @param dateFrom Start of datetime interval
     * @param dateTo   End of datetime interval
     * @return List of user avaliable in the given datetime interval
     */
    public List<User> listAvailableUsers(Date dateFrom, Date dateTo) {
        return getDb().transaction(session -> {
            Query query = session.createQuery("select distinct user from Availability as a where (:dateFrom between a.startTime and a.endTime) " +
                    "and :dateTo between a.startTime and a.endTime");
            query.setParameter("dateFrom", dateFrom);
            query.setParameter("dateTo", dateTo);

            return (List<User>) query.list();
        });
    }


    /**
     * Removes availability in the given datetime interval for the given user. Splits availability
     * objects if necessary.
     *
     * @param user     User to make unavailable
     * @param dateFrom Start of datetime interval
     * @param dateTo   End of datetime interval
     */
    public void makeUnavailable(User user, Date dateFrom, Date dateTo) {
        getDb().transaction(session -> {
            //FIXME: i don't work right

            Query query = session.createQuery("from Availability av where :username = av.user.username and (:start_a <= av.endTime) and (:end_a >= av.startTime)");

            query.setParameter("username", user.getUsername());
            query.setParameter("start_a", dateFrom);
            query.setParameter("end_a", dateTo);

            DateInterval removeInterval = new DateInterval(dateFrom, dateTo);
            for (Availability av : (List<Availability>) query.list()) {
                DateInterval avInterval = new DateInterval(av.getStartTime(), av.getEndTime());
                Tuple2<Optional<DateInterval>, Optional<DateInterval>> newIntervals = avInterval.sub(removeInterval);

                if (newIntervals.getFirst().isPresent()) {
                    av.setStartTime(newIntervals.getFirst().get().getFromAsDate());
                    av.setEndTime(newIntervals.getFirst().get().getToAsDate());

                    save(av);
                }

                if (newIntervals.getSecond().isPresent()) {
                    Availability newAv = new Availability();
                    newAv.setStartTime(newIntervals.getSecond().get().getFromAsDate());
                    newAv.setEndTime(newIntervals.getSecond().get().getToAsDate());
                    newAv.setUser(user);

                    save(newAv);
                }
            }
        });
    }

    /**
     * Converts the given availability-object into it's model equivalent.
     *
     * @param availability Availability-object to convert.
     * @return An AvailabilityModel-object representing the given object.
     */
    public AvailabilityModel toModel(Availability availability) {
        AvailabilityModel model = new AvailabilityModel();
        model.setUserId(availability.getUser().getId());
        model.setStartTime(availability.getStartTime());
        model.setEndTime(availability.getEndTime());

        return model;
    }
}
