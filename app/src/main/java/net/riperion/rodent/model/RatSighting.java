package net.riperion.rodent.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;

/**
 * This class represents each individual rat sighting report.
 */

public class RatSighting implements Comparable<RatSighting> {
    private static final BigDecimal maxLatitude = new BigDecimal(90);
    private static final BigDecimal minLatitude = new BigDecimal(-90);
    private static final BigDecimal maxLongitude = new BigDecimal(180);
    private static final BigDecimal minLongitude = new BigDecimal(-180);

    private final int id;
    @SerializedName("date_created") private final Date date;
    @SerializedName("location_type") private final String locationType;
    @SerializedName("zip_code") private final int zipCode;
    private final String address;
    private final String city;
    private final String borough;
    private final BigDecimal latitude; // Made these decimals!
    private final BigDecimal longitude;

    /**
     * Construct a new Rat Sighting instance
     * @param id the unique id for this sighting
     * @param date the date of this sighting
     * @param locationType the location type for this sighting
     * @param zipCode the zip code of the location
     * @param address the address of the location
     * @param city the city the sighting was in
     * @param borough the borough the sighting was in
     * @param latitude the latitude of the location of the sighting
     * @param longitude the longitude of the location of the sighting
     */
    public RatSighting(int id, Date date, String locationType, int zipCode, String address, String city, String borough, BigDecimal latitude, BigDecimal longitude) {
        this.id = id;
        this.date = date;
        this.locationType = locationType;
        this.zipCode = zipCode;
        this.address = address;
        this.city = city;
        this.borough = borough;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getLocationType() {
        return locationType;
    }

    public int getZipCode() {
        return zipCode;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getBorough() {
        return borough;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * Constructs a string containing all details of a rat sighting
     * @return string containing rat sighting details
     */
    public String getDetails() {
        String result = "";
        result += String.format("Key: %d%n", id);
        result += String.format("Date created: %s%n", date);
        result += String.format("Location type: %s%n", locationType);
        result += String.format("Zip code: %s%n", zipCode);
        result += String.format("Address: %s%n", address);
        result += String.format("City: %s%n", city);
        result += String.format("Borough: %s%n", borough);
        result += String.format("Latitude: %s%n", latitude);
        result += String.format("Longitude: %s%n", longitude);
        return result;
    }

    /**
     * Check if a given location type input is valid
     * @param locationType the location type input to validate
     * @return whether or not the input is valid
     */
    public static boolean validateLocationType(String locationType) {
        return locationType.length() > 0;
    }

    /**
     * Check if a given zip code input is valid
     * @param zipCode the zip code input to validate
     * @return whether or not the input is valid
     */
    public static boolean validateZipCode(String zipCode) {
        try {
            if (zipCode.length() >= 5)
                return Integer.parseInt(zipCode) > 0;
        } catch (Exception ignored) {}

        return false;
    }

    /**
     * Check if a given address input is valid
     * @param address the address input to validate
     * @return whether or not the input is valid
     */
    public static boolean validateAddress(String address) {
        return address.length() > 0;
    }

    /**
     * Check if a given city input is valid
     * @param city the city input to validate
     * @return whether or not the input is valid
     */
    public static boolean validateCity(String city) {
        return city.length() > 0;
    }

    /**
     * Check if a given borough input is valid
     * @param borough the borough input to validate
     * @return whether or not the input is valid
     */
    public static boolean validateBorough(String borough) {
        return borough.length() > 0;
    }

    /**
     * Check if a given latitude input is valid
     * @param latitude the latitude input to validate
     * @return whether or not the input is valid
     */
    public static boolean validateLatitude(String latitude) {
        try {
            BigDecimal bd = new BigDecimal(latitude);

            if (bd.compareTo(maxLatitude) <= 0 && bd.compareTo(minLatitude) >= 0)
                return true;
        } catch (Exception ignored) {}

        return false;
    }

    /**
     * Check if a given longitude input is valid
     * @param longitude the longitude input to validate
     * @return whether or not the input is valid
     */
    public static boolean validateLongitude(String longitude) {
        try {
            BigDecimal bd = new BigDecimal(longitude);

            if (bd.compareTo(maxLongitude) <= 0 && bd.compareTo(minLongitude) >= 0)
                return true;
        } catch (Exception ignored) {}

        return false;
    }

    @Override
    public int compareTo(@NonNull RatSighting ratSighting) {
        return Integer.compare(this.getId(),ratSighting.getId()) ;
    }

    public static class InvalidRatSightingException extends Exception {
        public enum InvalidRatSightingReason {
            BAD_ADDRESS,
            BAD_ZIP_CODE,
            BAD_LOCATION_TYPE,
            BAD_BOROUGH,
            BAD_CITY,
            BAD_LATITUDE,
            BAD_LONGITUDE
        }

        private final InvalidRatSightingReason reason;

        public InvalidRatSightingException(InvalidRatSightingReason reason) {
            super(reason.name());
            this.reason = reason;
        }

        public InvalidRatSightingReason getReason() {
            return reason;
        }
    }
}
