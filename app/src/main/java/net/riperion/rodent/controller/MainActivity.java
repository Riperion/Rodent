package net.riperion.rodent.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import net.riperion.rodent.R;
import net.riperion.rodent.model.User;

/**
 * This activity is now deprecated - it used to be the landing page after login
 */
@Deprecated
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView messageView = (TextView) findViewById(R.id.textView2);
        messageView.setText("Welcome to Rodent, " + User.getCurrentUsername());
    }

    /**
     * Handle a logout button press by logging the user out and moving back to the splash activity
     * @param view The button that is pressed on
     */
    public void onLogoutPressed(View view) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
