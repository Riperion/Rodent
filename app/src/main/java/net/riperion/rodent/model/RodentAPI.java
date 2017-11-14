package net.riperion.rodent.model;

import java.math.BigDecimal;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * This interface contains the API methods the current Rodent API is compatible with.
 * It is used by the Retrofit library to generate a callable API object.
 */

public interface RodentAPI {
    @POST("/api/auth/users/create/")
    Call<Void> createUser(@Body User.UserWrapper userWrapper);

    @POST("/api/auth/token/create/")
    Call<AuthToken> login(@Body User.UserWrapper userWrapper);

    @POST("/api/auth/token/destroy/")
    Call<Void> logout(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("/api/ratsightings/")
    Call<Void> addRatSighting(@Header("Authorization") String authorization, @Field("location_type") String locationType, @Field("zip_code") Integer zipCode, @Field("address") String address, @Field("city") String city, @Field("borough") String borough, @Field("latitude") BigDecimal latitude, @Field("longitude") BigDecimal longitude);

    @GET("/api/ratsightings/")
    Call<ListWrapper<RatSighting>> getRatSightings(@Header("Authorization") String authorization, @Query("offset") Integer offset);

    @GET("/api/ratsightings/")
    Call<ListWrapper<RatSighting>> getRatSightingsByDateRange(@Header("Authorization") String authorization, @Query("offset") Integer offset, @Query("date_created_0") String from, @Query("date_created_1") String to);

    @GET("/api/ratsightings/{id}/")
    Call<RatSighting> getRatSightingById(@Header("Authorization") String authorization, @Path("id") int id);

    @GET("/api/stats/")
    Call<List<DateCountPair>> getRatSightingsMonthlyStats(@Header("Authorization") String authorization);
}
