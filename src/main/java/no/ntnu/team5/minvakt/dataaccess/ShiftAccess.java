package no.ntnu.team5.minvakt.dataaccess;

import no.ntnu.team5.minvakt.db.Shift;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by alan on 11/01/2017.
 */

@Component
@Scope("singleton")
public class ShiftAccess extends Access<Shift> {}
