package net.riperion.rodent;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    public void onLoginPressed(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void onRegisterPressed(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("The register dialog is not ready yet! Try to login instead.").setTitle("Oops!");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
