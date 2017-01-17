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
public class AvailabilityAccess extends Access<Availability> {
    public boolean makeAvailable(User user, Date from, Date to) {
        Availability av = new Availability();
        av.setUser(user);
        av.setStartTime(from);
        av.setEndTime(to);

        return save(av);
    }

    public List<Availability> listAvailable() {
        return db.transaction(session -> {
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

    public void makeUnavailable(User user, Date dateFrom, Date dateTo) {
        db.transaction(session -> {
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
    public AvailabilityModel toModel(Availability availability) {
        AvailabilityModel  model = new AvailabilityModel();
        model.setUserId(availability.getUser().getId());
        model.setStartTime(availability.getStartTime());
        model.setEndTime(availability.getEndTime());

        return model;
    }
}
