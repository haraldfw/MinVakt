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

    /**
     * Creates a new {@see AccessContext} instance, the object must be closed after use.
     * <p>
     * Handles the initialization of the context.
     *
     * @return The {@see AccessContext} instance
     */
    public AccessContext newContext() {
        AccessContext ac = new AccessContext();
        beanFactory.autowireBean(ac);
        ac.init();
        return ac;
    }

    /**
     * A helper method to get an {@see AccessContext} instance.
     * <p>
     * Handles the initialization AND closing of the context.
     *
     * @param consumer The function within which the context is open.
     *                 Takes one parameter: {@see AccessContext}
     */
    public void with(Consumer<AccessContext> consumer) {
        AccessContext ac = newContext();
        consumer.accept(ac);
        ac.close();
    }

    /**
     * A helper method to get an {@see AccessContext} instance.
     * <p>
     * Handles the initialization AND closing of the context.
     *
     * @param consumer The function within which the context is open.
     *                 Takes one parameter: {@see AccessContext} and returns {@code <R>}
     * @param <R>      The return type of {@code consumer}, the return value is passed through.
     *                 Take care not to leak any context sensitive objects(like {@see User}).
     * @return The object returned by {@code consumer}.
     */
    public <R> R with(Function<AccessContext, R> consumer) {
        AccessContext ac = newContext();
        R r = consumer.apply(ac);
        ac.close();
        return r;
    }
}
