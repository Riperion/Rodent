package net.riperion.rodent.model;

import android.text.TextUtils;

import net.riperion.rodent.RodentApp;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by cgokmen on 9/29/17.
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
     * @param isAdmin whether or not the user should be an admin
     * @throws IllegalArgumentException if the username or password is illegal
     * @throws IllegalArgumentException if a user with this username already exists
     * @return whether or not the user was added successfully
     */
    public static boolean addUser(String username, String password, boolean isAdmin) throws IOException {
        Call<Void> call = RodentApp.getApi().createUser(new UserWrapper(username, password));
        Response<Void> response = call.execute();

        return response.isSuccessful();

        // TODO: What do we do about isAdmin?
    }

    /**
     * Authenticates a user by validating username/password and setting currentUser singleton
     * @param username the username of the user to authenticate
     * @param password the password the user is trying to log in with
     * @return whether or not the login was successful
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
        // TODO: Propagate exception
    }

    public static void logoutUser(Callback<Void> cb) {
        Call<Void> call = RodentApp.getApi().logout(authToken.get_authorization());
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
        public String username;
        public String password;

        public UserWrapper(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
