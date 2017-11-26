package net.riperion.rodent.model;

import java.util.Date;

/**
 * A wrapper class that contains the date - count pairs the Stats view of the API returns.
 */

public class DateCountPair {
    private final Date month;
    private final int count;

    /**
     * Initialize a new DateCountPair. This constructor is to be called by the GSON deserializer.
     * @param month the month received from the API
     * @param count the count for the given month
     */
    public DateCountPair(Date month, int count) {
        this.month = month;
        this.count = count;
    }

    /**
     * Get the month of the date-count pair.
     * @return a date object corresponding to the first second of the month
     */
    public Date getMonth() {
        return month;
    }

    /**
     * Get the count of the date-count pair.
     * @return count of objects in the given month
     */
    public int getCount() {
        return count;
    }

}
