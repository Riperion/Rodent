package net.riperion.rodent.model;

/**
 * This class represents the authentication token that is obtained from the API on login
 * and sent with each the API call for authentication.
 */

class AuthToken {
    private final String auth_token;

    /**
     * Creates an authentication token instance - for use by the GSON deserializer
     * @param auth_token the authentication token string
     */
    public AuthToken(String auth_token) {
        this.auth_token = auth_token;
    }

    /**
     * Gets the authentication token string
     * @return the token string
     */
    public String getAuthToken() {
        return auth_token;
    }

    /**
     * Gets the authorization header contents to be used on API calls
     * @return authorization header contents
     */
    public String getAuthorization() {
        return "Token " + auth_token;
    }
}
