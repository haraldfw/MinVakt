package no.ntnu.team5.minvakt.controllers.web;

import no.ntnu.team5.minvakt.security.auth.intercept.Authorize;
import no.ntnu.team5.minvakt.utils.Cookies;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Harald Floor Wilhelmsen on 13.01.2017.
 */
@Controller
public class LoginWebController {

    @GetMapping("/login")
    public String show(Model model) {
        return "site/login";
    }

    @Authorize
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Model model) {
        Cookies.getCookie(request, "access_token").ifPresent(cookie -> {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        });

        return "site/login";
    }
}