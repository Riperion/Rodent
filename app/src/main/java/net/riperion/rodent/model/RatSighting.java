package net.riperion.rodent.model;

import android.util.Log;

import net.riperion.rodent.R;
import net.riperion.rodent.RodentApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cgokmen on 10/10/17.
 */

public class RatSighting {
    private static ArrayList<RatSighting> ratSightingList = new ArrayList<>();
    private static HashMap<Integer, RatSighting> ratSightingMap = new HashMap<>();
    private static boolean loaded = false;

    private int key;
    private String dateCreated;
    private String locationType;
    private String zipCode;
    private String address;
    private String city;
    private String borough;
    private String latitude;
    private String longitude;

    /**
     * Construct a new Rat Sighting instance
     * @param key the unique key for this sighting
     * @param dateCreated the creation date of this sighting
     * @param locationType the location type for this sighting
     * @param zipCode the zip code of the location
     * @param address the address of the location
     * @param city the city the sighting was in
     * @param borough the borough the sighting was in
     * @param latitude the latitude of the location of the sighting
     * @param longitude the longitude of the location of the sighting
     */
    public RatSighting(int key, String dateCreated, String locationType, String zipCode, String address, String city, String borough, String latitude, String longitude) {
        this.key = key;
        this.dateCreated = dateCreated;
        this.locationType = locationType;
        this.zipCode = zipCode;
        this.address = address;
        this.city = city;
        this.borough = borough;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getKey() {
        return key;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getLocationType() {
        return locationType;
    }

    public String getZipCode() {
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

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    /**
     * Constructs a string containing all details of a rat sighting
     * @return string containing rat sighting details
     */
    public String getDetails() {
        String result = "";
        result += String.format("Key: %d%n", key);
        result += String.format("Date created: %s%n", dateCreated);
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

    public void setZipCode(String zipCode) {
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

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public static List<RatSighting> getRatSightings() {
        return ratSightingList;
    }

    public static RatSighting getRatSightingByKey(int key) {
        return ratSightingMap.get(key);
    }


    /**
     * Adds a rat sighting onto the database
     *
     * @param key the unique key for this sighting
     * @param dateCreated the creation date of this sighting
     * @param locationType the location type for this sighting
     * @param zipCode the zip code of the location
     * @param address the address of the location
     * @param city the city the sighting was in
     * @param borough the borough the sighting was in
     * @param latitude the latitude of the location of the sighting
     * @param longitude the longitude of the location of the sighting
     */
    public static void addRatSighting(int key, String dateCreated, String locationType, String zipCode, String address, String city, String borough, String latitude, String longitude) {
        RatSighting r = new RatSighting(key, dateCreated, locationType, zipCode, address, city, borough, latitude, longitude);
        ratSightingList.add(r);
        ratSightingMap.put(r.key, r);
    }

    /**
     * Loads rat sightings from database into memory
     */
    public static void loadRatSightings() {
        if (!loaded) {
            loaded = true;
            InputStream inputStream = RodentApp.getContext().getResources().openRawResource(R.raw.data);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                String csvLine = reader.readLine();
                while ((csvLine = reader.readLine()) != null) {
                    String[] row = csvLine.split(",", -1);
                    //Log.i("Line", csvLine);
                    //Log.i("entry count", "" + row.length);

                    int key = Integer.parseInt(row[0]);
                    //Log.i("key", "" + key);

                    String dateCreated = row[1];
                    String locationType = row[7];
                    String zipCode = row[8];
                    String address = row[9];
                    String city = row[16];
                    String borough = row[23];
                    String latitude = row[49];
                    String longitude = row[50];

                    addRatSighting(key, dateCreated, locationType, zipCode, address, city, borough, latitude, longitude);
                }

                Log.i("Loaded", String.format("Loaded %d rat sightings", ratSightingList.size()));
            } catch (IOException ex) {
                throw new RuntimeException("Error in reading CSV file: " + ex);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException("Error while closing input stream: " + e);
                }
            }
        }
    }
}
