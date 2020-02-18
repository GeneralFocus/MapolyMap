package com.gaspercloud.gotozeal.POJO;


import android.graphics.Bitmap;
import android.util.Log;

import com.gaspercloud.gotozeal.POJO.Nearby.NearbySearch;
import com.gaspercloud.gotozeal.POJO.Nearby.Result;
import com.gaspercloud.gotozeal.RetrofitMaps;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class PlacesList {

    private int id;
    private String mLikelyPlaceNames;
    private String mLikelyPlaceAddresses;
    private List<String> mLikelyPlaceAttributions;
    private LatLng mLikelyPlaceLatLngs;
    private Bitmap bitmap;
    private String photoRef;

    public PlacesList(int id, String mLikelyPlaceNames, String mLikelyPlaceAddresses, List<String> mLikelyPlaceAttributions, LatLng mLikelyPlaceLatLngs, Bitmap bitmap,
                      String photoRef) {
        this.id = id;
        this.mLikelyPlaceNames = mLikelyPlaceNames;
        this.mLikelyPlaceAddresses = mLikelyPlaceAddresses;
        this.mLikelyPlaceAttributions = mLikelyPlaceAttributions;
        this.mLikelyPlaceLatLngs = mLikelyPlaceLatLngs;
        this.bitmap = bitmap;
        this.photoRef = photoRef;
    }

    public String getPhotoRef() {
        return photoRef;
    }

    public void setPhotoRef(String photoRef) {
        this.photoRef = photoRef;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmLikelyPlaceNames() {
        return mLikelyPlaceNames;
    }

    public void setmLikelyPlaceNames(String mLikelyPlaceNames) {
        this.mLikelyPlaceNames = mLikelyPlaceNames;
    }

    public String getmLikelyPlaceAddresses() {
        return mLikelyPlaceAddresses;
    }

    public void setmLikelyPlaceAddresses(String mLikelyPlaceAddresses) {
        this.mLikelyPlaceAddresses = mLikelyPlaceAddresses;
    }

    public List<String> getmLikelyPlaceAttributions() {
        return mLikelyPlaceAttributions;
    }

    public void setmLikelyPlaceAttributions(List<String> mLikelyPlaceAttributions) {
        this.mLikelyPlaceAttributions = mLikelyPlaceAttributions;
    }

    public LatLng getmLikelyPlaceLatLngs() {
        return mLikelyPlaceLatLngs;
    }

    public void setmLikelyPlaceLatLngs(LatLng mLikelyPlaceLatLngs) {
        this.mLikelyPlaceLatLngs = mLikelyPlaceLatLngs;
    }

    @Override
    public String toString() {
        return "PlacesList{" +
                "id=" + id +
                ", mLikelyPlaceNames='" + mLikelyPlaceNames + '\'' +
                ", mLikelyPlaceAddresses='" + mLikelyPlaceAddresses + '\'' +
                ", mLikelyPlaceAttributions='" + mLikelyPlaceAttributions.toString() + '\'' +
                ", mLikelyPlaceLatLngs=" + mLikelyPlaceLatLngs +
                '}';
    }


    private void build_retrofit_and_get_getGeocodeReverseAddress(LatLng origin) {
        /*https://maps.googleapis.com/maps/api/directions/json?
        origin=Toronto&destination=Montreal
                &key=YOUR_API_KEY*/
        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);
        //origin = new LatLng(28.7158727, 77.1910738);

        Call<NearbySearch> call = service.getGeocodeReverseAddress(origin.latitude + "," + origin.longitude);
        Log.i("TAG_SHOW_ORIGIN_getGeocodeReverseAddress", origin.toString());

        call.enqueue(new Callback<NearbySearch>() {
            @Override
            public void onResponse(Response<NearbySearch> response, Retrofit retrofit) {

                try {
                    //Remove previous line from map

                    // This loop will go through all the results and add marker on each location.
                    NearbySearch nearbyPlaces = response.body();
                    Log.i("TAG_SHOW_getGeocodeReverseAddress", nearbyPlaces.toString());
                    for (Result result : nearbyPlaces.getResults()) {
                        /*String distance = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getText();
                        String time = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();
                        Log.i("TAG_SHOW_time", time);
                        //ShowDistanceDuration.setText("TYPE:" + type + "\nDistance:" + distance + ", Duration:" + time);
                        ShowDistanceDuration.append("\n\nTYPE:" + type + "\nDistance:" + distance + ", Duration:" + time);
                        String encodedString = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                        List<LatLng> list = decodePoly(encodedString);
                        mMap.clear();

                        line = mMap.addPolyline(new PolylineOptions()
                                .addAll(list)
                                .width(5)
                                //.width(20)
                                .color(type.equals("driving") ? Color.BLACK : Color.RED)
                                .geodesic(true)
                        );*/

                        //MarkerOptions options1 = new MarkerOptions();
                        //options1.position(new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng()));
                        //options1.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromURL(result.getIcon())));
                        //options1.icon(BitmapDescriptorFactory.fromResource(R.drawable.locator));
                        //mMap.addMarker(options1.title(result.getName()).snippet(result.getVicinity()));
                        //mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                        //mMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
                    }
                } catch (Exception e) {
                    Log.d("onResponse_nearbyPlaces", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("onFailurenearbyPlaces", t.toString());
            }
        });

    }


}