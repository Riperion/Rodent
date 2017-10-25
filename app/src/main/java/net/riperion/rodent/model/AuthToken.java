package net.riperion.rodent.model;

/**
 * Created by cgokmen on 10/25/17.
 */

class AuthToken {
    private String auth_token;

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
    public String get_auth_token() {
        return auth_token;
    }

    /**
     * Gets the authorization header contents to be used on API calls
     * @return authorization header contents
     */
    public String get_authorization() {
        return "Token " + auth_token;
    }
}
