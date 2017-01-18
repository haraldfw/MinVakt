package no.ntnu.team5.minvakt.security.auth.intercept;

import io.jsonwebtoken.Claims;
import no.ntnu.team5.minvakt.security.auth.JWT;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by alan on 14/01/2017.
 */

public class AuthorizeHandler {
    public static boolean handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod hm, Authorize annotation) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return false;

        Optional<Cookie> token = Stream.of(cookies)
                .filter(cookie -> cookie.getName().equals("access_token"))
                .findAny();

        if (!token.isPresent()) {
            return fail(annotation.value(), response);
        }

        String tokenValue = token.get().getValue();
        Optional<Claims> claims = JWT.verify(tokenValue);

        if (!claims.isPresent()) {
            return fail(annotation.value(), response);
        }

        request.setAttribute("auth.verifier", new Verifier(claims.get()));

        return true;
    }

    public static boolean fail(String redirect, HttpServletResponse response) {
        if (redirect.equals(Authorize.NONE)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            try {
                response.sendRedirect(redirect);
            } catch (IOException e) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }

        return false;
    }
}
