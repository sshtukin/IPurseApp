package com.ipurse;

import com.ipurse.models.googleMap.MyPlaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IGoogleApi {
    @GET
    Call<MyPlaces> getNearByPlaces(@Url String url);
}
