package no.ntnu.team5.minvakt.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by alan on 19/01/2017.
 */
public class Cookies {

    /**
     * A helper method that gets some cookie from an {@see HttpServletRequest}.
     * @param request The {@see HttpServletRequest} to look for the cookie in.
     * @param name The name of the cookie.
     * @return {@see Optional<Cookie>} if the cookie exist, otherwise {@see Optional#empty}
     */
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return Optional.empty();

        return Stream.of(cookies)
                .filter(cookie -> cookie.getName().equals(name))
                .findAny();
    }
}
