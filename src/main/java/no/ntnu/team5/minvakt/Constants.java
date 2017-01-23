package no.ntnu.team5.minvakt;

import no.ntnu.team5.minvakt.security.auth.JWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Harald Floor Wilhelmsen on 17.01.2017.
 */
@Component
public class Constants {

    public static String ADMIN;
    public static String SECURE_KEY;

    @Value("${roles.admin}")
    public void setADMIN(String ADMIN) {
        Constants.ADMIN = ADMIN;
    }

    @Value("${application.secret_key}")
    public void setSecureKey(String secretKey) {
        Constants.SECURE_KEY = secretKey;
    }
}
