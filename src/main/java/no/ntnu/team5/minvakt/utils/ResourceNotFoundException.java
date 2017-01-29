package no.ntnu.team5.minvakt.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by alan on 24/01/2017.
 */

/**
 * A exception that when cast inside a {@see org.springframework.web.servlet.mvc.Controller} method sets the
 * HttpStatus to {@see org.springframework.http.HttpStatus#NOT_FOUND}
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String desc) {
        super(desc);
    }
}
