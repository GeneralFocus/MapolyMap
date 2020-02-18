package com.gaspercloud.gotozeal.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gaspercloud.gotozeal.POJO.PlacesList;
import com.gaspercloud.gotozeal.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

public class Nearest_Location_Adapter extends RecyclerView.Adapter<Nearest_Location_Adapter.MyViewHolder> {

    private List<PlacesList> placeList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNew, textViewProductName, textViewProductPrice;
        public ImageView imageViewProductThumb;

        public MyViewHolder(View view) {
            super(view);
            textViewNew = view.findViewById(R.id.textViewNew);
            textViewProductName = view.findViewById(R.id.textViewProductName);
            textViewProductPrice = view.findViewById(R.id.textViewProductPrice);
            imageViewProductThumb = view.findViewById(R.id.imageViewProductThumb);
        }
    }

    Context context;
    PlacesClient mPlacesClient;

    public Nearest_Location_Adapter(List<PlacesList> placeList, Context context, PlacesClient mPlacesClient) {
        this.placeList = placeList;
        this.context = context;
        this.mPlacesClient = mPlacesClient;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.data_grid_view_products_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        PlacesList movie = placeList.get(position);
        /*for (Result result : nearbyPlaces.getResults()) {
        //holder.textViewNew.setText(result.getName());
        holder.textViewProductName.setText(result.getName());
        holder.textViewProductPrice.setVisibility(View.GONE);
        holder.imageViewProductThumb.setImageBitmap(movie.getBitmap());
        }*/
        holder.textViewNew.setText(movie.getmLikelyPlaceNames());
        holder.textViewProductName.setText(movie.getmLikelyPlaceAddresses());
        holder.textViewProductPrice.setVisibility(View.GONE);
        //holder.imageViewProductThumb.setImageBitmap(movie.getBitmap());
        /*String url = "https://maps.googleapis.com/maps/api/place/photo" +
                "?maxwidth=400" +
                "&photoreference=" + movie.getPhotoRef() +
                "&key=AIzaSyDKVGF_OnLWj9lOPppNTH29BANogajV39Q";*/
        //holder.imageViewProductThumb.setImageResource(R.drawable.background_display);


        String placeId = movie.getPhotoRef();
// Specify fields. Requests for photos must always have the PHOTO_METADATAS field.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.PHOTO_METADATAS);

// Get a Place object (this example uses fetchPlace(), but you can also use findCurrentPlace())
        FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, fields);

        mPlacesClient.fetchPlace(placeRequest).addOnSuccessListener(
                new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {

                        Place place = fetchPlaceResponse.getPlace();

                        // Get the photo metadata.
                        PhotoMetadata photoMetadata = null;
                        try {
                            photoMetadata = place.getPhotoMetadatas().get(0);
                        } catch (Exception e) {
                            Log.e("PHOTO_STATUS", "photoMetadata Exception: " + e);
                        }
                        if (photoMetadata != null) {
                            Log.i("PHOTO_STATUS", "Photo Good OK");
                            // Get the attribution text.
                            String attributions = photoMetadata.getAttributions();

                            // Create a FetchPhotoRequest.
                            FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                                    .setMaxWidth(200) // Optional.
                                    .setMaxHeight(150) // Optional.
                                    .build();
                            mPlacesClient.fetchPhoto(photoRequest).addOnSuccessListener(new OnSuccessListener<FetchPhotoResponse>() {
                                @Override
                                public void onSuccess(FetchPhotoResponse fetchPhotoResponse) {
                                    Glide.with(context)
                                            //.load(url)
                                            .load(fetchPhotoResponse.getBitmap())
                                            .into(holder.imageViewProductThumb);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    if (exception instanceof ApiException) {
                                        ApiException apiException = (ApiException) exception;
                                        int statusCode = apiException.getStatusCode();
                                        // Handle error with given status code.
                                        Log.e("PHOTO_STATUS", "Place not found: " + exception.getMessage());
                                    }
                                }
                            });
                        } else {
                            //bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gotozeal_logo);
                            Glide.with(context)
                                    //.load(url)
                                    .load(R.drawable.gotozeal_logo)
                                    .into(holder.imageViewProductThumb);
                            Log.i("PHOTO_STATUS", "Photo Empty");
                        }
                    }
                });

        Log.i("TAG_NAME:", movie.getmLikelyPlaceNames());
    }

    @Override
    public int getItemCount() {
        return placeList != null ? placeList.size() : 0;
    }
}