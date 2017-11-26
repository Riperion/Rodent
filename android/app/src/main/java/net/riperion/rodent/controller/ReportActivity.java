package net.riperion.rodent.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.riperion.rodent.R;
import net.riperion.rodent.model.RatSighting;
import net.riperion.rodent.model.RatSightingProvider;

import java.io.IOException;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class ReportActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    @Nullable
    private ReportSightingTask mReportTask = null;

    // UI references.
    private EditText mLocationTypeView;
    private EditText mZipCodeView;
    private EditText mAddressView;
    private EditText mCityView;
    private EditText mBoroughView;
    private EditText mLatitudeView;
    private EditText mLongitudeView;
    private View mProgressView;
    private View mReportFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        // Set up the login form.
        mLocationTypeView = (EditText) findViewById(R.id.location_type_input);
        mZipCodeView = (EditText) findViewById(R.id.zipcode_input);
        mAddressView = (EditText) findViewById(R.id.address_input);
        mCityView = (EditText) findViewById(R.id.city_input);
        mBoroughView = (EditText) findViewById(R.id.borough_input);
        mLatitudeView = (EditText) findViewById(R.id.latitude_input);
        mLongitudeView = (EditText) findViewById(R.id.longitude_input);

        mLongitudeView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if ((id == R.id.report) || (id == EditorInfo.IME_NULL)) {
                    attemptReport();
                    return true;
                }
                return false;
            }
        });

        Button mLoginButton = (Button) findViewById(R.id.report_button);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptReport();
            }
        });

        mReportFormView = findViewById(R.id.report_form);
        mProgressView = findViewById(R.id.report_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptReport() {
        if (mReportTask != null) {
            return;
        }

        // Reset errors.
        mLongitudeView.setError(null);

        // Store values at the time of the login attempt.
        String locationType = mLocationTypeView.getText().toString();
        String zipCode = mZipCodeView.getText().toString();
        String address = mAddressView.getText().toString();
        String city = mCityView.getText().toString();
        String borough = mBoroughView.getText().toString();
        String latitude = mLatitudeView.getText().toString();
        String longitude = mLongitudeView.getText().toString();

        showProgress(true);
        mReportTask = new ReportSightingTask(locationType, zipCode, address, city, borough, latitude, longitude);
        mReportTask.execute((Void) null);
    }

    /**
     * Updates the UI according to the response from the model and the API
     * @param success whether or not the operation succeeded
     * @param e if success is false, the exception that was raised (this will not be null then)
     */
    private void onReportResult(boolean success, Exception e) {
        mReportTask = null;
        showProgress(false);

        if (success) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ReportActivity.this);

            builder.setMessage("You have successfully filed a report.").setTitle("Success!");
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    ReportActivity.this.onBackPressed();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            // There has to have been an exception
            assert e != null;

            if (e instanceof RatSighting.InvalidRatSightingException) {
                // We handle this by highlighting the appropriate field
                EditText focusView = null;

                List<RatSighting.InvalidRatSightingException.InvalidRatSightingReason> reasons = (((RatSighting.InvalidRatSightingException) e).getReasons());
                if (reasons.contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_LOCATION_TYPE)) {
                    focusView = mLocationTypeView;
                    focusView.setError("Invalid input.");
                } if (reasons.contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_ZIP_CODE)) {
                    focusView = mZipCodeView;
                    focusView.setError("Invalid input.");
                } if (reasons.contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_ADDRESS)) {
                    focusView = mAddressView;
                    focusView.setError("Invalid input.");
                } if (reasons.contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_CITY)) {
                    focusView = mCityView;
                    focusView.setError("Invalid input.");
                } if (reasons.contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_BOROUGH)) {
                    focusView = mBoroughView;
                    focusView.setError("Invalid input.");
                } if (reasons.contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_LATITUDE)) {
                    focusView = mLatitudeView;
                    focusView.setError("Invalid input.");
                } if (reasons.contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_LONGITUDE)) {
                    focusView = mLongitudeView;
                    focusView.setError("Invalid input.");
                }

                assert focusView != null;
                focusView.requestFocus();
            } else {
                String message = "An unexpected error occurred.";

                if (e instanceof IOException) {
                    message = "An error occurred while trying to connect to the server.";
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(ReportActivity.this);
                builder.setMessage(message).setTitle("Oops!");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     * @param show Whether the progress bar should be showed or hidden
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mReportFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mReportFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mReportFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
     * Represents an asynchronous task that calls synchronous model functions in order to add
     * a new Rat Sighting Report to the database.
     */
    private final class ReportSightingTask extends AsyncTask<Void, Void, Boolean> {
        private final String locationType;
        private final String zipCode;
        private final String address;
        private final String city;
        private final String borough;
        private final String latitude;
        private final String longitude;

        private Exception exception;

        /**
         * Instantiate a ReportSightingTask to process a new Rat Sighting Report
         * @param locationType the location type for this sighting
         * @param zipCode the zip code of the location
         * @param address the address of the location
         * @param city the city the sighting was in
         * @param borough the borough the sighting was in
         * @param latitude the latitude of the location of the sighting
         * @param longitude the latitude of the location of the sighting
         */
        private ReportSightingTask(String locationType, String zipCode, String address, String city, String borough, String latitude, String longitude) {
            this.locationType = locationType;
            this.zipCode = zipCode;
            this.address = address;
            this.city = city;
            this.borough = borough;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return RatSightingProvider.addRatSighting(locationType, zipCode, address, city, borough, latitude, longitude);
            } catch (Exception e) {
                this.exception = e;
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            onReportResult(success, exception);
        }

        @Override
        protected void onCancelled() {
            mReportTask = null;
            showProgress(false);
        }
    }
}

