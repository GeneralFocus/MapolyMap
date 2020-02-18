package com.gaspercloud.gotozeal.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gaspercloud.gotozeal.R;
import com.gaspercloud.gotozeal.SessionManagement;
import com.gaspercloud.gotozeal.model.Product;
import com.gaspercloud.gotozeal.ui.actionProductActivity.ReviewProducts;
import com.gaspercloud.gotozeal.ui.actionProductActivity.SingleProductActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

import static com.gaspercloud.gotozeal.Constants.get30daysDateInterval;
import static com.gaspercloud.gotozeal.FashionAdd_gotozeal.decodeBase64;

public class List_Product_Data_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    boolean showValues;
    LatLng origin;
    private Context mContext;
    private List<Product> productList, productListFiltered;
    //private HomeViewModel homeViewModel;
    private SessionManagement sessionManagement;

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_products_layout, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        return productListFiltered == null ? 0 : productListFiltered.size();
    }


    /*public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, description, price, old_price, rating_count, new_signal;
        private ImageView thumbnail;
        RatingBar ratingBar_list;
        //private Button buy_products_services;
        private RelativeLayout view_foreground;

        public MyViewHolder(View view) {
            super(view);
            new_signal = view.findViewById(R.id.new_signal);
            name = view.findViewById(R.id.name);
            price = view.findViewById(R.id.price);
            old_price = view.findViewById(R.id.old_price);
            description = view.findViewById(R.id.description);
            rating_count = view.findViewById(R.id.rating_count);
            thumbnail = view.findViewById(R.id.thumbnail);
            ratingBar_list = view.findViewById(R.id.ratingBar_list);
            //buy_products_services = view.findViewById(R.id.buy_products_services);
            view_foreground = view.findViewById(R.id.view_foreground);
        }
    }*/


    public List_Product_Data_Adapter(Context mContext, List<Product> productList, Activity fragment, SessionManagement sessionManagement, boolean showValues, LatLng origin) {
        this.mContext = mContext;
        this.productList = productList;
        this.productListFiltered = productList;
        db = FirebaseFirestore.getInstance();
        //this.homeViewModel =
        //      ViewModelProviders.of(fragment).get(HomeViewModel.class);
        this.sessionManagement = sessionManagement;
        this.showValues = showValues;
        this.origin = origin;

        if (showValues) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
            getLastLocation();
        }
    }


    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return productList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView name, description, price, old_price, rating_count, new_signal, author_details;
        private ImageView thumbnail;
        RatingBar ratingBar_list;
        //private Button buy_products_services;
        private RelativeLayout view_foreground;

        public ItemViewHolder(View view) {
            super(view);
            author_details = view.findViewById(R.id.author_details);
            new_signal = view.findViewById(R.id.new_signal);
            name = view.findViewById(R.id.name);
            price = view.findViewById(R.id.price);
            old_price = view.findViewById(R.id.old_price);
            description = view.findViewById(R.id.description);
            rating_count = view.findViewById(R.id.rating_count);
            thumbnail = view.findViewById(R.id.thumbnail);
            ratingBar_list = view.findViewById(R.id.ratingBar_list);
            //buy_products_services = view.findViewById(R.id.buy_products_services);
            view_foreground = view.findViewById(R.id.view_foreground);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder holder, int position) {

        //final Product productView = productList.get(position);
        final Product productView = productListFiltered.get(position);
        //Log.i("TAG_productView", productView.toString());
        Date newDate = get30daysDateInterval();
        float regularprice = 0;
        float saleprice = 0;
        try {
            //regularprice = Float.parseFloat(productView.getRegular_price());
            //saleprice = Float.parseFloat(productView.getSale_price());
        } catch (Exception e) {
            Timber.e(e);
        }
        /*if (saleprice > 0) {
            //first check if there is discount on ground
            holder.new_signal.setText(mContext.getString(R.string.percent_symbol, calculatePercentage(saleprice, regularprice) + "% off"));
        } else if (newDate.before(getDateFromString(productView.getdate_created()))) {
            //check if  new date is before product creation date, product becomes old after 30 days
            holder.new_signal.setText(mContext.getString(R.string.new_product));
        } else {*/
        holder.author_details.setVisibility(View.GONE);
        holder.new_signal.setVisibility(View.GONE);
        //}
        Spanned name = Html.fromHtml(Html.fromHtml(productView.getName()).toString());
        Spanned desc = Html.fromHtml(Html.fromHtml(productView.getDescription()).toString());
        String price = productView.getLatitude();
        //String[] split_price = price.toString().split(" ");
        holder.name.setText(name);
        holder.description.setText(desc);
        /*if (split_price.length > 1 && !split_price[0].isEmpty()) {
            holder.price.setText(split_price[1]);
            holder.old_price.setText(split_price[0]);
            holder.old_price.setPaintFlags(holder.old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {*/
        holder.price.setText(price);
        holder.price.setVisibility(View.GONE);
        holder.old_price.setVisibility(View.GONE);
        //}

        /*holder.ratingBar_list.setRating(Float.parseFloat(productView.getAverage_rating()));
        int ratingCount = productView.getRating_count();
        if (ratingCount <= 0) {
            holder.rating_count.setText(mContext.getString(R.string.no_reviews));
        } else {
            String ratingCountplural = ratingCount > 1 ? mContext.getString(R.string.reviews_message) : mContext.getString(R.string.review_message);
            holder.rating_count.setText(mContext.getString(R.string.rating_general, ratingCount, ratingCountplural));
        }*/
        holder.ratingBar_list.setVisibility(View.GONE);
        holder.rating_count.setVisibility(View.GONE);
        // loading productView cover using Glide library
        /*List<ImageProperties> imageProperties = productView.getImages();
        holder.thumbnail.setScaleType(MATRIX);
        if (imageProperties != null) {*/
        Bitmap myBitmapAgain = decodeBase64(productView.getImages());
        Glide.with(mContext).load(myBitmapAgain).placeholder(R.drawable.ic_menu_gallery).error(R.drawable.ic_photo_library_red_24dp).into(holder.thumbnail);
        /*} else {
            Glide.with(mContext).load(R.drawable.ic_menu_gallery).placeholder(R.drawable.ic_menu_gallery).error(R.drawable.ic_photo_library_red_24dp).into(holder.thumbnail);
        }*/

        /*holder.buy_products_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "You just clicked a buy button.", Toast.LENGTH_SHORT).show();
            }
        });*/
        if (showValues) {
            holder.view_foreground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    //appBarLayout.setExpanded(false);
                    //homeViewModel.getParentID().setValue(productView.getId());
                    //nextActivityProduct(mContext, SingleProductActivity.class, "product", productView);
                    if (origin == null) {
                        Toast.makeText(mContext, "Sorry, the app is yet to confirm your location, Try again.", Toast.LENGTH_SHORT).show();
                        getLastLocation();
                    } else {
                        Intent intent = new Intent(view.getContext(), SingleProductActivity.class);
                        intent.putExtra("product", productView.getId());
                        intent.putExtra("lat", origin.latitude);
                        intent.putExtra("lon", origin.longitude);
                        view.getContext().startActivity(intent);
                    }
                }
            });
       /*   //to Automatically add product to purchase cart use longPress listener
        holder.view_foreground.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //homeViewModel.getParentID().setValue(productView.getId());
                CartModel cartModel = new CartModel(1, productView);
                db.collection(mContext.getString(R.string.customers_table)).document(sessionManagement.getUsername())
                        .collection(mContext.getString(R.string.customers_cart)).document(String.valueOf(cartModel.getProduct().getId()))
                        .set(cartModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toasty.success(mContext, mContext.getString(R.string.customers_cart_added, mContext.getString(R.string.customers_cart)), Toast.LENGTH_SHORT, true).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Timber.e(e);
                                Toasty.error(mContext, mContext.getString(R.string.network_error_message), Toast.LENGTH_SHORT, true).show();
                            }
                        });
                return true;
            }
        });*/
        } else {
            //Fashion Stylist place
            holder.view_foreground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    //appBarLayout.setExpanded(false);
                    //homeViewModel.getParentID().setValue(productView.getId());
                    //nextActivityProduct(mContext, ReviewProducts.class, "product", productView);
                    Intent intent = new Intent(view.getContext(), ReviewProducts.class);
                    intent.putExtra("product", productView.getId());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productListFiltered = productList;
                } else {
                    List<Product> filteredList = new ArrayList<>();
                    for (Product row : productList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getDescription().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    productListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productListFiltered = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    origin = new LatLng(location.getLatitude(), location.getLongitude());
                                }
                            }
                        }
                );
            } else {

            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    //public static LatLng origin;
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            origin = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                (Activity) mContext,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;


}