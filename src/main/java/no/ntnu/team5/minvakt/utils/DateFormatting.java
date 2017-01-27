package no.ntnu.team5.minvakt.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Harald Floor Wilhelmsen on 27.01.2017.
 */
public final class DateFormatting {

    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("EEE, d MMM yyyy HH:mm");

    private DateFormatting() {

    }

    public static String format(Date input) {
        return DATE_FORMAT.format(input);
    }
}
