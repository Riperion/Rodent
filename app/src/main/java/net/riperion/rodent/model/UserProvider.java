package net.riperion.rodent.model;

import net.riperion.rodent.RodentApp;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class contains a library of static methods that are used to communicate with the API about User objects.
 * It bridges the Retrofit API handler and the activities that depend on the API.
 */

public class UserProvider {
    private static String currentUsername;
    private static AuthToken authToken;

    /**
     * Creates a new user instance and adds it to the user database
     *
     * @param username the username of the user to add
     * @param password the password of the user to add
     * @return whether or not the user was added successful
     * @throws User.InvalidUserException if the username or password is invalid
     * @throws IOException if an error occurs while trying to connect to the API
     */
    public static boolean addUser(String username, String password) throws IOException, User.InvalidUserException {
        User.validateUserDetails(username, password);

        Call<Void> call = RodentApp.getApi().createUser(new UserCredentialWrapper(username, password));
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
        Call<AuthToken> call = RodentApp.getApi().login(new UserCredentialWrapper(username, password));
        Response<AuthToken> response = call.execute();

        if (response.code() == 200) {
            authToken = response.body();
            currentUsername = username;
            return true;
        }

        return false;
    }

    /**
     * Send a logout call to the API so that our token is invalidated
     * @param callback the class to issue callbacks on once a response is received
     */
    public static void logoutUser(Callback<Void> callback) {
        Call<Void> call = RodentApp.getApi().logout(authToken.getAuthorization());
        call.enqueue(callback);
    }

    /**
     * Get the currently logged in user's username
     * @return String containing the user's username
     */
    public static String getCurrentUsername() {
        return currentUsername;
    }

    /**
     * Get the singleton authentication token representing the present session on the API
     * @return the present authentication token
     */
    static AuthToken getAuthToken() {
        return authToken;
    }

    /**
     * Wraps user creation credentials for the API call
     */
    static class UserCredentialWrapper {
        final String username;
        final String password;

        /**
         * Instantiate a UserCredentialWrapper for use in a registration API call
         * @param username the username to register with
         * @param password the password to register with
         */
        UserCredentialWrapper(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
