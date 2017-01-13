package no.ntnu.team5.minvakt.dataaccess;

import no.ntnu.team5.minvakt.db.Competence;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Kenan on 1/12/2017.
 */
@Component
@Scope("singleton")
public class CompetenceAccess extends Access<Competence> {

}
