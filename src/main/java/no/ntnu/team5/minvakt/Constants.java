package no.ntnu.team5.minvakt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Harald Floor Wilhelmsen on 17.01.2017.
 */
@Component
public class Constants {

    public static String ADMIN;

    @Value("${roles.admin}")
    public void setADMIN(String ADMIN) {
        Constants.ADMIN = ADMIN;
    }
}
