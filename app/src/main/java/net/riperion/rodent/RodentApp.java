package net.riperion.rodent;

import android.app.Application;
import android.content.Context;

/**
 * Created by cgokmen on 10/10/17.
 */

public class RodentApp extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    /**
     * Allows access to the singleton application context
     * @return the application context
     */
    public static Context getContext(){
        return mContext;
    }
}
