package no.ntnu.team5.minvakt.data;

import groovy.lang.Tuple2;
import no.ntnu.team5.minvakt.utils.DateInterval;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by alan on 13/01/2017.
 */
public class DateIntervalTest {

    @Test
    public void testSub() {
        LocalDateTime now = LocalDateTime.now();
        DateInterval t1 = new DateInterval(now, now.plusDays(2).plusSeconds(30));
        DateInterval t2 = new DateInterval(now.plusDays(2), now.plusDays(5));
        DateInterval t3 = new DateInterval(now.plusDays(1).minusSeconds(15), now.plusDays(1).plusSeconds(15));
        DateInterval t4 = new DateInterval(t2.to, t2.to.plusMonths(1).plusHours(1));
        DateInterval t5 = new DateInterval(now, t2.to.plusMonths(1).plusHours(2));

        Tuple2<Optional<DateInterval>, Optional<DateInterval>> a1 = t1.sub(t2);
        Assert.assertTrue(a1.getFirst().isPresent());
        Assert.assertFalse(a1.getSecond().isPresent());
        Assert.assertEquals(new DateInterval(t1.from, t2.from), a1.getFirst().get());

        Tuple2<Optional<DateInterval>, Optional<DateInterval>> a2 = t1.sub(t3);
        Assert.assertTrue(a2.getFirst().isPresent());
        Assert.assertTrue(a2.getSecond().isPresent());
        Assert.assertEquals(new DateInterval(t1.from, t3.from), a2.getFirst().get());
        Assert.assertEquals(new DateInterval(t3.to, t1.to), a2.getSecond().get());

        Tuple2<Optional<DateInterval>, Optional<DateInterval>> a3 = t2.sub(t1);
        Assert.assertTrue(a3.getFirst().isPresent());
        Assert.assertFalse(a3.getSecond().isPresent());
        Assert.assertEquals(new DateInterval(t1.to, t2.to), a3.getFirst().get());

        Tuple2<Optional<DateInterval>, Optional<DateInterval>> a4 = t4.sub(t5);
        Assert.assertFalse(a4.getFirst().isPresent());
        Assert.assertFalse(a4.getSecond().isPresent());

        Tuple2<Optional<DateInterval>, Optional<DateInterval>> a5 = t1.sub(t4);
        Assert.assertTrue(a5.getFirst().isPresent());
        Assert.assertFalse(a5.getSecond().isPresent());
        Assert.assertEquals(t1, a5.getFirst().get());
    }
}
