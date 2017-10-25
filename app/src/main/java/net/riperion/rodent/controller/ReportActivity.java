package net.riperion.rodent.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * A login screen that offers login via email/password.
 */
public class ReportActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
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
                if (id == R.id.report || id == EditorInfo.IME_NULL) {
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

        boolean cancel = false;
        View focusView = null;

        if (!RatSighting.validateLocationType(locationType)) {
            mLocationTypeView.setError("Invalid location type.");
            focusView = mLocationTypeView;
            cancel = true;
        }

        if (!RatSighting.validateZipCode(zipCode)) {
            mZipCodeView.setError("Invalid zip code.");
            focusView = mZipCodeView;
            cancel = true;
        }

        if (!RatSighting.validateAddress(address)) {
            mAddressView.setError("Invalid address.");
            focusView = mAddressView;
            cancel = true;
        }

        if (!RatSighting.validateCity(city)) {
            mCityView.setError("Invalid city.");
            focusView = mCityView;
            cancel = true;
        }

        if (!RatSighting.validateBorough(borough)) {
            mBoroughView.setError("Invalid borough.");
            focusView = mBoroughView;
            cancel = true;
        }

        if (!RatSighting.validateLatitude(latitude)) {
            mLatitudeView.setError("Invalid latitude.");
            focusView = mLatitudeView;
            cancel = true;
        }

        if (!RatSighting.validateLongitude(longitude)) {
            mLongitudeView.setError("Invalid longitude.");
            focusView = mLongitudeView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mReportTask = new ReportSightingTask(this, locationType, Integer.parseInt(zipCode), address, city, borough, new BigDecimal(latitude), new BigDecimal(latitude));
            mReportTask.execute((Void) null);
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

    private class ReportSightingTask extends AsyncTask<Void, Void, Boolean> {

        private final ReportActivity mActivity;
        private String locationType;
        private int zipCode;
        private String address;
        private String city;
        private String borough;
        private BigDecimal latitude;
        private BigDecimal longitude;

        ReportSightingTask(ReportActivity activity, String locationType, int zipCode, String address, String city, String borough, BigDecimal latitude, BigDecimal longitude) {
            mActivity = activity;
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
                return RatSighting.addRatSighting(locationType, zipCode, address, city, borough, latitude, longitude);
            } catch (IOException e) {
                e.printStackTrace(); // TODO: handle this
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mReportTask = null;
            showProgress(false);

            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

            if (success) {
                builder.setMessage("You have successfully filed a report.").setTitle("Success!");
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mActivity.onBackPressed();
                    }
                });
            } else {
                builder.setMessage("Something went wrong, try again.").setTitle("Oops!");
            }

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        @Override
        protected void onCancelled() {
            mReportTask = null;
            showProgress(false);
        }
    }
}

