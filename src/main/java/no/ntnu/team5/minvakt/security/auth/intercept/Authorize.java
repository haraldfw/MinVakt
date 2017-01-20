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
    static final String NONE = "I_HATE_JAVA_WHY_CANT_THIS_BE_NULL?";

    String value() default NONE;
}
