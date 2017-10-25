package net.riperion.rodent;

import android.app.Application;
import android.content.Context;

import net.riperion.rodent.model.RodentAPI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cgokmen on 10/10/17.
 */

public class RodentApp extends Application {
    private static Context mContext;
    private static RodentAPI api;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://rodent.riperion.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(RodentAPI.class);
    }

    /**
     * Gets the singleton API client instance for use by all models
     * @return API client instance
     */
    public static RodentAPI getApi() {
        return api;
    }

    /**
     * Allows access to the singleton application context
     * @return the application context
     */
    public static Context getContext(){
        return mContext;
    }
}
