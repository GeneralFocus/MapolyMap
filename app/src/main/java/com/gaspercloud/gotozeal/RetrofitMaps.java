package com.gaspercloud.gotozeal;


import com.gaspercloud.gotozeal.POJO.Detail.Detail;
import com.gaspercloud.gotozeal.POJO.Example;
import com.gaspercloud.gotozeal.POJO.Nearby.NearbySearch;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by navneet on 17/7/16.
 */
public interface RetrofitMaps {

    /*
     * Retrofit get annotation with our URL
     * And our method that will return us details of student.
     */
    @GET("api/directions/json?key=your_api_key")
    Call<Example> getDistanceDuration(@Query("units") String units, @Query("origin") String origin, @Query("destination") String destination, @Query("mode") String mode);

    @GET("api/place/nearbysearch/json?rankby=distance&key=your_api_key")
    Call<NearbySearch> getNearestDistance(@Query("location") String location);

    @GET("api/geocode/json?key=your_api_key")
    Call<NearbySearch> getGeocodeReverseAddress(@Query("location") String location);

    @GET("api/place/details/json?key=your_api_key")
    Call<Detail> getDetails(@Query("place_id") String place_id);

}
