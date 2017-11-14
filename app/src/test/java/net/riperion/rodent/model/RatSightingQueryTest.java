package net.riperion.rodent.model;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by cgokmen on 11/13/17.
 */
public class RatSightingQueryTest {
    @BeforeClass
    public static void loginTestUser() {
        try {
            assertTrue(User.authenticateUser("tester", "1234567a"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void logoutTestUser() {
        User.logoutUser(null);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void invalidAddress() throws IOException, RatSighting.InvalidRatSightingException {
        thrown.expect(RatSighting.InvalidRatSightingException.class);
        thrown.expect(hasProperty("reason", is(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_ADDRESS)));

        RatSightingQuery.addRatSighting("Valid", "99999", "", "valid", "valid", "0", "0");
    }
}