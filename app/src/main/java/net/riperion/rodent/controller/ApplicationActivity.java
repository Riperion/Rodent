package net.riperion.rodent.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import net.riperion.rodent.R;
import net.riperion.rodent.model.UserProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This activity contains the main navigation center for all of the app's features.
 */
public class ApplicationActivity extends AppCompatActivity implements Callback<Void> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    /**
     * Handle a logout button press by logging the user out and moving back to the splash activity
     * @param view The button that is pressed on
     */
    public void onLogoutPressed(@SuppressWarnings("UnusedParameters") View view) {
        UserProvider.logoutUser(this);
    }

    /**
     * Handle a list button press by launching the list activity
     * @param view The button that is pressed on
     */
    public void onListPressed(@SuppressWarnings("UnusedParameters") View view) {
        Intent intent = new Intent(this, RatSightingListActivity.class);
        startActivity(intent);
    }

    /**
     * Handle a query button press by launching the graph activity
     * @param view The button that is pressed on
     */
    public void onQueryPressed(@SuppressWarnings("UnusedParameters") View view) {
        Intent intent = new Intent(this, GraphActivity.class);
        startActivity(intent);
    }

    /**
     * Handle a map button press by launching the map activity
     * @param view The button that is pressed on
     */
    public void onMapPressed(@SuppressWarnings("UnusedParameters") View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
        goHome();
    }

    @Override
    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
        goHome();
    }

    /**
     * When the logout button is hit and the logout action succeeds, we go home
     */
    private void goHome() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
