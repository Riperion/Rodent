package net.riperion.rodent;

import android.app.Application;

import net.riperion.rodent.model.RodentAPI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class represents the Rodent application and is used to store the singleton API
 * connection handler object.
 */

public class RodentApp extends Application {
    private static final RodentAPI api = new Retrofit.Builder()
            .baseUrl("https://rodent.riperion.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RodentAPI.class);

    /**
     * Gets the singleton API client instance for use by all models
     * @return API client instance
     */
    public static RodentAPI getApi() {
        return api;
    }
}
