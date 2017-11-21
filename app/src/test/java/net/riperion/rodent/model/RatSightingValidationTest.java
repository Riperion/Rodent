package net.riperion.rodent.model;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Tests the validateRatSighting method on the RatSighting class
 */
public class RatSightingValidationTest {
    @Test
    public void validEverything() throws IOException, RatSighting.InvalidRatSightingException {
        RatSighting.validateRatSightingDetails("valid", "99999", "valid", "valid", "valid", "0", "0");
        RatSighting.validateRatSightingDetails("valid", "30332", "valid", "valid", "valid", "89", "179");
        RatSighting.validateRatSightingDetails("valid", "12345", "valid", "valid", "valid", "-89", "-179");
        RatSighting.validateRatSightingDetails("valid", "01004", "valid", "valid", "valid", "-90", "180");
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void invalidLocationType() throws IOException, RatSighting.InvalidRatSightingException {
        thrown.expect(RatSighting.InvalidRatSightingException.class);
        thrown.expect(hasProperty("reasons", contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_LOCATION_TYPE)));

        RatSighting.validateRatSightingDetails("", "99999", "valid", "valid", "valid", "0", "0");
    }

    @Test
    public void invalidZipCode1() throws IOException, RatSighting.InvalidRatSightingException {
        thrown.expect(RatSighting.InvalidRatSightingException.class);
        thrown.expect(hasProperty("reasons", contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_ZIP_CODE)));

        RatSighting.validateRatSightingDetails("valid", "", "valid", "valid", "valid", "0", "0");
    }

    @Test
    public void invalidZipCode2() throws IOException, RatSighting.InvalidRatSightingException {
        thrown.expect(RatSighting.InvalidRatSightingException.class);
        thrown.expect(hasProperty("reasons", contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_ZIP_CODE)));

        RatSighting.validateRatSightingDetails("valid", "1", "valid", "valid", "valid", "0", "0");
    }

    @Test
    public void invalidZipCode3() throws IOException, RatSighting.InvalidRatSightingException {
        thrown.expect(RatSighting.InvalidRatSightingException.class);
        thrown.expect(hasProperty("reasons", contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_ZIP_CODE)));

        RatSighting.validateRatSightingDetails("valid", "-5555", "valid", "valid", "valid", "0", "0");
    }

    @Test
    public void invalidZipCode4() throws IOException, RatSighting.InvalidRatSightingException {
        thrown.expect(RatSighting.InvalidRatSightingException.class);
        thrown.expect(hasProperty("reasons", contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_ZIP_CODE)));

        RatSighting.validateRatSightingDetails("valid", "-55555", "valid", "valid", "valid", "0", "0");
    }

    @Test
    public void invalidZipCode5() throws IOException, RatSighting.InvalidRatSightingException {
        thrown.expect(RatSighting.InvalidRatSightingException.class);
        thrown.expect(hasProperty("reasons", contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_ZIP_CODE)));

        RatSighting.validateRatSightingDetails("valid", "00000", "valid", "valid", "valid", "0", "0");
    }

    @Test
    public void invalidAddress() throws IOException, RatSighting.InvalidRatSightingException {
        thrown.expect(RatSighting.InvalidRatSightingException.class);
        thrown.expect(hasProperty("reasons", contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_ADDRESS)));

        RatSighting.validateRatSightingDetails("valid", "99999", "", "valid", "valid", "0", "0");
    }

    @Test
    public void invalidCity() throws IOException, RatSighting.InvalidRatSightingException {
        thrown.expect(RatSighting.InvalidRatSightingException.class);
        thrown.expect(hasProperty("reasons", contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_CITY)));

        RatSighting.validateRatSightingDetails("valid", "99999", "valid", "", "valid", "0", "0");
    }

    @Test
    public void invalidBorough() throws IOException, RatSighting.InvalidRatSightingException {
        thrown.expect(RatSighting.InvalidRatSightingException.class);
        thrown.expect(hasProperty("reasons", contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_BOROUGH)));

        RatSighting.validateRatSightingDetails("valid", "99999", "valid", "valid", "", "0", "0");
    }

    @Test
    public void invalidLatitude1() throws IOException, RatSighting.InvalidRatSightingException {
        thrown.expect(RatSighting.InvalidRatSightingException.class);
        thrown.expect(hasProperty("reasons", contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_LATITUDE)));

        RatSighting.validateRatSightingDetails("valid", "99999", "valid", "valid", "valid", "asd", "0");
    }

    @Test
    public void invalidLatitude2() throws IOException, RatSighting.InvalidRatSightingException {
        thrown.expect(RatSighting.InvalidRatSightingException.class);
        thrown.expect(hasProperty("reasons", contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_LATITUDE)));

        RatSighting.validateRatSightingDetails("valid", "99999", "valid", "valid", "valid", "-900", "0");
    }

    @Test
    public void invalidLatitude3() throws IOException, RatSighting.InvalidRatSightingException {
        thrown.expect(RatSighting.InvalidRatSightingException.class);
        thrown.expect(hasProperty("reasons", contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_LATITUDE)));

        RatSighting.validateRatSightingDetails("valid", "99999", "valid", "valid", "valid", "900", "0");
    }

    @Test
    public void invalidLatitude4() throws IOException, RatSighting.InvalidRatSightingException {
        thrown.expect(RatSighting.InvalidRatSightingException.class);
        thrown.expect(hasProperty("reasons", contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_LATITUDE)));

        RatSighting.validateRatSightingDetails("valid", "99999", "valid", "valid", "valid", "91", "0");
    }

    @Test
    public void invalidLatitude5() throws IOException, RatSighting.InvalidRatSightingException {
        thrown.expect(RatSighting.InvalidRatSightingException.class);
        thrown.expect(hasProperty("reasons", contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_LATITUDE)));

        RatSighting.validateRatSightingDetails("valid", "99999", "valid", "valid", "valid", "-91", "0");
    }

    @Test
    public void invalidLongitude1() throws IOException, RatSighting.InvalidRatSightingException {
        thrown.expect(RatSighting.InvalidRatSightingException.class);
        thrown.expect(hasProperty("reasons", contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_LONGITUDE)));

        RatSighting.validateRatSightingDetails("valid", "99999", "valid", "valid", "valid", "0", "asd");
    }

    @Test
    public void invalidLongitude2() throws IOException, RatSighting.InvalidRatSightingException {
        thrown.expect(RatSighting.InvalidRatSightingException.class);
        thrown.expect(hasProperty("reasons", contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_LONGITUDE)));

        RatSighting.validateRatSightingDetails("valid", "99999", "valid", "valid", "valid", "0", "-900");
    }

    @Test
    public void invalidLongitude3() throws IOException, RatSighting.InvalidRatSightingException {
        thrown.expect(RatSighting.InvalidRatSightingException.class);
        thrown.expect(hasProperty("reasons", contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_LONGITUDE)));

        RatSighting.validateRatSightingDetails("valid", "99999", "valid", "valid", "valid", "0", "900");
    }

    @Test
    public void invalidLongitude4() throws IOException, RatSighting.InvalidRatSightingException {
        thrown.expect(RatSighting.InvalidRatSightingException.class);
        thrown.expect(hasProperty("reasons", contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_LONGITUDE)));

        RatSighting.validateRatSightingDetails("valid", "99999", "valid", "valid", "valid", "0", "181");
    }

    @Test
    public void invalidLongitude5() throws IOException, RatSighting.InvalidRatSightingException {
        thrown.expect(RatSighting.InvalidRatSightingException.class);
        thrown.expect(hasProperty("reasons", contains(RatSighting.InvalidRatSightingException.InvalidRatSightingReason.BAD_LONGITUDE)));

        RatSighting.validateRatSightingDetails("valid", "99999", "valid", "valid", "valid", "0", "-181");
    }
}