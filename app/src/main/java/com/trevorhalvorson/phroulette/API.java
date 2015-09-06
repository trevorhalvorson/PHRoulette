package com.trevorhalvorson.phroulette;

import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Trevor on 9/5/2015.
 */
public interface API {

    @GET("/posts?=")
    void getProduct(
            @Query("days_ago") String days,
            Callback<JsonObject> response);

}
