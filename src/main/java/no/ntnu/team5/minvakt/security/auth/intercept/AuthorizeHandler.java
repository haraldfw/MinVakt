package no.ntnu.team5.minvakt.security.auth.intercept;

import io.jsonwebtoken.Claims;
import no.ntnu.team5.minvakt.security.auth.JWT;
import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import no.ntnu.team5.minvakt.utils.Cookies;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * Created by alan on 14/01/2017.
 */

public class AuthorizeHandler {
    private static final long MINUTES_10 = MILLISECONDS.convert(10, MINUTES);

    /**
     * Handles a request made on a method annotated with @Authorize, if the authorization is successful
     * adds the claims made by the {@code access_token} as an attribute on the request. In addition if the token expires in less than
     * 10 minutes we refresh the token and add it to the response.
     * <p>
     * If authorization fails redirects to {@see Authorize#value} if set, otherwise redirects to {@code /error} with status code 500
     *
     * @param request    The request made by an client
     * @param response   The response
     * @param hm         The method called
     * @param annotation The annotation on the method
     * @return {@code true}
     */
    public static boolean handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod hm, Authorize annotation) {
        Optional<Cookie> token = Cookies.getCookie(request, "access_token");

        if (!token.isPresent()) {
            return fail(annotation.value(), response);
        }

        String tokenValue = token.get().getValue();
        Optional<Claims> claims = JWT.verify(tokenValue);

        if (!claims.isPresent()) {
            return fail(annotation.value(), response);
        }

        if (claims.get().getExpiration().getTime() - new Date().getTime() < MINUTES_10) {
            String newToken = JWT.refresh(claims.get());
            token.get().setValue(newToken);
            token.get().setPath("/");
            response.addCookie(token.get());
        }

        request.setAttribute("auth.verifier", new Verifier(claims.get()));

        return true;
    }

    /**
     * Handles the failing of an request if it was not authorized.
     *
     * @param redirect The url to redirect to; may be the default {@see Authorize#NONE}
     * @param response The respone
     * @return {@code false}
     */
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
