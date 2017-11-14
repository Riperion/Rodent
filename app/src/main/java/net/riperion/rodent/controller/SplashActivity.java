package net.riperion.rodent.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.riperion.rodent.R;

/**
 * This activity handles the splash screen view.
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_new);
    }

    /**
     * This method handles presses on the login button
     * @param view the button that was pressed (the login button)
     */
    public void onLoginPressed(@SuppressWarnings("UnusedParameters") View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * This method handles presses on the register button
     * @param view the button that was pressed (the register button)
     */
    public void onRegisterPressed(@SuppressWarnings("UnusedParameters") View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
