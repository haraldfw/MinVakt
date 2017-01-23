package no.ntnu.team5.minvakt.data.access;

import no.ntnu.team5.minvakt.db.Competence;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Kenan on 1/12/2017.
 */

@Component
@Scope("prototype")
public class CompetenceAccess extends Access<Competence, no.ntnu.team5.minvakt.model.Competence> {
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
    public List<String> getCompetenceNames(List<no.ntnu.team5.minvakt.model.Competence> comps) {
        List<String> names =  new ArrayList<>();

        for (no.ntnu.team5.minvakt.model.Competence c : comps) {
            names.add(c.getName());
        }
        return names;
    }

    public Set<Competence> getFromNames(List<String> compNames) {
        return compNames.stream().map(this::getFromName).collect(Collectors.toSet());
    }

    public no.ntnu.team5.minvakt.model.Competence toModel(Competence competence) {
        no.ntnu.team5.minvakt.model.Competence c = new no.ntnu.team5.minvakt.model.Competence();
        c.setName(competence.getName());
        return c;
    }
}
