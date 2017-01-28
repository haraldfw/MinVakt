package no.ntnu.team5.minvakt.security.auth.verify;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by alan on 12/01/2017.
 */

/**
 * A exception that when cast inside a {@see org.springframework.web.servlet.mvc.Controller} method sets the HttpStatus to {@see org.springframework.http.HttpStatus#UNAUTHORIZED}
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class ForbiddenException extends RuntimeException {
}
