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
//    @GET("api/directions/json?key=AIzaSyC22GfkHu9FdgT9SwdCWMwKX1a4aohGifM")
    //@GET("api/directions/json?key=AIzaSyAGujRe5zbOC9YzpiHCOBnE0BJYZ-RvIGI")
    @GET("api/directions/json?key=AIzaSyDKVGF_OnLWj9lOPppNTH29BANogajV39Q")
    Call<Example> getDistanceDuration(@Query("units") String units, @Query("origin") String origin, @Query("destination") String destination, @Query("mode") String mode);

    @GET("api/place/nearbysearch/json?rankby=distance&key=AIzaSyDKVGF_OnLWj9lOPppNTH29BANogajV39Q")
    Call<NearbySearch> getNearestDistance(@Query("location") String location);

    @GET("api/geocode/json?key=AIzaSyDKVGF_OnLWj9lOPppNTH29BANogajV39Q")
    Call<NearbySearch> getGeocodeReverseAddress(@Query("location") String location);

    //https://maps.googleapis.com/maps/api/place/details/json?place_id=ChIJN1t_tDeuEmsRUsoyG83frY4&fields=name,rating,formatted_phone_number&key=YOUR_API_KEY
    @GET("api/place/details/json?key=AIzaSyDKVGF_OnLWj9lOPppNTH29BANogajV39Q")
    Call<Detail> getDetails(@Query("place_id") String place_id);

}
