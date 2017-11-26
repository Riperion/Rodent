package net.riperion.rodent.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents statically the User that is currently authenticated on the application.
 */

public class User {
    /**
     * Validates a set of User detail input against requirements for the fields.
     * @param username the username to validate
     * @param password the password to validate
     * @throws InvalidUserException an exception containing validation error reasons if validation fails
     */
    public static void validateUserDetails(String username, String password) throws InvalidUserException {
        List<InvalidUserException.InvalidUserReason> reasons = new ArrayList<>();
        if (!validateUsername(username)) {
            reasons.add(InvalidUserException.InvalidUserReason.BAD_USERNAME);
        }
        if (!validatePassword(password)) {
            reasons.add(InvalidUserException.InvalidUserReason.BAD_PASSWORD);
        }

        if (!reasons.isEmpty()) {
            throw new InvalidUserException(reasons);
        }
    }

    /**
     * Validates a given username input
     * @param username the username input to validate
     * @return whether or not the input is valid
     */
    private static boolean validateUsername(String username) {
        return username.length() >= 8;
    }

    /**
     * Validates a given password input
     * @param password the password input to validate
     * @return whether or not the input is valid
     */
    private static boolean validatePassword(String password) {
        return (password.length() >= 8) && // Not too short
                        !password.toUpperCase().equals(password) && // Not all uppercase
                        !password.toLowerCase().equals(password) && // Not all lowercase
                        password.matches(".*\\d+.*"); // Contains at least one digit
    }

    /**
     * Exception to be thrown by validator in case an user validation exception occurs
     */
    public static class InvalidUserException extends Exception {
        public enum InvalidUserReason {
            BAD_USERNAME,
            BAD_PASSWORD
        }

        private final List<InvalidUserReason> reasons;

        /**
         * Constructs an instance of the exception to be thrown when User detail validation fails.
         * @param reasons the reasons of the failure of the validation
         */
        public InvalidUserException(List<InvalidUserReason> reasons) {
            super("Invalid user input.");
            this.reasons = new ArrayList<>(reasons);
        }

        /**
         * Gets the reasons of the invalidity of the User details input
         * @return a list of reasons of invalidity
         */
        public List<InvalidUserReason> getReasons() {
            return reasons;
        }
    }
}
