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

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public boolean isAdmin() {
        return mIsAdmin;
    }

    public static User[] getUsers() {
        return users.toArray(new User[0]);
    }

    public static User getUserByUsername(String username) {
        for (User u: users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return u;
            }
        }

        return null;
    }

    public static boolean addUser(String username, String password, boolean isAdmin) {
        try {
            User u = new User(username, password, isAdmin);
            users.add(u);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean authenticateUser(String username, String password) {
        User u = getUserByUsername(username);

        if (u != null && u.getPassword().equals(password)) {
            currentUser = u;
            return true;
        }

        return false;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
