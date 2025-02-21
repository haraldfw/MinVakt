package no.ntnu.team5.minvakt.utils;

import groovy.lang.Tuple2;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

/**
 * Created by alan on 13/01/2017.
 */
public class DateInterval {
    public LocalDateTime from;
    public LocalDateTime to;

    public DateInterval(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    public DateInterval(Date from, Date to) {
        this.from = LocalDateTime.ofInstant(from.toInstant(), ZoneId.systemDefault());
        this.to = LocalDateTime.ofInstant(to.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Test whether or not this dateinterval includes some time.
     *
     * @param time The time to check.
     * @return {@code true} if the time is included, {@code false} otherwise.
     */
    public boolean includes(LocalDateTime time) {
        return time.isAfter(from) && time.isBefore(to);
    }

    /**
     * Test whether two {@see DateInterval} intersects.
     *
     * @param other The other {@see DateInterval}.
     * @return {@code true} if they intersect, {@code false} otherwise.
     */
    public boolean intersects(DateInterval other) {
        return includes(other.from) || includes(other.to);
    }

    /**
     * Subtracts another {@see DateInterval} from this {@see DateInterval}.
     *
     * @param other The other {@see DateInterval}.
     * @return A tuple with 0-2 intervals, if a interval is mission it's {@see Optional#empty}.
     */
    public Tuple2<Optional<DateInterval>, Optional<DateInterval>> sub(DateInterval other) {
        if (other.from.isBefore(from)) {
            if (other.to.isAfter(to)) {
                return new Tuple2<>(Optional.empty(), Optional.empty());
            } else {
                return new Tuple2<>(Optional.of(new DateInterval(other.to, to)), Optional.empty());
            }
        } else if (other.from.isBefore(to)) {
            if (other.to.isBefore(to)) {
                return new Tuple2<>(Optional.of(new DateInterval(from, other.from)), Optional.of(new DateInterval(other.to, to)));
            } else {
                return new Tuple2<>(Optional.of(new DateInterval(from, other.from)), Optional.empty());
            }
        } else {
            return new Tuple2<>(Optional.of(this), Optional.empty());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof DateInterval)) return false;

        DateInterval other = (DateInterval) obj;

        return from.equals(other.from) && to.equals(other.to);
    }

    @Override
    public String toString() {
        return "from: " + from.toString() + "\nto: " + to.toString();
    }

    public Date getToAsDate() {
        Instant instant = to.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public Date getFromAsDate() {
        Instant instant = from.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }
}
