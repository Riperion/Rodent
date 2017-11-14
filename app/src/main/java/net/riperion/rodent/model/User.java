package net.riperion.rodent.model;

import android.text.TextUtils;

import net.riperion.rodent.RodentApp;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class represents statically the User that is currently authenticated on the application.
 */

public class User {
    public static boolean validateUsername(String username) {
        return !TextUtils.isEmpty(username);
    }

    public static boolean validatePassword(String password) {
        return !TextUtils.isEmpty(password);
    }

    private static String currentUsername;
    private static AuthToken authToken;

    /**
     * Creates a new user instance and adds it to the user database
     *
     * @param username the username of the user to add
     * @param password the password of the user to add
     * @return whether or not the user was added successful
     * @throws InvalidUserException if the username or password is invalid
     * @throws IOException if an error occurs while trying to connect to the API
     */
    public static boolean addUser(String username, String password) throws IOException, InvalidUserException {
        if (!validateUsername(username))
            throw new InvalidUserException(InvalidUserException.InvalidUserReason.BAD_USERNAME);

        if (!validatePassword(password))
            throw new InvalidUserException(InvalidUserException.InvalidUserReason.BAD_PASSWORD);

        Call<Void> call = RodentApp.getApi().createUser(new UserWrapper(username, password));
        Response<Void> response = call.execute();

        return response.isSuccessful();
    }

    /**
     * Authenticates a user by validating username/password and setting currentUser singleton
     * @param username the username of the user to authenticate
     * @param password the password the user is trying to log in with
     * @return whether or not the login was successful
     * @throws IOException if an error occurs while trying to connect to the API
     */
    public static boolean authenticateUser(String username, String password) throws IOException {
        Call<AuthToken> call = RodentApp.getApi().login(new UserWrapper(username, password));
        Response<AuthToken> response = call.execute();

        if (response.code() < 400) {
            authToken = response.body();
            currentUsername = username;
            return true;
        }

        return false;
    }

    public static void logoutUser(Callback<Void> cb) {
        Call<Void> call = RodentApp.getApi().logout(authToken.getAuthorization());
        call.enqueue(cb);
    }

    /**
     * Get the currently logged in user's username
     * @return String containing the user's username
     */
    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static AuthToken getAuthToken() {
        return authToken;
    }

    static class UserWrapper {
        public final String username;
        public final String password;

        public UserWrapper(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    public static class InvalidUserException extends Exception {
        public enum InvalidUserReason {
            BAD_USERNAME,
            BAD_PASSWORD
        }

        private final InvalidUserReason reason;

        public InvalidUserException(InvalidUserReason reason) {
            super(reason.name());
            this.reason = reason;
        }

        public InvalidUserReason getReason() {
            return reason;
        }
    }
}
