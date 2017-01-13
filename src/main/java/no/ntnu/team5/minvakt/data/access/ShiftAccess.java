package no.ntnu.team5.minvakt.data.access;

import no.ntnu.team5.minvakt.db.Shift;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by alan on 12/01/2017.
 */

@Component
@Scope("singleton")
public class ShiftAccess extends Access<Shift> {
}
