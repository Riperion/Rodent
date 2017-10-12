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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import net.riperion.rodent.R;
import net.riperion.rodent.model.User;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserRegisterTask mRegisterTask = null;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private Switch mIsAdminView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.register || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        Button mLoginButton = (Button) findViewById(R.id.register_button);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mLoginFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
        mIsAdminView = (Switch) findViewById(R.id.admin_switch);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        if (mRegisterTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean isAdmin = mIsAdminView.isChecked();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!User.validatePassword(password)) {
            mPasswordView.setError("Invalid password.");
            focusView = mPasswordView;
            cancel = true;
        }

        if (!User.validateUsername(username)) {
            mUsernameView.setError("This field cannot be blank.");
            focusView = mUsernameView;
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
            mRegisterTask = new UserRegisterTask(username, password, isAdmin);
            mRegisterTask.execute((Void) null);
        }
    }

    private void onPostRegister(boolean success) {
        mRegisterTask = null;
        showProgress(false);

        if (success) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            final RegisterActivity thisActivity = this;

            builder.setMessage("Registration was successful! Click on the login button to log in.").setTitle("Success!");
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Intent intent = new Intent(thisActivity, SplashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            mPasswordView.setError("Unknown error.");
            mPasswordView.requestFocus();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
    private class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private final boolean mIsAdmin;

        UserRegisterTask(String username, String password, boolean isAdmin) {
            mUsername = username;
            mPassword = password;
            mIsAdmin = isAdmin;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return false;
            }

            try {
                return User.addUser(mUsername, mPassword, mIsAdmin);
            } catch (Exception e) {
                // TODO: Add some sort of error
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            onPostRegister(success);
        }

        @Override
        protected void onCancelled() {
            mRegisterTask = null;
            showProgress(false);
        }
    }
}

