package net.riperion.rodent.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Contains functions used to serialize and deserialize between Dates and floats for the graph view.
 */

public class DateFloatSerializer {
    private static final int MONTHS_IN_YEAR = 12;

    /**
     * Serialize a Date object into a float for use on the graph view
     * @param date the Date object to serialize (only the year and month will be taken into account)
     * @return a float representing this Date
     */
    public static float getFloatFromDate(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        return (float) ((year * MONTHS_IN_YEAR) + month);
    }

    /**
     * Deserialize a date float into a String for use on the graph axes
     * @param serializedDate the float to deserialize
     * @return a String in YYYY-MM format representing the serialized date
     */
    public static String getDateStringFromFloat(float serializedDate) {
        int time = (int) serializedDate;
        int year = time / MONTHS_IN_YEAR;
        int month = time % MONTHS_IN_YEAR;

        return String.format("%04d-%02d", year, month + 1);
    }
}
