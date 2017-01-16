package no.ntnu.team5.minvakt.data.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by alan on 15/01/2017.
 */

@Component
@Scope("singleton")
public class AccessContextFactory {

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    public AccessContext newContext() {
        AccessContext ac = new AccessContext();
        beanFactory.autowireBean(ac);
        ac.init();
        return ac;
    }

    public void with(Consumer<AccessContext> consumer) {
        AccessContext ac = newContext();
        consumer.accept(ac);
        ac.close();
    }

    public <R> R with(Function<AccessContext, R> consumer) {
        AccessContext ac = newContext();
        R r = consumer.apply(ac);
        ac.close();
        return r;
    }
}
