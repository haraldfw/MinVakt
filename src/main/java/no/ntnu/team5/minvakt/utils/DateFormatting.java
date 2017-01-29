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

    /**
     * A common format to format dates.
     * @param input The {@see Date} to format.
     * @return The formatted string.
     */
    public static String format(Date input) {
        return DATE_FORMAT.format(input);
    }
}
