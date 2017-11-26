package net.riperion.rodent.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class represents each individual rat sighting report.
 */

public class RatSighting implements Comparable<RatSighting> {
    private static final BigDecimal maxLatitude = new BigDecimal(90);
    private static final BigDecimal minLatitude = new BigDecimal(-90);
    private static final BigDecimal maxLongitude = new BigDecimal(180);
    private static final BigDecimal minLongitude = new BigDecimal(-180);

    private final int id;
    @SerializedName("date_created")
    private final Date date;
    @SerializedName("location_type")
    private final String locationType;
    @SerializedName("zip_code")
    private final int zipCode;
    private final String address;
    private final String city;
    private final String borough;
    private final BigDecimal latitude; // Made these decimals!
    private final BigDecimal longitude;

    /**
     * Constructs a new Rat Sighting instance
     *
     * @param id           the unique id for this sighting
     * @param date         the date of this sighting
     * @param locationType the location type for this sighting
     * @param zipCode      the zip code of the location
     * @param address      the address of the location
     * @param city         the city the sighting was in
     * @param borough      the borough the sighting was in
     * @param latitude     the latitude of the location of the sighting
     * @param longitude    the longitude of the location of the sighting
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

    /**
     * Gets the ID of a given rat sighting instance
     * @return the ID of the instance
     */
    public Integer getId() {
        return id;
    }

    /**
     * Gets the date of a given rat sighting instance
     * @return the date of the instance
     */
    public Date getDate() {
        return date;
    }

    /**
     * Gets the location type of a given rat sighting instance
     * @return the location type of the instance
     */
    public String getLocationType() {
        return locationType;
    }

    /**
     * Gets the ZIP code of the location of a given rat sighting instance
     * @return the ZIP code of the location of the instance
     */
    public int getZipCode() {
        return zipCode;
    }

    /**
     * Gets the address of the location of a given rat sighting instance
     * @return the address of the location of the instance
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the city of the location of a given rat sighting instance
     * @return the city of the location of the instance
     */
    public String getCity() {
        return city;
    }

    /**
     * Gets the borough of the location of a given rat sighting instance
     * @return the borough of the location of the instance
     */
    public String getBorough() {
        return borough;
    }

    /**
     * Gets the latitude of the location of a given rat sighting instance
     * @return the latitude of the location of the instance
     */
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * Gets the longitude of the location of a given rat sighting instance
     * @return the longitude of the location of the instance
     */
    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * Constructs a string containing all details of a rat sighting
     *
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
     * Validates all inputs regarding a Rat Sighting
     *
     * @param locationType the location type for this sighting
     * @param zipCode      the zip code of the location
     * @param address      the address of the location
     * @param city         the city the sighting was in
     * @param borough      the borough the sighting was in
     * @param latitude     the latitude of the location of the sighting
     * @param longitude    the longitude of the location of the sighting
     * @throws InvalidRatSightingException if any of the inputs is invalid
     */
    static void validateRatSightingDetails(String locationType, String zipCode, String address, String city, String borough, String latitude, String longitude) throws InvalidRatSightingException {
        ArrayList<InvalidRatSightingException.InvalidRatSightingReason> reasons = new ArrayList<>();

        if (!RatSighting.validateLocationType(locationType)) {
            reasons.add(InvalidRatSightingException.InvalidRatSightingReason.BAD_LOCATION_TYPE);
        }

        if (!RatSighting.validateZipCode(zipCode)) {
            reasons.add(InvalidRatSightingException.InvalidRatSightingReason.BAD_ZIP_CODE);
        }

        if (!RatSighting.validateAddress(address)) {
            reasons.add(InvalidRatSightingException.InvalidRatSightingReason.BAD_ADDRESS);
        }

        if (!RatSighting.validateCity(city)) {
            reasons.add(InvalidRatSightingException.InvalidRatSightingReason.BAD_CITY);
        }

        if (!RatSighting.validateBorough(borough)) {
            reasons.add(InvalidRatSightingException.InvalidRatSightingReason.BAD_BOROUGH);
        }

        if (!RatSighting.validateLatitude(latitude)) {
            reasons.add(InvalidRatSightingException.InvalidRatSightingReason.BAD_LATITUDE);
        }

        if (!RatSighting.validateLongitude(longitude)) {
            reasons.add(InvalidRatSightingException.InvalidRatSightingReason.BAD_LONGITUDE);
        }

        if (!reasons.isEmpty()) {
            throw new InvalidRatSightingException(reasons);
        }
    }


    /**
     * Check if a given location type input is valid
     *
     * @param locationType the location type input to validate
     * @return whether or not the input is valid
     */
    private static boolean validateLocationType(String locationType) {
        return !locationType.isEmpty();
    }

    /**
     * Check if a given zip code input is valid
     *
     * @param zipCode the zip code input to validate
     * @return whether or not the input is valid
     */
    private static boolean validateZipCode(String zipCode) {
        try {
            if (zipCode.length() >= 5) {
                return Integer.parseInt(zipCode) > 0;
            }
        } catch (Exception ignored) {
        }

        return false;
    }

    /**
     * Check if a given address input is valid
     *
     * @param address the address input to validate
     * @return whether or not the input is valid
     */
    private static boolean validateAddress(String address) {
        return !address.isEmpty();
    }

    /**
     * Check if a given city input is valid
     *
     * @param city the city input to validate
     * @return whether or not the input is valid
     */
    private static boolean validateCity(String city) {
        return !city.isEmpty();
    }

    /**
     * Check if a given borough input is valid
     *
     * @param borough the borough input to validate
     * @return whether or not the input is valid
     */
    private static boolean validateBorough(String borough) {
        return !borough.isEmpty();
    }

    /**
     * Check if a given latitude input is valid
     *
     * @param latitude the latitude input to validate
     * @return whether or not the input is valid
     */
    private static boolean validateLatitude(String latitude) {
        try {
            BigDecimal bd = new BigDecimal(latitude);

            if ((bd.compareTo(maxLatitude) <= 0) && (bd.compareTo(minLatitude) >= 0)) {
                return true;
            }
        } catch (Exception ignored) {
        }

        return false;
    }

    /**
     * Check if a given longitude input is valid
     *
     * @param longitude the longitude input to validate
     * @return whether or not the input is valid
     */
    private static boolean validateLongitude(String longitude) {
        try {
            BigDecimal bd = new BigDecimal(longitude);

            if ((bd.compareTo(maxLongitude) <= 0) && (bd.compareTo(minLongitude) >= 0)) {
                return true;
            }
        } catch (Exception ignored) {
        }

        return false;
    }

    @Override
    public int compareTo(@NonNull RatSighting ratSighting) {
        return Integer.compare(this.getId(), ratSighting.getId());
    }

    /**
     * Represents an error obtained during the validation of a Rat Sighting input
     */
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

        private final List<InvalidRatSightingReason> reasons;

        /**
         * Constructs an exception to be thrown when rat sighting input validation fails
         * @param reasons the reasons of the failure of the validation
         */
        public InvalidRatSightingException(List<InvalidRatSightingReason> reasons) {
            super("Invalid rat sighting input.");
            this.reasons = new ArrayList<>(reasons);
        }

        /**
         * Gets the reasons of the invalidity of the Rat Sighting input
         * @return a list of reasons of invalidity
         */
        public List<InvalidRatSightingReason> getReasons() {
            return reasons;
        }
    }
}
