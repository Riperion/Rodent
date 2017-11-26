package net.riperion.rodent.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.*;

/**
 * Tests the validateUserDetails method on the User class
 */
public class UserValidationTest {
    @Test
    public void validEverything() throws IOException, User.InvalidUserException {
        User.validateUserDetails("abcdefgh", "123456Aa");
        User.validateUserDetails("abcdefghijk", "AsdAsdAsd1");
        User.validateUserDetails("abcdefg123456a", "AAAAAAa1");
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void invalidUsername() throws IOException, User.InvalidUserException {
        thrown.expect(User.InvalidUserException.class);
        thrown.expect(hasProperty("reasons", contains(User.InvalidUserException.InvalidUserReason.BAD_USERNAME)));

        User.validateUserDetails("abcdefg", "123456Aa");
    }

    @Test
    public void invalidPasswordAllUppercase() throws IOException, User.InvalidUserException {
        thrown.expect(User.InvalidUserException.class);
        thrown.expect(hasProperty("reasons", contains(User.InvalidUserException.InvalidUserReason.BAD_PASSWORD)));

        User.validateUserDetails("abcdefghijk", "AAAAAA12");
    }

    @Test
    public void invalidPasswordAllLowercase() throws IOException, User.InvalidUserException {
        thrown.expect(User.InvalidUserException.class);
        thrown.expect(hasProperty("reasons", contains(User.InvalidUserException.InvalidUserReason.BAD_PASSWORD)));

        User.validateUserDetails("abcdefghijk", "aaaaaa12");
    }

    @Test
    public void invalidPasswordNoDigits() throws IOException, User.InvalidUserException {
        thrown.expect(User.InvalidUserException.class);
        thrown.expect(hasProperty("reasons", contains(User.InvalidUserException.InvalidUserReason.BAD_PASSWORD)));

        User.validateUserDetails("abcdefghijk", "AAAAAAAa");
    }

    @Test
    public void invalidPasswordAllDigits() throws IOException, User.InvalidUserException {
        thrown.expect(User.InvalidUserException.class);
        thrown.expect(hasProperty("reasons", contains(User.InvalidUserException.InvalidUserReason.BAD_PASSWORD)));

        User.validateUserDetails("abcdefghijk", "12345678");
    }

    @Test
    public void invalidPasswordTooShort() throws IOException, User.InvalidUserException {
        thrown.expect(User.InvalidUserException.class);
        thrown.expect(hasProperty("reasons", contains(User.InvalidUserException.InvalidUserReason.BAD_PASSWORD)));

        User.validateUserDetails("abcdefghijk", "AAaa12");
    }
}