package no.ntnu.team5.minvakt.data.access;

import no.ntnu.team5.minvakt.db.Availability;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Kenan on 1/12/2017.
 */

@Component
@Scope("prototype")
public class AvailabilityAccess extends Access<Availability> {
    public boolean makeAvailable(Date from, Date to) {
        //Availability avb = new Availability(,from, to); FIXME: Add userparameter, need method for getting from token

        //save(avb); //
        return true; //FIXME: Unit predicate
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
}
