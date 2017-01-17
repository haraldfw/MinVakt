package no.ntnu.team5.minvakt.configs;

import no.ntnu.team5.minvakt.security.auth.intercept.Interceptor;
import no.ntnu.team5.minvakt.security.auth.intercept.VerifierArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Created by alan on 14/01/2017.
 */

@Configuration
public class WebMvc extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Interceptor());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new VerifierArgumentResolver());
    }
}