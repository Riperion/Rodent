package net.riperion.rodent.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This is a wrapper class that contains the date - count pairs the Stats view of the API returns.
 */

public class DateCountPair {
    private final Date month;
    private final int count;

    public DateCountPair(Date month, int count) {
        this.month = month;
        this.count = count;
    }

    public Date getMonth() {
        return month;
    }

    public int getCount() {
        return count;
    }

    public static float getFloatFromDate(Date d) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(d);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        return (float) (year * 12 + month);
    }

    public static String getDateFromFloat(float f) {
        int time = (int) f;
        int year = time / 12;
        int month = time % 12;

        return String.format("%04d-%02d", year, month + 1);
    }
}
