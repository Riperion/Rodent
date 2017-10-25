package net.riperion.rodent.controller;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import net.riperion.rodent.R;
import net.riperion.rodent.model.User;

import java.util.List;

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
    public void onLogoutPressed(View view) {
        User.logoutUser(this);
    }

    /**
     * Handle a logout button press by logging the user out and moving back to the splash activity
     * @param view The button that is pressed on
     */
    public void onListPressed(View view) {
        Intent intent = new Intent(this, RatSightingListActivity.class);
        startActivity(intent);
    }

    /**
     * Handle a logout button press by logging the user out and moving back to the splash activity
     * @param view The button that is pressed on
     */
    public void onQueryPressed(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This feature does not exist yet!").setTitle("Oops!");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Handle a logout button press by logging the user out and moving back to the splash activity
     * @param view The button that is pressed on
     */
    public void onMapPressed(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResponse(Call<Void> call, Response<Void> response) {
        goHome();
    }

    @Override
    public void onFailure(Call<Void> call, Throwable t) {
        goHome();
    }

    /**
     * When the logout button is hit and the logout action succeeds, we go home
     */
    public void goHome() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
