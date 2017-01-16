package no.ntnu.team5.minvakt.data.access;

import no.ntnu.team5.minvakt.db.Competence;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Kenan on 1/12/2017.
 */

@Component
@Scope("prototype")
public class CompetenceAccess extends Access<Competence> {
    public List<String> getCompetencesNames() {
        return db.transaction(session -> {
            return session.createQuery("select name from Competence").list();
        });
    }

    public Set<Competence> getFromNames(List<String> compNames) {
        Set<Competence> competences = new HashSet<>();
        for(String name : compNames) {
            competences.add(new Competence(name));
        }

        return competences;
    }

    public Competence getFromName(String compName) {
        return db.transaction(session -> {
            Query query = session.createQuery("from Competence where name = :name");
            query.setParameter("name", compName);
            return (Competence) query.uniqueResult();
        });
    }
}
