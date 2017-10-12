package net.riperion.rodent.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import net.riperion.rodent.R;
import net.riperion.rodent.model.RatSighting;

public class SplashActivity extends AppCompatActivity {

    private RatSightingLoadTask mLoadTask;
    private View mProgressView;
    private View mSplashView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mSplashView = findViewById(R.id.splash_view);
        mProgressView = findViewById(R.id.progress_view);

        showProgress(true);
        mLoadTask = new SplashActivity.RatSightingLoadTask(this);
        mLoadTask.execute((Void) null);
    }

    public void onLoginPressed(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void onRegisterPressed(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mSplashView.setVisibility(show ? View.GONE : View.VISIBLE);
        mSplashView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mSplashView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }



    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class RatSightingLoadTask extends AsyncTask<Void, Void, Boolean> {

        private final SplashActivity mActivity;

        RatSightingLoadTask(SplashActivity activity) {
            mActivity = activity;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Simulate network access.
                RatSighting.loadRatSightings();
                return true;
            } catch (Exception e) {
                Log.e("HI", e.toString());
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mLoadTask = null;
            showProgress(false);

            if (!success) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

                builder.setMessage("Rat sighting loading failed.").setTitle("Oops!");
                builder.setCancelable(false);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

        @Override
        protected void onCancelled() {
            mLoadTask = null;
            showProgress(false);
        }
    }
}
