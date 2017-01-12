package no.ntnu.team5.minvakt.dataaccess;

import no.ntnu.team5.minvakt.db.Shift;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Kenan on 1/12/2017.
 */

//TODO: Maybe added shift methods
@Component
@Scope("singleton")
public class ShiftAccess extends Access<Shift> {

}
