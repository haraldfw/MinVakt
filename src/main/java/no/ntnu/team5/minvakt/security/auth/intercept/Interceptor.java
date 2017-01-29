package no.ntnu.team5.minvakt.security.auth.intercept;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by alan on 14/01/2017.
 */

public class Interceptor extends HandlerInterceptorAdapter {

    /**
     * An interceptor that looks for methods annotated with {@see Authorize} and sends them to {@see AuthorizeHandler#handle}
     *
     * @param request  The request
     * @param response The response
     * @param handler  The method to look for annotations on.
     * @return {@code false}
     * @throws Exception In case we raise ForbiddenException
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;

            if (hm.hasMethodAnnotation(Authorize.class)) {
                if (!AuthorizeHandler.handle(request, response, hm, hm.getMethodAnnotation(Authorize.class))) {
                    return false;
                }
            }
        }

        return true;
    }
}
