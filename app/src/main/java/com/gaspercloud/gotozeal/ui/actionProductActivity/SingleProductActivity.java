package com.gaspercloud.gotozeal.ui.actionProductActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gaspercloud.gotozeal.GeofenceBroadcastReceiver;
import com.gaspercloud.gotozeal.POJO.Detail.Detail;
import com.gaspercloud.gotozeal.POJO.Example;
import com.gaspercloud.gotozeal.POJO.Nearby.NearbySearch;
import com.gaspercloud.gotozeal.POJO.Nearby.Result;
import com.gaspercloud.gotozeal.POJO.PlacesList;
import com.gaspercloud.gotozeal.R;
import com.gaspercloud.gotozeal.RetrofitMaps;
import com.gaspercloud.gotozeal.Signup_gotozeal;
import com.gaspercloud.gotozeal.adapter.Nearest_Location_Adapter;
import com.gaspercloud.gotozeal.entitiesDB.orderModelDatabase;
import com.gaspercloud.gotozeal.model.OrderModel;
import com.gaspercloud.gotozeal.network.CheckInternetConnection;
import com.gaspercloud.gotozeal.ui.design.MyDividerItemDecoration;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static com.gaspercloud.gotozeal.Constants.nextActivity;

public class SingleProductActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener {

    private GoogleMap mMap;
    LatLng origin, originNext;
    LatLng dest;
    ArrayList<LatLng> MarkerPoints;
    //TextView ShowDistanceDuration, ShowDistanceDurationDrive;
    TextView title_grid, show_time, show_distance, title_grid_walk, show_time_walk, show_distance_walk, start_now;
    Polyline line;
    String originName, originAddress, originID, destName, destAddress, destID;

    //private SliderLayout productimage_slider;


    private LocationManager locationManager;
    private String provider;

    Geocoder geocoder;

    private Context context;


    static double latDou, lonDou;


    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    ListView lstPlaces;
    private PlacesClient mPlacesClient;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // A default location (MAPOLY Administration Block, Ojere) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(77.0977895, 3.3220847);
    private static final int DEFAULT_ZOOM = 16;
    private static final int MAX_ZOOM = 20;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted, permissionRationaleAlreadyShown;

    // Used for selecting the current place.
    //private static final int M_MAX_ENTRIES = 5;
    private static final int M_MAX_ENTRIES = 20;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private String[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;

    List<PlacesList> placesLists;
    List<NearbySearch> nearbySearches;
    private RecyclerView recyclerView;
    private Nearest_Location_Adapter nearest_location_adapter;

    private FloatingActionButton floatingActionNearMe;
    List<Address> addresses;
    String addressDefault = "", knownName = "";
    AutocompleteSupportFragment autocompleteFragment;
    private Marker geoFenceMarker;

    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";

    public static Intent makeNotificationIntent(Context applicationContext, String msg) {
        Intent intent = new Intent(applicationContext, SingleProductActivity.class);
        intent.putExtra(NOTIFICATION_MSG, msg);
        return intent;
    }

    LatLngBounds BOUNDS = new LatLngBounds(new LatLng(7.0946458, 3.3210053),
            new LatLng(7.1050475, 3.3330537));

    void defaultAddress(LatLng loca) {
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(loca.latitude, loca.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null) {
            addressDefault = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
        } else {
            Log.e("addressesNULL", "Address is null");
        }
    }

    private GeofencingClient geofencingClient;
    private static boolean showNearbyPlaces;

    LinearLayout allview, directionView, cardview_grid_category_walk, cardview_grid_category_drive;


    public static boolean showNotifications;


    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        showNearbyPlaces = false;
        setContentView(R.layout.activity_single_product);

        allview = findViewById(R.id.allview);
        allview.setVisibility(View.GONE);

        dl = findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.feedback:
                        AddReview(context);
                        return true;
                    case R.id.settings:

// get prompts.xml view
                        LayoutInflater li = LayoutInflater.from(context);
                        View promptsView = li.inflate(R.layout.prompts, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                context);

                        // set prompts.xml to alertdialog builder
                        alertDialogBuilder.setView(promptsView);

                        final EditText userInput = promptsView
                                .findViewById(R.id.editTextDialogUserInput);

                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // get user input and set it to result
                                                // edit text
                                                //result.setText(userInput.getText());
                                                if (userInput.getText().toString().equalsIgnoreCase("123456")) {
                                                    Intent intent = new Intent(context, ReviewProducts.class);
                                                    intent.putExtra("product", 0);
                                                    startActivity(intent);
                                                    dialog.cancel();
                                                } else {
                                                    Toast.makeText(context, "Password incorrect, try again.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();

                        return true;
                    default:
                        return true;
                }
            }
        });


        showNotifications = true;
        geofencingClient = LocationServices.getGeofencingClient(this);

        //permissionCALL2();
        //permissionCALL();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/


        //ShowDistanceDuration = findViewById(R.id.show_distance_time);
        title_grid = findViewById(R.id.title_grid);
        show_time = findViewById(R.id.show_time);
        show_distance = findViewById(R.id.show_distance);
        title_grid_walk = findViewById(R.id.title_grid_walk);
        show_time_walk = findViewById(R.id.show_time_walk);
        show_distance_walk = findViewById(R.id.show_distance_walk);

        final CheckBox checkBox = findViewById(R.id.checkBox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotifications = checkBox.isChecked();
            }
        });
/*
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }*/

        this.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//Choosing the best criteria depending on what is available.
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
//provider = LocationManager.GPS_PROVIDER; // We want to use the GPS

// Initialize the location fields
        //Location location = locationManager.getLastKnownLocation(provider);


        // Initializing
        MarkerPoints = new ArrayList<>();

        //show error dialog if Google Play Services not available
        if (!isGooglePlayServicesAvailable()) {
            Log.d("onCreate", "Google Play Services not available. Ending Test case.");
            Toast.makeText(context, "Google Play Services must be available on device. Download Now.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Log.d("onCreate", "Google Play Services available. Continuing.");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // Set up the views
        lstPlaces = findViewById(R.id.listPlaces);
        lstPlaces.setVisibility(View.GONE);

        directionView = findViewById(R.id.directionView);
        directionView.setVisibility(View.GONE);

        cardview_grid_category_drive = findViewById(R.id.cardview_grid_category_drive);
        cardview_grid_category_drive.setVisibility(View.GONE);

        cardview_grid_category_walk = findViewById(R.id.cardview_grid_category_walk);
        cardview_grid_category_walk.setVisibility(View.GONE);

        // Initialize the Places client
        String apiKey = getString(R.string.google_maps_key);
        Places.initialize(getApplicationContext(), apiKey);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mPlacesClient = Places.createClient(this);
        getLastLocation();


        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        EditText place_autocomplete_search_input = (EditText) ((LinearLayout) autocompleteFragment.getView()).getChildAt(1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            place_autocomplete_search_input.setTextColor(getResources().getColor(R.color.primaryTextColor, context.getTheme()));
        } else {
            place_autocomplete_search_input.setTextColor(getResources().getColor(R.color.primaryTextColor));
        }
        ImageView locationSearchIcon = (ImageView) ((LinearLayout) autocompleteFragment.getView()).getChildAt(0);
        locationSearchIcon.setImageResource(R.drawable.ic_dehaze_white_24dp);
        locationSearchIcon.setFocusable(true);
        locationSearchIcon.setClickable(true);
        locationSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Hey", Toast.LENGTH_SHORT).show();
                //pickCurrentPlace(true);
                //new pickUpLocation(true).execute();
                dl.openDrawer(Gravity.LEFT); //Edit Gravity.START need API 14
            }
        });
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        autocompleteFragment.setHint("Enter Location Address");

        //Set Region restriction
        autocompleteFragment.setLocationRestriction(RectangularBounds.newInstance(
                BOUNDS));

        // Set up a PlaceSelectionListener to handle the response.

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                recyclerView.setVisibility(View.GONE);
                Log.i("TAG8", "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng());
                origin = place.getLatLng();
                originName = place.getName();
                originAddress = place.getAddress();
                originID = place.getId();
                //mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title("Moving Location:\n" + originNext.toString()));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("TAG9", "An error occurred: " + status);
                //Toast.makeText(context, "No Location.", Toast.LENGTH_SHORT).show();
            }
        });


        // Initialize the AutocompleteSupportFragment.
        final AutocompleteSupportFragment autocomplete_fragment_next = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_next);


        EditText place_autocomplete_search_input_next = (EditText) ((LinearLayout) autocomplete_fragment_next.getView()).getChildAt(1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            place_autocomplete_search_input_next.setTextColor(getResources().getColor(R.color.primaryTextColor, context.getTheme()));
        } else {
            place_autocomplete_search_input_next.setTextColor(getResources().getColor(R.color.primaryTextColor));
        }
        ImageView destinationSearchIcon = (ImageView) ((LinearLayout) autocomplete_fragment_next.getView()).getChildAt(0);
        destinationSearchIcon.setImageResource(R.drawable.ic_my_location_white_24dp);
        //destinationSearchIcon.setImageResource(R.drawable.ic_place_white_24dp);
        destinationSearchIcon.setFocusable(true);
        destinationSearchIcon.setClickable(true);
        destinationSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Hey", Toast.LENGTH_SHORT).show();
                //pickCurrentPlace(true);
                if (mMap != null) {
                    mMap.clear();
                }
                new pickUpLocation(true).execute();
            }
        });

        // Specify the types of place data to return.
        autocomplete_fragment_next.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));

        autocomplete_fragment_next.setHint("Enter Destination Address");
        //Set Region restriction
        autocomplete_fragment_next.setLocationRestriction(RectangularBounds.newInstance(
                BOUNDS));

        // Set up a PlaceSelectionListener to handle the response.
        autocomplete_fragment_next.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                removeGeofence();//remove GeoFence to add a new one
                Log.i("TAG8", "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng());
                recyclerView.setVisibility(View.GONE);
                dest = place.getLatLng();
                destName = place.getName();
                destAddress = place.getAddress();
                destID = place.getId();
                //if (origin != null) {
                build_retrofit_and_get_response("driving", origin, dest, false);
                build_retrofit_and_get_response("walking", origin, dest, false);
                //build_retrofit_and_get_nearbyplaces(origin);
                //getCurrentPlaceLikelihoods(false);
                //}
                //mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title("Moving Location:\n" + originNext.toString()));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("TAG9", "An error occurred: " + status);
                //Toast.makeText(context, "No Location.", Toast.LENGTH_SHORT).show();
            }
        });


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setVisibility(View.GONE);

        nearbySearches = new ArrayList<>();
        placesLists = new ArrayList<>();

        placesLists.add(new PlacesList(1, "Please wait", "Loading nearby Locations", null, null, null, "CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU"));
        nearest_location_adapter = new Nearest_Location_Adapter(placesLists, context, mPlacesClient);
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        //LinearLayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
// horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));


        recyclerView.setAdapter(nearest_location_adapter);

        floatingActionNearMe = findViewById(R.id.floatingActionNearMe);
        floatingActionNearMe.setVisibility(View.GONE);
        floatingActionNearMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "You reach here");
                directionView.setVisibility(View.GONE);
                cardview_grid_category_drive.setVisibility(View.GONE);
                cardview_grid_category_walk.setVisibility(View.GONE);

                if (placesLists == null || placesLists.size() <= 0) {
                    //pickCurrentPlace(false);
                    new pickUpLocation(false).execute();
                    //showNearbyPlaces = true;
                }
                Log.i(TAG, "placesLists.size: " + placesLists.size());
                //nearest_location_adapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);
                floatingActionNearMe.setVisibility(View.GONE);

            }
        });


        start_now = findViewById(R.id.start_now);
        start_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionNearMe.setVisibility(View.VISIBLE);
                allview.setVisibility(View.VISIBLE);
                start_now.setVisibility(View.GONE);
            }
        });

        //registerReceiver(broadcastReceiver, new IntentFilter("NOTIFICATION_SENT"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(broadcastReceiver);
    }

    /*BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // internet lost alert dialog method call from here...
            removeGeofence();//remove GeoFence to add a new one
        }
    };*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private Geofence createGeofence(String key, LatLng dest, float GEOFENCE_RADIUS_IN_METERS,
                                    long GEOFENCE_EXPIRATION_IN_MILLISECONDS) {
        Log.i(TAG, "createGeofence()");
        return new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(key)

                .setCircularRegion(
                        dest.latitude,
                        dest.longitude,
                        GEOFENCE_RADIUS_IN_METERS
                )
                .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }

    // Draw Geofence circle on GoogleMap
    private Circle geoFenceLimits;

    private void removeGeofenceDraw() {
        Log.d(TAG, "removeGeofenceDraw()");
        if (geoFenceMarker != null)
            geoFenceMarker.remove();
        if (geoFenceLimits != null)
            geoFenceLimits.remove();
    }

    private void drawGeofence(float GEOFENCE_RADIUS, Marker geoFenceMarker) {
        Log.d(TAG, "drawGeofence()");

        if (geoFenceLimits != null)
            geoFenceLimits.remove();
        CircleOptions circleOptions = new CircleOptions()
                .center(geoFenceMarker.getPosition())
                .strokeColor(Color.argb(50, 70, 70, 70))
                .fillColor(Color.argb(100, 150, 150, 150))
                .radius(GEOFENCE_RADIUS);
        geoFenceLimits = mMap.addCircle(circleOptions);
    }

    private GeofencingRequest getGeofencingRequest(String key, LatLng dest, float GEOFENCE_RADIUS_IN_METERS,
                                                   long GEOFENCE_EXPIRATION_IN_MILLISECONDS) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        //builder.addGeofences(geofenceList);
        builder.addGeofence(createGeofence(key, dest, GEOFENCE_RADIUS_IN_METERS, GEOFENCE_EXPIRATION_IN_MILLISECONDS));
        return builder.build();
    }

    private PendingIntent geofencePendingIntent;

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    private static final String TAG = SingleProductActivity.class.getSimpleName();

    private void removeGeofence() {
        geofencingClient.removeGeofences(getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Geofences removed
                        // ...
                        Log.i(TAG, "Geofence removed");
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to remove geofences
                        // ...
                        Log.e(TAG, "Geofence removed error");
                        e.printStackTrace();
                    }
                });

        removeGeofenceDraw();
    }

    private void addGeofence(String key, LatLng dest, float GEOFENCE_RADIUS_IN_METERS,
                             long GEOFENCE_EXPIRATION_IN_MILLISECONDS) {
        //Toast.makeText(this, "here too", Toast.LENGTH_SHORT).show();
        geofencingClient.addGeofences(getGeofencingRequest(key, dest, GEOFENCE_RADIUS_IN_METERS, GEOFENCE_EXPIRATION_IN_MILLISECONDS), getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Geofences added
                        // ...
                        Log.i(TAG, "Geofence Added");
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add geofences
                        // ...
                        Log.e(TAG, "Geofence Added");
                        e.printStackTrace();
                    }
                });
    }

    private static final int PERMISSIONS_REQUEST_CODE = 11;
    private static final int PERMISSIONS_REQUEST_CODE_12 = 12;
    private static final String GEOFENCE_REQ_ID = "MAPOLY Destination Alert";


   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //makeCall();
            }
        }
    }*/

    private void permissionCALL2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                //Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
                if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                    //Toast.makeText(context, "3", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(context)
                            .setCancelable(false)
                            .setTitle("Call Permission")
                            .setMessage("Hi there! We can't call anyone without the call permission, could you please grant it?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CODE);
                                }
                            })
                            /*.setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(context, ":(", Toast.LENGTH_SHORT).show();
                                }
                            })*/
                            .show();
                } else {
                    //Toast.makeText(context, "4", Toast.LENGTH_SHORT).show();
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CODE);
                }
            } else {
                //Toast.makeText(context, "5", Toast.LENGTH_SHORT).show();
                // makeCall();
            }
        } else {
            //Toast.makeText(context, "6", Toast.LENGTH_SHORT).show();
            //makeCall();
        }
    }

    private void permissionCALL() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Toast.makeText(context, "3", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(context)
                            .setCancelable(false)
                            .setTitle("Gallery View")
                            .setMessage("Hi there! Grant access to show locations?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
                                }
                            })
                            /*.setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(context, ":(", Toast.LENGTH_SHORT).show();
                                }
                            })*/
                            .show();
                } else {
                    //Toast.makeText(context, "4", Toast.LENGTH_SHORT).show();
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
                }
            } else {
                //Toast.makeText(context, "5", Toast.LENGTH_SHORT).show();
                // makeCall();
            }
        } else {
            //Toast.makeText(context, "6", Toast.LENGTH_SHORT).show();
            //makeCall();
        }
    }


    private File saveBitmap(@NonNull final Context context, @NonNull final Bitmap bitmap,
                            @NonNull final Bitmap.CompressFormat format, @NonNull final String mimeType,
                            @NonNull final String displayName) throws IOException {
        final String relativeLocation = Environment.DIRECTORY_PICTURES;

        final ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation);

        final ContentResolver resolver = context.getContentResolver();

        OutputStream stream = null;
        Uri uri = null;

        try {
            final Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            uri = resolver.insert(contentUri, contentValues);

            if (uri == null) {
                throw new IOException("Failed to create new MediaStore record.");
            }

            stream = resolver.openOutputStream(uri);

            if (stream == null) {
                throw new IOException("Failed to get output stream.");
            }

            if (bitmap.compress(format, 95, stream) == false) {
                throw new IOException("Failed to save bitmap.");
            }
        } catch (IOException e) {
            if (uri != null) {
                resolver.delete(uri, null, null);
            }

            throw e;
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        Log.i("TAG", uri.getPath());
//        return new File(uri.getPath());
        return new File(getPathFromURI(uri));
    }

    public String getPathFromURI(Uri ContentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media._ID};
        Cursor cursor = getContentResolver()
                .query(ContentUri, proj, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            res = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            cursor.close();
        }


        return res;
    }

    @Override
    protected void onStop() {
        //productimage_slider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void goToCart(View view) {

       /* if ( customheader.getText().toString().length() == 0 ||  custommessage.getText().toString().length() ==0 ){

            Snackbar.make(view, "Header or Message Empty", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else {*/
        //addToCart(view);
        //nextActivity(context, Cart.class, "", null);
        //  }

        //AddReview(null, context);


    }


    @Override
    public void onLocationChanged(Location location) {
            /*originNext = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(originNext));
            //mMap.addMarker(new MarkerOptions().position(originNext).title("Moving Location:\n" + originNext.toString()));
            Log.i("TAG_ONCHANGE", location.toString());*/
        //pickCurrentPlace();
        mMap.clear();
        mLastKnownLocation = location;
        origin = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
        //defaultAddress(origin);
        Log.i("addressDefault_TAG", addressDefault);
        //Toasty.info(context, "You are now at:\n " + addressDefault, Toasty.LENGTH_SHORT, true).show();
        //pickCurrentPlace(false);
        new pickUpLocation(false).execute();
        if (origin != null && dest != null) {
            //Toast.makeText(context, "I am good", Toast.LENGTH_SHORT).show();
            build_retrofit_and_get_response("driving", origin, dest, false);
            build_retrofit_and_get_response("walking", origin, dest, false);
            /*if (mLastKnownLocation != null) {
                Toast.makeText(context, "I am good.", Toast.LENGTH_SHORT).show();
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude())));
            }*/
        }

//autocompleteFragment.setText(addressDefault);
    }

    @Override
    protected void onResume() {
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
        super.onResume();
        Log.v("TAG_RESUME", "Resuming");

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        if (locationManager != null) {
            //locationManager.requestLocationUpdates(provider, 400, 1, this);
            //locationManager.requestLocationUpdates(provider, 1000, 1, this);//Update Map every 1 seconds
            locationManager.requestLocationUpdates(provider, 10000, 1, this);//Update Map every 1 seconds
        }
        //productimage_slider.startAutoCycle();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    class AddOrder extends AsyncTask<Void, Void, Void> {
        private OrderModel u;
        private WeakReference<Context> c;

        public AddOrder(Context c, OrderModel u) {
            this.c = new WeakReference<>(c);
            this.u = u;
            Log.i("Login_GotoZeal", u.toString());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            orderModelDatabase ud = orderModelDatabase.getAppDatabase(c.get());
            Long val = ud.orderModelDAO().insert(u);
            Log.i("val", val + "");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Toasty.info(context, "Thank you for using MAPOLY Tour, Kindly send us a feedback.", Toast.LENGTH_SHORT, true);
            Toasty.info(context, "Thank you for using MAPOLY Tour.", Toast.LENGTH_SHORT, true);
            //((Activity) c.get()).finish();
        }
    }


    static String messageSave = "";

    //private void AddReview(final OrderModel orderModel, final Context context) {
    public void AddReview(final Context context) {
        // Use the Builder class for convenient dialog construction
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.alert_dialog_popup_layout, null);
        //final orderModelDatabase modelDatabase = orderModelDatabase.getAppDatabase(context);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText message = promptsView
                .findViewById(R.id.message);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                messageSave = message.getText().toString();


                                OrderModel orderModel = new OrderModel();
                                orderModel.setCustomer_id(1);
                                //orderModel.setCustomer_id(Signup_gotozeal.customers1.getId());
                                orderModel.setStatus("false");
                                //orderModel.setVersion(Signup_gotozeal.customers1.getUsername());
                                orderModel.setSet_paid(false);
                                //orderModel.setNumber(productModel.getParent_id());
                                //orderModel.setParent_id(productModel.getId());
                                orderModel.setVersion(originName);//Origin Name
                                orderModel.setOrder_key(destName);//Destination Name
                                orderModel.setCustomer_note(messageSave);
                                //orderModel.setTotal("" + (Integer.parseInt(quantityProductPage.getText().toString()) * Float.parseFloat(productModel.getLatitude())));

                                new AddOrder(context, orderModel).execute();

                                dialog.dismiss();
                                //finish();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Toasty.info(context, "Thank you for using MAPOLY Tour.", Toast.LENGTH_SHORT, true);
                                //finish();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    /*  public void similarProduct(View view) {
          Toasty.info(context, "Still Looking at Similar Product ways or something else here", Toasty.LENGTH_SHORT, true).show();
          //finish();
      }
  */
    public void shareProduct(View view) {
        Toasty.info(context, "Share products to Social Media Channels", Toasty.LENGTH_SHORT, true).show();
    /*
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Found amazing " + productname.getText().toString() + "on Magic Prints App";
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
        */
    }

    public void Notifications(View view) {
        Toasty.info(context, "Set Notifications here", Toasty.LENGTH_SHORT, true).show();
        /*startActivity(new Intent(IndividualProduct.this, NotificationActivity.class));
        finish();*/
    }

    private void makeCall(String tel) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + tel));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        context.startActivity(intent);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(false);
        // Constrain the camera target to the Adelaide bounds.
        mMap.setLatLngBoundsForCameraTarget(BOUNDS);
        getLastLocation();

        // Add a marker in Sydney and move the camera
        //LatLng Model_Town = new LatLng(28.7158727, 77.1910738);
//        Log.i("LATLONG", dest.toString());
        /*LatLng Model_Town = origin;//new LatLng(latDou, lonDou);
        mMap.addMarker(new MarkerOptions().position(Model_Town).title("Current Location:\n" + origin.toString()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Model_Town));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        mMap.clear();
        MarkerPoints.clear();
        MarkerPoints = new ArrayList<>();
        ShowDistanceDuration.setText("");
        // Adding new item to the ArrayList
        MarkerPoints.add(origin);
        MarkerPoints.add(dest);*/
/*
        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();
        // Setting the position of the marker
        options.position(origin);
        //if (MarkerPoints.size() == 1) {
        //options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.locator));
        //} else if (MarkerPoints.size() == 2) {
        //options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        //}
        // Add new marker to the Google Map Android API V2
        mMap.addMarker(options.title("Current Location - " + origin.toString()));

        MarkerOptions options1 = new MarkerOptions();
        options1.position(dest);
        options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(options1.title("Destination Location - " + dest.toString()));

        mMap.addPolyline(new PolylineOptions()
                .addAll(MarkerPoints)
                .width(5)
                .color(Color.RED)
                .geodesic(true));*/
        //build_retrofit_and_get_response("driving");


        //Showing Distance to Cover
        //double distance = distance(origin.latitude, origin.longitude, dest.latitude, dest.longitude, "K");
        /*int speed = 1 / 60;//a constant of HUMAN WALKING if speed is 1 second divided by 60minutes
        float time = (float) (distance / speed);
        int hours = (int) (time / 60); //since both are ints, you get an int
        int minutes = (int) (time % 60);*/
        //ShowDistanceDuration.setText("Distance:" + String.format("%.2f", distance) + " KM");
        //\nDuration:" + String.format("%d", hours) + ":" + String.format("%02d", minutes));


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTag() != null) {
                    Log.i(TAG, "Reach here:" + marker.getTag().toString());
                    showDialog(SingleProductActivity.this, marker.getTitle(), marker.getSnippet(), marker.getTag().toString());
                }
                return false;
            }
        });

        // Setting onclick event listener for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                Log.i(TAG, "You reach here");
                directionView.setVisibility(View.GONE);
                cardview_grid_category_drive.setVisibility(View.GONE);
                cardview_grid_category_walk.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                //floatingActionNearMe.setVisibility(View.VISIBLE);

             /*   if (placesLists == null || placesLists.size() <= 0) {
                    pickCurrentPlace(false);
                    //showNearbyPlaces = true;
                }
                Log.i(TAG, "placesLists.size: " + placesLists.size());
                nearest_location_adapter.notifyDataSetChanged();
                if (showNearbyPlaces) {
                    recyclerView.setVisibility(View.VISIBLE);
                    showNearbyPlaces = false;
                    Log.i(TAG, "showNearbyPlaces Current False: " + showNearbyPlaces);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    showNearbyPlaces = true;
                    Log.i(TAG, "showNearbyPlaces Current True: " + showNearbyPlaces);
                }*/

/*
                // clearing map and generating new marker points if user clicks on map more than two times
                //if (MarkerPoints.size() > 1) {
                    mMap.clear();
                    MarkerPoints.clear();
                    MarkerPoints = new ArrayList<>();
                    ShowDistanceDuration.setText("");
                //}

                // Adding new item to the ArrayList
                MarkerPoints.add(point);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(point);


                if (MarkerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (MarkerPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }


                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if (MarkerPoints.size() >= 2) {
                    origin = MarkerPoints.get(0);
                    dest = MarkerPoints.get(1);
                }*/

            }
        });


// Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        LatLng sydney = mDefaultLocation;
        mMap.addMarker(new MarkerOptions().position(sydney).title("Moshood Abiola Polytechnic"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                sydney, DEFAULT_ZOOM));

        //
        // PASTE THE LINES BELOW THIS COMMENT
        //

        // Enable the zoom controls for the map
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Prompt the user for permission.
        getLocationPermission();


        mMap.clear();
        //pickCurrentPlace(true);
        new pickUpLocation(true).execute();


        // Do other setup activities here too, as described elsewhere in this tutorial.
        /*mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
                TextView title = (infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = (infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());


                TextView description = (infoWindow.findViewById(R.id.description));
                description.setText(marker.getSnippet());
                ImageView imageViewDetail = infoWindow.findViewById(R.id.imageViewDetail);
                imageViewDetail.setImageResource(R.drawable.mapolylogo);
                //if (marker.getTag() != null) {
                  //  Toast.makeText(context, "clc", Toast.LENGTH_SHORT).show();
                //}
                return infoWindow;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker.getTag() != null) {
                    showDialog(SingleProductActivity.this, marker.getTitle(), marker.getSnippet(), marker.getTag().toString());
                }
            }
        });*/
        Button btnDriving = findViewById(R.id.btnDriving);
        btnDriving.setVisibility(View.GONE);
        btnDriving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //build_retrofit_and_get_response("driving");
            }
        });

        Button btnWalk = findViewById(R.id.btnWalk);
        btnWalk.setVisibility(View.GONE);
        btnWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //build_retrofit_and_get_response("walking");

            }
        });

        ImageButton btnDrivingImage = findViewById(R.id.btnDrivingImage);
        btnDrivingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                build_retrofit_and_get_response("driving", origin, dest, true);
            }
        });

        ImageButton btnWalkImage = findViewById(R.id.btnWalkImage);
        btnWalkImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                build_retrofit_and_get_response("walking", origin, dest, true);

            }
        });
        //pickCurrentPlace();
        /*defaultAddress(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()));
        Log.i("addressDefault_TAG", addressDefault);
        autocompleteFragment.setText(addressDefault);*/
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        mLocationPermissionGranted = false;
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        permissionRationaleAlreadyShown = mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
            case PERMISSIONS_REQUEST_CODE_12: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionRationaleAlreadyShown = true;
                    float GEOFENCE_RADIUS_IN_METERS = 100;
                    //float GEOFENCE_RADIUS_IN_METERS = 50;
                    long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 60 * 60 * 1000;

                    addGeofence(GEOFENCE_REQ_ID, dest, GEOFENCE_RADIUS_IN_METERS,
                            GEOFENCE_EXPIRATION_IN_MILLISECONDS);
                }
            }
        }
    }

    private void build_retrofit_and_get_response(final String type, final LatLng origin, final LatLng dest, final boolean showDistanceTime) {
        if (showDistanceTime) {//show DIstance and Time only When Enable
            if (type.equals("driving")) {
                cardview_grid_category_drive.setVisibility(View.VISIBLE);
                cardview_grid_category_walk.setVisibility(View.GONE);
            } else {
                cardview_grid_category_walk.setVisibility(View.VISIBLE);
                cardview_grid_category_drive.setVisibility(View.GONE);
            }
            return;//just show results
        }
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
        Log.i("TAG_SHOW_ORIGIN", origin.toString());
        Log.i("TAG_SHOW_DESTINATION", dest.toString());

        Call<Example> call = service.getDistanceDuration("metric", origin.latitude + "," + origin.longitude, dest.latitude + "," + dest.longitude, type);
        //Call<Example> call = service.getDistanceDuration("metric", origin.toString(), dest.toString(), type);

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Response<Example> response, Retrofit retrofit) {

                try {
                    //Remove previous line from map
                    if (line != null) {
                        line.remove();
                    }
                    // This loop will go through all the results and add marker on each location.
                    Example example = response.body();
                    Log.i("TAG_SHOW", example.toString());
                    for (int i = 0; i < response.body().getRoutes().size(); i++) {
                        String distance = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getText();
                        String time = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();
                        Log.i("TAG_SHOW_time", time);
                        //if (showDistanceTime) {//show DIstance and Time only When Enable
                        if (type.equals("driving")) {
                            //ShowDistanceDurationDrive.append("\n\nTYPE:" + type + "\nDistance:" + distance + ", Duration:" + time);
                            //cardview_grid_category_drive.setVisibility(View.VISIBLE);
                            //cardview_grid_category_walk.setVisibility(View.GONE);
                            title_grid.setText(type.toUpperCase());
                            show_time.setText(time);
                            show_distance.setText(distance);
                        } else {
                            //ShowDistanceDuration.setText("TYPE:" + type + "\nDistance:" + distance + ", Duration:" + time);
                            //cardview_grid_category_walk.setVisibility(View.VISIBLE);
                            //cardview_grid_category_drive.setVisibility(View.GONE);
                            title_grid_walk.setText(type.toUpperCase());
                            show_time_walk.setText(time);
                            show_distance_walk.setText(distance);
                        }
                        //}
                        String encodedString = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                        List<LatLng> list = decodePoly(encodedString);
                        mMap.clear();

                        line = mMap.addPolyline(new PolylineOptions()
                                .addAll(list)
                                .width(5)
                                //.width(20)
                                .color(type.equals("driving") ? Color.BLACK : Color.RED)
                                .geodesic(true)
                        );

                        MarkerOptions options1 = new MarkerOptions();
                        options1.position(origin);
                        options1.icon(BitmapDescriptorFactory.fromResource(R.drawable.locator));
                        Marker marker = mMap.addMarker(options1.title(originName).snippet(originAddress));
                        marker.setTag(originID);

                        //mMap.addMarker(options1.title("Current Location - " + dest.toString()));
                        options1 = new MarkerOptions();
                        options1.position(dest);
                        options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        //geoFenceMarker = mMap.addMarker(options1.title(destName).snippet(destAddress));
                        Marker marker2 = mMap.addMarker(options1.title(destName).snippet(destAddress));
                        marker2.setTag(destID);
                        geoFenceMarker = marker2;
                        //mMap.addMarker(options1.title(destName).snippet(destAddress));
                        //mMap.addMarker(options1.title("Destination Location - " + dest.toString()));

                        //mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));

//                        LatLngBounds bounds = new LatLngBounds(
                        //                              origin, dest);
  /*                      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 0));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
*/

                        CameraUpdate center =
                                CameraUpdateFactory.newLatLng(origin);
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(DEFAULT_ZOOM);

                        mMap.moveCamera(center);
                        mMap.animateCamera(zoom);


                    }

                    directionView.setVisibility(View.VISIBLE);

                    float GEOFENCE_RADIUS_IN_METERS = 100;
                    long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 60 * 60 * 1000;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // Required if your app targets Android 10 or higher.
                        if (ContextCompat.checkSelfPermission(context,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                            if (permissionRationaleAlreadyShown) {
                                ActivityCompat.requestPermissions(SingleProductActivity.this,
                                        new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                                        PERMISSIONS_REQUEST_CODE_12);
                                //Toast.makeText(context, "reach om 3", Toast.LENGTH_SHORT).show();
                            } else {
                                // Show an explanation to the user as to why your app needs the
                                // permission. Display the explanation *asynchronously* -- don't block
                                // this thread waiting for the user's response!
                                //Toast.makeText(context, "reach ok 1", Toast.LENGTH_SHORT).show();

                                addGeofence(GEOFENCE_REQ_ID, dest, GEOFENCE_RADIUS_IN_METERS,
                                        GEOFENCE_EXPIRATION_IN_MILLISECONDS);
                                drawGeofence(GEOFENCE_RADIUS_IN_METERS, geoFenceMarker);
                            }
                        } else {
                            //Toast.makeText(context, "reach", Toast.LENGTH_SHORT).show();
                            // Background location runtime permission already granted.
                            // You can now call geofencingClient.addGeofences().

                            addGeofence(GEOFENCE_REQ_ID, dest, GEOFENCE_RADIUS_IN_METERS,
                                    GEOFENCE_EXPIRATION_IN_MILLISECONDS);
                            drawGeofence(GEOFENCE_RADIUS_IN_METERS, geoFenceMarker);
                        }
                    } else {
                        //Toast.makeText(context, "reach ok 7", Toast.LENGTH_SHORT).show();
                        addGeofence(GEOFENCE_REQ_ID, dest, GEOFENCE_RADIUS_IN_METERS,
                                GEOFENCE_EXPIRATION_IN_MILLISECONDS);
                        drawGeofence(GEOFENCE_RADIUS_IN_METERS, geoFenceMarker);

                    }
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    Log.d("onResponse_getMessage", e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });

    }

    private void build_retrofit_and_get_nearbyplaces(LatLng origin) {
        nearbySearches = new ArrayList<>();
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

        Call<NearbySearch> call = service.getNearestDistance(origin.latitude + "," + origin.longitude);
        Log.i("TAG_nearbyPlaces", origin.toString());

        call.enqueue(new Callback<NearbySearch>() {
            @Override
            public void onResponse(Response<NearbySearch> response, Retrofit retrofit) {

                try {
                    //Remove previous line from map

                    nearbySearches.clear();
                    // This loop will go through all the results and add marker on each location.
                    NearbySearch nearbyPlaces = response.body();
                    nearbySearches.add(nearbyPlaces);
                    Log.i("TAG_SHOW_NEARBY", nearbyPlaces.toString());
                    for (Result result : nearbyPlaces.getResults()) {
                      /*  String distance = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getText();
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
                        );
*/
                        MarkerOptions options1 = new MarkerOptions();
                        options1.position(new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng()));
                        //options1.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromURL(result.getIcon())));
                        options1.icon(BitmapDescriptorFactory.fromResource(R.drawable.locator));
                        Marker marker3 = mMap.addMarker(options1.title(result.getName()).snippet(result.getVicinity()));
                        marker3.setTag(originID);

                        //mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                        //mMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
                    }
                    nearest_location_adapter.notifyDataSetChanged();
                    showNearbyPlaces = true;

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


    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    // Checking if Google Play Services Available or not
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
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
                                    /*price.setText(location.getLatitude() + "");
                                    shortdesc.setText(location.getLongitude() + "");*/
                                    origin = new LatLng(location.getLatitude(), location.getLongitude());
                                    //defaultAddress(origin);
                                    Log.i("CURRENTLOCATIONOLD", origin.toString());
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
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

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
          /*price.setText(mLastLocation.getLatitude() + "");
            shortdesc.setText(mLastLocation.getLongitude() + "");
            */
            origin = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            Log.i("CURRENTLOCATION", origin.toString());
            //defaultAddress(origin);
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::                                                                         :*/
    /*::  This routine calculates the distance between two points (given the     :*/
    /*::  latitude/longitude of those points). It is being used to calculate     :*/
    /*::  the distance between two locations using GeoDataSource (TM) products   :*/
    /*::                                                                         :*/
    /*::  Definitions:                                                           :*/
    /*::    Southern latitudes are negative, eastern longitudes are positive     :*/
    /*::                                                                         :*/
    /*::  Function parameters:                                                   :*/
    /*::    lat1, lon1 = Latitude and Longitude of point 1 (in decimal degrees)  :*/
    /*::    lat2, lon2 = Latitude and Longitude of point 2 (in decimal degrees)  :*/
    /*::    unit = the unit you desire for results                               :*/
    /*::           where: 'M' is statute miles (default)                         :*/
    /*::                  'K' is kilometers                                      :*/
    /*::                  'N' is nautical miles                                  :*/
    /*::  Worldwide cities and other features databases with latitude longitude  :*/
    /*::  are available at https://www.geodatasource.com                         :*/
    /*::                                                                         :*/
    /*::  For enquiries, please contact sales@geodatasource.com                  :*/
    /*::                                                                         :*/
    /*::  Official Web site: https://www.geodatasource.com                       :*/
    /*::                                                                         :*/
    /*::           GeoDataSource.com (C) All Rights Reserved 2019                :*/
    /*::                                                                         :*/
    /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("K")) {
                dist = dist * 1.609344;
            } else if (unit.equals("N")) {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }

    public void goToAdmin(View view) {

        nextActivity(context, Signup_gotozeal.class, "", null);


    }

    Bitmap bitmap = null;

    private void getCurrentPlaceLikelihoods(final boolean changeAddress) {
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,
                Place.Field.LAT_LNG, Place.Field.PHOTO_METADATAS);

        // Get the likely places - that is, the businesses and other points of interest that
        // are the best match for the device's current location.
        @SuppressWarnings("MissingPermission") final FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.builder(placeFields).build();
        Task<FindCurrentPlaceResponse> placeResponse = mPlacesClient.findCurrentPlace(request);
        placeResponse.addOnCompleteListener(this,
                new OnCompleteListener<FindCurrentPlaceResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                        if (task.isSuccessful()) {
                            FindCurrentPlaceResponse response = task.getResult();
                            // Set the count, handling cases where less than 5 entries are returned.
                            int count;
                            if (response.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                                count = response.getPlaceLikelihoods().size();
                            } else {
                                count = M_MAX_ENTRIES;
                            }

                            int i = 0;
                            mLikelyPlaceNames = new String[count];
                            mLikelyPlaceAddresses = new String[count];
                            mLikelyPlaceAttributions = new String[count];
                            mLikelyPlaceLatLngs = new LatLng[count];

                            //placesLists = new ArrayList<>();
                            placesLists.clear();
                            double minValue = 0;
                            for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                                bitmap = null;
                                Place currPlace = placeLikelihood.getPlace();
                                mLikelyPlaceNames[i] = currPlace.getName();
                                mLikelyPlaceAddresses[i] = currPlace.getAddress();
                                /*mLikelyPlaceAttributions[i] = (currPlace.getAttributions() == null) ?
                                        null : String.join(" ", currPlace.getAttributions());*/
                                mLikelyPlaceLatLngs[i] = currPlace.getLatLng();
                                Log.i(TAG, "changeAddress" + changeAddress);
                                if (changeAddress && minValue < placeLikelihood.getLikelihood()) {
                                    Log.i(TAG, "You set am " + changeAddress);
                                    //if (minValue < placeLikelihood.getLikelihood()) {
                                    Log.i(TAG, "You set am");
                                    autocompleteFragment.setText(currPlace.getName());
                                    origin = currPlace.getLatLng();
                                    originName = currPlace.getName();
                                    originID = currPlace.getId();
                                    if (originName.contains("ICT")) {
                                        //Toast.makeText(context, "Location " + originName, Toast.LENGTH_SHORT).show();
                                        autocompleteFragment.setText(currPlace.getName());
                                        originID = currPlace.getId();
                                    }


                                }
                                minValue = placeLikelihood.getLikelihood();
                                String currLatLng = (mLikelyPlaceLatLngs[i] == null) ?
                                        "" : mLikelyPlaceLatLngs[i].toString();


                                String placeId = currPlace.getId();

                                // Get the photo metadata.
                                /*PhotoMetadata photoMetadata = null;
                                try {
                                    photoMetadata = currPlace.getPhotoMetadatas().get(0);
                                } catch (Exception e) {
                                    Log.e(TAG, e.getMessage());
                                }

                                if (photoMetadata != null) {
                                    // Get the attribution text.
                                    String attributions = photoMetadata.getAttributions();

                                    // Create a FetchPhotoRequest.
                                    FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                                            .setMaxWidth(150) // Optional.
                                            .setMaxHeight(150) // Optional.
                                            .build();
                                    mPlacesClient.fetchPhoto(photoRequest).addOnSuccessListener(new OnSuccessListener<FetchPhotoResponse>() {
                                        @Override
                                        public void onSuccess(FetchPhotoResponse fetchPhotoResponse) {
                                            bitmap = fetchPhotoResponse.getBitmap();
                                            Log.e(TAG, fetchPhotoResponse.getBitmap().toString() + " bitmap found: " + bitmap.getHeight());
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            if (e instanceof ApiException) {
                                                ApiException apiException = (ApiException) e;
                                                int statusCode = apiException.getStatusCode();
                                                // Handle error with given status code.
                                                Log.e(TAG, "Place not found: " + e.getMessage());
                                            }
                                        }
                                    });

                                }*/
                                placesLists.add(new PlacesList(i, currPlace.getName(), currPlace.getAddress(), currPlace.getAttributions(), currPlace.getLatLng(), bitmap, placeId));
                                //placesLists.add(new PlacesList(i, currPlace.getName(), currPlace.getAddress(), currPlace.getAttributions(), currPlace.getLatLng(), bitmap, currPlace.getPhoneNumber()));

                                Log.i("TAG_LOCATE_LIKELYHOOD", placeLikelihood.toString());
                                Log.i("TAG_LOCATE", String.format("Place " + currPlace.getName()
                                        + " has likelihood: " + placeLikelihood.getLikelihood()
                                        + " at " + currLatLng));

                                i++;
                                if (i > (count - 1)) {
                                    break;
                                }
                            }

                            if (mLastKnownLocation != null) {
                                Log.d("TAG1", "Latitude: " + mLastKnownLocation.getLatitude());
                                Log.d("TAG2", "Longitude: " + mLastKnownLocation.getLongitude());
                                //defaultAddress(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));

                                Marker marker4 = mMap.addMarker(new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude())).title(originName).snippet(originAddress));
                                marker4.setTag(originID);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                //mMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
                                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                                //origin = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                                //defaultAddress(origin);
                                //Log.i("addressDefault_TAG", addressDefault);
                                //autocompleteFragment.setText(addressDefault);

                            } else {
                                Log.d("TAG3", "Current location is null. Using defaults.");
                                Log.e("TAG4", "Exception: %s", task.getException());
                                mMap.moveCamera(CameraUpdateFactory
                                        .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            }

                            showNearbyPlaces = true;
                            nearest_location_adapter.notifyDataSetChanged();
                            //floatingActionNearMe.setVisibility(View.VISIBLE);


                            // COMMENTED OUT UNTIL WE DEFINE THE METHOD
                            // Populate the ListView
                            //fillPlacesList();
                        } else {
                            Exception exception = task.getException();
                            if (exception instanceof ApiException) {
                                ApiException apiException = (ApiException) exception;
                                Log.e("TAG_EC", "Place not found: " + apiException.getStatusCode());
                                Log.e("TAG_EC", "Place not found: " + apiException.getMessage());
                            }
                        }
                    }
                });
    }

    private void getDeviceLocation(final boolean changeAddress) {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            //getCurrentPlaceLikelihoods(changeAddress);
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            /*if (mLastKnownLocation != null) {
                                Log.d("TAG1", "Latitude: " + mLastKnownLocation.getLatitude());
                                Log.d("TAG2", "Longitude: " + mLastKnownLocation.getLongitude());
                                defaultAddress(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));

                                Marker marker4 = mMap.addMarker(new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude())).title(knownName).snippet(addressDefault));
                                marker4.setTag(originID);
                            //mMap.addMarker(new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(),
                                    //mLastKnownLocation.getLongitude())).title("Current Location:\n" + origin.toString()));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                //mMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
                                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                                //origin = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                                //defaultAddress(origin);
                                //Log.i("addressDefault_TAG", addressDefault);
                                //autocompleteFragment.setText(addressDefault);

                            } else {
                                Log.d("TAG3", "Current location is null. Using defaults.");
                                Log.e("TAG4", "Exception: %s", task.getException());
                                mMap.moveCamera(CameraUpdateFactory
                                        .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            }*/

                            getCurrentPlaceLikelihoods(changeAddress);
                            //build_retrofit_and_get_nearbyplaces(new LatLng(mLastKnownLocation.getLatitude(),
                            //      mLastKnownLocation.getLongitude()));
                        } else {
                            Log.i(TAG, "Location cannot be detected");
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void pickCurrentPlace(boolean changeAddress) {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            getDeviceLocation(changeAddress);
        } else {
            // The user has not granted permission.
            Log.i("TAG4", "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title("Moshood Abiola Polytechnic")
                    .position(mDefaultLocation)
                    .snippet("Ojere"));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    private AdapterView.OnItemClickListener listClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            // position will give us the index of which place was selected in the array
            LatLng markerLatLng = mLikelyPlaceLatLngs[position];
            String markerSnippet = mLikelyPlaceAddresses[position];
            if (mLikelyPlaceAttributions[position] != null) {
                markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[position];
            }

            // Add a marker for the selected place, with an info window
            // showing information about that place.
            mMap.addMarker(new MarkerOptions()
                    .title(mLikelyPlaceNames[position])
                    .position(markerLatLng)
                    .snippet(markerSnippet));

            // Position the map's camera at the location of the marker.
            mMap.moveCamera(CameraUpdateFactory.newLatLng(markerLatLng));
        }
    };

    private void fillPlacesList() {
        // Set up an ArrayAdapter to convert likely places into TextViews to populate the ListView
        ArrayAdapter<String> placesAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mLikelyPlaceNames);
        lstPlaces.setAdapter(placesAdapter);
        //lstPlaces.setOnItemClickListener(listClickedHandler);
    }


    class pickUpLocation extends AsyncTask<Void, Void, Void> {
        boolean changeAddress;

        public pickUpLocation(boolean changeAddress) {
            this.changeAddress = changeAddress;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            pickCurrentPlace(changeAddress);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Toasty.info(context, "Thank you for using MAPOLY Tour, Kindly send us a feedback.", Toast.LENGTH_SHORT, true);
            //Toasty.info(context, "Thank you for using MAPOLY Tour.", Toast.LENGTH_SHORT, true);
            //((Activity) c.get()).finish();
        }
    }


    public static Dialog dialog;

    public void showDialog(Activity activity, String title, String snippet, String placeID) {

        dialog = new Dialog(activity);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_info_contents);

        Button btndialog = dialog.findViewById(R.id.btndialog);
        btndialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        TextView title1 = (dialog.findViewById(R.id.title));
        title1.setText(title);

        TextView snippet1 = (dialog.findViewById(R.id.snippet));
        snippet1.setText(snippet);

        final TextView description = dialog.findViewById(R.id.description);
        description.setVisibility(View.GONE);

        final ImageView imageViewDetail = dialog.findViewById(R.id.imageViewDetail);

        String placeId = placeID;
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
                            description.setText(attributions);
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
                                            .into(imageViewDetail);
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
                                    .load(R.drawable.mapolylogo)
                                    .into(imageViewDetail);
                            Log.i("PHOTO_STATUS", "Photo Empty");
                        }
                    }
                });

        dialog.show();

    }
}
