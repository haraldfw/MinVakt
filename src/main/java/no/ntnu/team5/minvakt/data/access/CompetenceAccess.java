package no.ntnu.team5.minvakt.data.access;

import no.ntnu.team5.minvakt.db.Competence;
import no.ntnu.team5.minvakt.db.Shift;
import no.ntnu.team5.minvakt.model.ShiftModel;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Kenan on 1/12/2017.
 */

@Component
@Scope("prototype")
public class CompetenceAccess extends Access<Competence> {
    public List<String> getCompetencesNames() {
        return getDb().transaction(session -> {
            return session.createQuery("select name from Competence").list();
        });
    }

    public Competence getFromName(String compName) {
        return getDb().transaction(session -> {
            Query query = session.createQuery("from Competence where name = :name");
            query.setParameter("name", compName);
            return (Competence) query.uniqueResult();
        });
    }

    public static no.ntnu.team5.minvakt.model.Competence toModel(Competence competence) {
        no.ntnu.team5.minvakt.model.Competence c = new no.ntnu.team5.minvakt.model.Competence();
        c.setName(competence.getName());
        return c;
    }

    public static List<no.ntnu.team5.minvakt.model.Competence> toModel(Set<Competence> list) {
        return list.stream().map(CompetenceAccess::toModel).collect(Collectors.toList());
    }
}
