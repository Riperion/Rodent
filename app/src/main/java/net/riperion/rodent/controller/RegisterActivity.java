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
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.riperion.rodent.R;
import net.riperion.rodent.model.User;
import net.riperion.rodent.model.UserProvider;

import java.io.IOException;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    @Nullable
    private UserRegisterTask mRegisterTask = null;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
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
                if ((id == R.id.register) || (id == EditorInfo.IME_NULL)) {
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

        // Store values at the time of the registration attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        // Show a progress spinner, and kick off a background task to
        // perform the user registration attempt.
        showProgress(true);
        mRegisterTask = new UserRegisterTask(username, password);
        mRegisterTask.execute((Void) null);
    }

    /**
     * Updates the UI according to the response from the model and the API
     * @param success whether or not the operation succeeded
     * @param e if status is false, the exception that was raised (this will not be null then)
     */
    private void onRegisterResult(boolean success, Exception e) {
        mRegisterTask = null;
        showProgress(false);

        if (success) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Registration was successful! Click on the login button to log in.").setTitle("Success!");
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Intent intent = new Intent(RegisterActivity.this, SplashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            // There has to have been an exception
            assert e != null;

            if (e instanceof User.InvalidUserException) {
                // We handle this by highlighting the appropriate field
                EditText focusView = null;
                List<User.InvalidUserException.InvalidUserReason> reasons = ((User.InvalidUserException) e).getReasons();

                if (reasons.contains(User.InvalidUserException.InvalidUserReason.BAD_USERNAME)) {
                    mUsernameView.setError("Invalid username.");
                    focusView = mUsernameView;
                }
                if (reasons.contains(User.InvalidUserException.InvalidUserReason.BAD_USERNAME)) {
                    mPasswordView.setError("Invalid password.");
                    focusView = mPasswordView;
                }

                assert focusView != null;
                focusView.requestFocus();
            } else {
                String message = "An unexpected error occurred.";

                if (e instanceof IOException) {
                    message = "An error occurred while trying to connect to the server.";
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setMessage(message).setTitle("Oops!");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     * @param show Whether or not the progress UI should be shown
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
    private final class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private Exception exception;

        /**
         * Constructs an user registration task object with the given user details
         * @param username User's username
         * @param password User's password
         */
        private UserRegisterTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return UserProvider.addUser(mUsername, mPassword);
            } catch (Exception e) {
                this.exception = e;
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            onRegisterResult(success, this.exception);
        }

        @Override
        protected void onCancelled() {
            mRegisterTask = null;
            showProgress(false);
        }
    }
}

