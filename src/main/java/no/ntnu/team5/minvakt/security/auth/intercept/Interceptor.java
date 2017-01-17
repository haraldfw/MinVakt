package no.ntnu.team5.minvakt.security.auth.intercept;

import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by alan on 14/01/2017.
 */

public class Interceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;

            if (hm.hasMethodAnnotation(Authorize.class)) {
                if (!AuthorizeHandler.handle(request, response, hm, hm.getMethodAnnotation(Authorize.class))) {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return false;
                }
            }
        }

        return true;
    }
}
