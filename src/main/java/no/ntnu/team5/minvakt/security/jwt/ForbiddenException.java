package no.ntnu.team5.minvakt.security.jwt;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by alan on 12/01/2017.
 */

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class ForbiddenException extends RuntimeException {
}
