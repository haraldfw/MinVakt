package no.ntnu.team5.minvakt.security.auth.intercept;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by alan on 14/01/2017.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {
    static final String NONE = "\uD83D\uDE02CA7175AE015253A49FF4432C81D8FD04D711505E99470B5692320DB08F3F5A6A9F809BE012DEB6AFC32229A3F887C9FE39479BB995A96A5892D1A51C005F1857\uD83D\uDE02";

    String value() default NONE;
}
