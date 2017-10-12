package net.riperion.rodent.model;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by cgokmen on 9/29/17.
 */

public class User {
    private static User currentUser = null;
    private static ArrayList<User> users = new ArrayList<>();

    public static boolean validateUsername(String username) {
        return !TextUtils.isEmpty(username);
    }

    public static boolean validatePassword(String password) {
        return !TextUtils.isEmpty(password);
    }

    private String mUsername;
    private String mPassword;
    private boolean mIsAdmin;

    /**
     * Creates a new user instance
     *
     * @param username the username of the user to add
     * @param password the password of the user to add
     * @param isAdmin whether or not the user should be an admin
     * @throws IllegalArgumentException if the username or password is illegal
     * @throws IllegalArgumentException if a user with this username already exists
     */
    private User(String username, String password, boolean isAdmin) {
        if (!validateUsername(username) || !validatePassword(password)) {
            throw new IllegalArgumentException("Invalid mUsername / password");
        }

        if (getUserByUsername(username) != null) {
            throw new IllegalArgumentException("A user with this username already exists.");
        }

        this.mUsername = username.toLowerCase();
        this.mPassword = password;
        this.mIsAdmin = isAdmin;
    }

    public User(String username, String password) {
        this(username, password, false);
    }

    public String getUsername() {
        return mUsername;
    }

    public String getPassword() {
        return mPassword;
    }

    /**
     * Change the user's password
     * @param password The new password
     */
    public void setPassword(String password) {
        this.mPassword = password;
    }

    public boolean isAdmin() {
        return mIsAdmin;
    }

    /**
     * Get an array of all users from the database
     *
     * @return array of User instances for each user
     */
    public static User[] getUsers() {
        return users.toArray(new User[0]);
    }

    /**
     * Search the user database for a user with the given username
     *
     * @param username the username to search for
     * @return the User instance with the given username, null if one doesn't exist
     */
    public static User getUserByUsername(String username) {
        for (User u: users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return u;
            }
        }

        return null;
    }

    /**
     * Creates a new user instance and adds it to the user database
     *
     * @param username the username of the user to add
     * @param password the password of the user to add
     * @param isAdmin whether or not the user should be an admin
     * @throws IllegalArgumentException if the username or password is illegal
     * @throws IllegalArgumentException if a user with this username already exists
     */
    public static boolean addUser(String username, String password, boolean isAdmin) {
        try {
            User u = new User(username, password, isAdmin);
            users.add(u);

            return true;
            // TODO: Propagate exception
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Authenticates a user by validating username/password and setting currentUser singleton
     * @param username the username of the user to authenticate
     * @param password the password the user is trying to log in with
     * @return whether or not the login was successful
     */
    public static boolean authenticateUser(String username, String password) {
        User u = getUserByUsername(username);

        if (u != null && u.getPassword().equals(password)) {
            currentUser = u;
            return true;
        }

        return false;
    }

    /**
     * Get the currently logged in user
     * @return User instance for the logged in user
     */
    public static User getCurrentUser() {
        return currentUser;
    }
}
