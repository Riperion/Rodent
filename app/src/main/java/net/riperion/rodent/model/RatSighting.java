package net.riperion.rodent.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import net.riperion.rodent.RodentApp;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by cgokmen on 10/10/17.
 */

public class RatSighting implements Comparable<RatSighting> {
    private int id;
    @SerializedName("date_created") private Date date;
    @SerializedName("location_type") private String locationType;
    @SerializedName("zip_code") private int zipCode;
    private String address;
    private String city;
    private String borough;
    private BigDecimal latitude; // Made these decimals!
    private BigDecimal longitude;

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

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setBorough(String borough) {
        this.borough = borough;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    /**
     * Synchronously gets an unfiltered list of rat sightings from the API
     * @param offset the offset from which we paginate
     * @return List of RatSighting objects retrieved
     * @throws IOException If something goes wrong with the API call
     */
    public static List<RatSighting> getRatSightings(Integer offset) throws IOException {
        Call<ListWrapper<RatSighting>> call = RodentApp.getApi().getRatSightings(User.getAuthToken().get_authorization(), offset);
        Response<ListWrapper<RatSighting>> response = call.execute();

        return response.body().getResults();
    }

    /**
     * Asynchronously gets an unfiltered list of rat sightings from the API
     * @param cb class that will be called as the callback once values are returned
     * @param offset the offset from which we paginate
     */
    public static void asyncGetRatSightings(Callback<ListWrapper<RatSighting>> cb, Integer offset) {
        Call<ListWrapper<RatSighting>> call = RodentApp.getApi().getRatSightings(User.getAuthToken().get_authorization(), offset);
        call.enqueue(cb);

        // TODO: Add unwrapper middleware
    }

    /**
     * Synchronously gets a single rat sighting from the API using its key
     * @param id the ID of the rat sighting we're looking for
     * @return the corresponding RatSighting object
     * @throws IOException If something goes wrong with the API call
     */
    public static RatSighting getRatSightingByKey(int id) throws IOException {
        Call<RatSighting> call = RodentApp.getApi().getRatSightingById(User.getAuthToken().get_authorization(), id);
        Response<RatSighting> response = call.execute();

        return response.body();
    }

    /**
     * Asynchronously gets a single rat sighting from the API using its key
     * @param id the ID of the rat sighting we're looking for
     * @param cb class that will be called as the callback once values are returned
     */
    public static void asyncGetRatSightingByKey(int id, Callback<RatSighting> cb) {
        Call<RatSighting> call = RodentApp.getApi().getRatSightingById(User.getAuthToken().get_authorization(), id);
        call.enqueue(cb);
    }

    /**
     * Asynchronously gets a list of rat sightings using a date range
     * @param cb class that will be called as the callback once values are returned
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     */
    public static void asyncGetRatSightingByDateRange(Date startDate, Date endDate, Callback<ListWrapper<RatSighting>> cb) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Call<ListWrapper<RatSighting>> call = RodentApp.getApi().getRatSightingsByDateRange(User.getAuthToken().get_authorization(), 0, dateFormat.format(startDate), dateFormat.format(endDate));
        call.enqueue(cb);

        // TODO: Add unwrapper middleware
    }

    /**
     * Synchronously sends a new Rat Sighting to the API
     * @param locationType the location type for this sighting
     * @param zipCode the zip code of the location
     * @param address the address of the location
     * @param city the city the sighting was in
     * @param borough the borough the sighting was in
     * @param latitude the latitude of the location of the sighting
     * @param longitude the longitude of the location of the sighting
     * @return boolean denoting whether the item was added successfully
     * @throws IOException If something goes wrong with the API call
     */
    public static boolean addRatSighting(String locationType, int zipCode, String address, String city, String borough, BigDecimal latitude, BigDecimal longitude) throws IOException {
        Call<Void> call = RodentApp.getApi().addRatSighting(User.getAuthToken().get_authorization(), locationType, zipCode, address, city, borough, latitude, longitude);
        Response<Void> request = call.execute();

        return request.isSuccessful();
    }

    public static boolean validateDate(String date) {
        return true; // TODO: Add validation logic!
    }

    public static boolean validateLocationType(String locationType) {
        return true; // TODO: Add validation logic!
    }

    public static boolean validateZipCode(String zipCode) {
        try {
            Integer.parseInt(zipCode);
            return true;
        } catch (Exception e) {
            return false; // TODO: Add validation logic!
        }
    }

    public static boolean validateAddress(String address) {
        return true; // TODO: Add validation logic!
    }

    public static boolean validateCity(String city) {
        return true; // TODO: Add validation logic!
    }

    public static boolean validateBorough(String borough) {
        return true; // TODO: Add validation logic!
    }

    public static boolean validateLatitude(String latitude) {
        try {
            BigDecimal bd = new BigDecimal(latitude);
            return true;
        } catch (Exception e) {
            return false; // TODO: Add validation logic!
        }
    }

    public static boolean validateLongitude(String longitude) {
        try {
            BigDecimal bd = new BigDecimal(longitude);
            return true;
        } catch (Exception e) {
            return false; // TODO: Add validation logic!
        }
    }

    @Override
    public int compareTo(@NonNull RatSighting ratSighting) {
        return Integer.compare(this.getId(),ratSighting.getId()) ;
    }
}
