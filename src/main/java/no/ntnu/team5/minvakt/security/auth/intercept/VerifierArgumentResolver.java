package no.ntnu.team5.minvakt.security.auth.intercept;

import no.ntnu.team5.minvakt.security.auth.verify.Verifier;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by alan on 14/01/2017.
 */

public final class VerifierArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(Verifier.class);
    }

    /**
     * If an method has an {@see Verifier} parameter read the {@code auth.verifier}
     * attribute from request and set the parameter to it.
     *
     * @param parameter            the parameter
     * @param mv                   The view container
     * @param request              The request
     * @param webDataBinderFactory The date binder factory
     * @return the {@code auth.verifier} attribute
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mv,
                                  NativeWebRequest request,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {

        HttpServletRequest req = (HttpServletRequest) request.getNativeRequest();

        return req.getAttribute("auth.verifier");
    }
}