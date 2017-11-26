package net.riperion.rodent.model;

import net.riperion.rodent.RodentApp;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class contains a library of static methods that are used to communicate with the API about Rat Sighting objects.
 * It bridges the Retrofit API handler and the activities that depend on the API.
 */

public class RatSightingProvider {
    /**
     * Synchronously fetch an unfiltered list of rat sightings from the API
     * @param offset the offset to paginate from
     * @return List of RatSighting objects retrieved
     * @throws IOException any errors that might result from the API call
     */
    public static List<RatSighting> getRatSightings(int offset) throws IOException {
        Call<ListWrapper<RatSighting>> call = RodentApp.getApi().getRatSightings(UserProvider.getAuthToken().getAuthorization(), offset);
        Response<ListWrapper<RatSighting>> response = call.execute();

        ListWrapper<RatSighting> body = response.body();
        assert body != null;
        return body.getResults();
    }

    /**
     * Asynchronously fetch an unfiltered list of rat sightings from the API
     * @param callback class that will be called as the callback once values are returned
     * @param offset the offset to paginate from
     */
    public static void asyncGetRatSightings(Callback<ListWrapper<RatSighting>> callback, Integer offset) {
        Call<ListWrapper<RatSighting>> call = RodentApp.getApi().getRatSightings(UserProvider.getAuthToken().getAuthorization(), offset);
        call.enqueue(callback);
    }

    /**
     * Synchronously fetch a single rat sighting from the API using its key
     * @param id the ID of the rat sighting to search for
     * @return the corresponding RatSighting object
     * @throws IOException any errors that might result from the API call
     */
    public static RatSighting getRatSightingByKey(int id) throws IOException {
        Call<RatSighting> call = RodentApp.getApi().getRatSightingById(UserProvider.getAuthToken().getAuthorization(), id);
        Response<RatSighting> response = call.execute();

        return response.body();
    }

    /**
     * Asynchronously fetch a single rat sighting from the API using its key
     * @param id the ID of the rat sighting to search for
     * @param callback class that will be called as the callback once values are returned
     */
    public static void asyncGetRatSightingByKey(int id, Callback<RatSighting> callback) {
        Call<RatSighting> call = RodentApp.getApi().getRatSightingById(UserProvider.getAuthToken().getAuthorization(), id);
        call.enqueue(callback);
    }

    /**
     * Asynchronously fetch a list of rat sightings using a date range
     * @param callback class that will be called as the callback once values are returned
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     */
    public static void asyncGetRatSightingByDateRange(Date startDate, Date endDate, Callback<ListWrapper<RatSighting>> callback) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Call<ListWrapper<RatSighting>> call = RodentApp.getApi().getRatSightingsByDateRange(UserProvider.getAuthToken().getAuthorization(), 0, dateFormat.format(startDate), dateFormat.format(endDate));
        call.enqueue(callback);
    }

    /**
     * Synchronously send a new rat sighting report to the API
     * @param locationType the location type for this sighting
     * @param zipCode the zip code of the location
     * @param address the address of the location
     * @param city the city the sighting was in
     * @param borough the borough the sighting was in
     * @param latitude the latitude of the location of the sighting
     * @param longitude the longitude of the location of the sighting
     * @return boolean denoting whether the item was added successfully
     * @throws IOException any errors that might result from the API call
     * @throws net.riperion.rodent.model.RatSighting.InvalidRatSightingException any errors that might result from invalid inputs on the attributes
     */
    public static boolean addRatSighting(String locationType, String zipCode, String address, String city, String borough, String latitude, String longitude) throws IOException, RatSighting.InvalidRatSightingException {
        // Run the validations
        RatSighting.validateRatSightingDetails(locationType, zipCode, address, city, borough, latitude, longitude);

        // Get the correct data types
        int parsedZipCode = Integer.parseInt(zipCode);
        BigDecimal parsedLatitude = new BigDecimal(latitude);
        BigDecimal parsedLongitude = new BigDecimal(longitude);

        Call<Void> call = RodentApp.getApi().addRatSighting(UserProvider.getAuthToken().getAuthorization(), locationType, parsedZipCode, address, city, borough, parsedLatitude, parsedLongitude);
        Response<Void> request = call.execute();

        return request.isSuccessful();
    }

    /**
     * Asynchronously fetch the number of rat sightings per month
     * @param callback class that will be called as the callback once values are returned
     */
    public static void asyncGetMonthlyRatSightingCountByMonth(Callback<List<DateCountPair>> callback) {
        Call<List<DateCountPair>> call = RodentApp.getApi().getRatSightingsMonthlyStats(UserProvider.getAuthToken().getAuthorization());
        call.enqueue(callback);
    }
}
