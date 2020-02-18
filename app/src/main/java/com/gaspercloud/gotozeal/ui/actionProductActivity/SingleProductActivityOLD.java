package com.gaspercloud.gotozeal.ui.actionProductActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
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
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.gaspercloud.gotozeal.POJO.Example;
import com.gaspercloud.gotozeal.R;
import com.gaspercloud.gotozeal.RetrofitMaps;
import com.gaspercloud.gotozeal.SessionManagement;
import com.gaspercloud.gotozeal.Signup_gotozeal;
import com.gaspercloud.gotozeal.entitiesDB.orderModelDatabase;
import com.gaspercloud.gotozeal.entitiesDB.productDatabase;
import com.gaspercloud.gotozeal.model.CartModel;
import com.gaspercloud.gotozeal.model.OrderModel;
import com.gaspercloud.gotozeal.model.Product;
import com.gaspercloud.gotozeal.network.CheckInternetConnection;
import com.github.florent37.expansionpanel.ExpansionLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import timber.log.Timber;

public class SingleProductActivityOLD extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener {

    private GoogleMap mMap;
    LatLng origin, originNext;
    LatLng dest;
    ArrayList<LatLng> MarkerPoints;
    TextView ShowDistanceDuration;
    Polyline line;


    //private SliderLayout productimage_slider;


    TextView productname;
    TextView productprice, old_price;
    //TextView addToCart;
    TextView buyNow;
    TextView productdesc;
    EditText quantityProductPage;
    LottieAnimationView addToWishlist;
    //EditText customheader;
    //EditText custommessage;
    LinearLayout hideQuantity;
    //RatingBar ratingBar_single;
    private LocationManager locationManager;
    private String provider;

    private String usermobile, useremail;

    private int quantity = 1;
    SessionManagement sessionManagement;

    private Product productModel;
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db;
    private DocumentReference docRef;

    private Context context;

    TextView add_to_cart;

    static double latDou, lonDou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // User is signed in
            FirebaseAuth.getInstance().signOut();
            nextActivity(context, Signup_gotozeal.class, "", "", true);//goto /logIn Page
        }*/
        setContentView(R.layout.activity_single_product_old);
        permissionCALL2();
        permissionCALL();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sessionManagement = new SessionManagement(context);

        add_to_cart = findViewById(R.id.add_to_cart);


        initialize_values();

        ShowDistanceDuration = findViewById(R.id.show_distance_time);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        this.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//Choosing the best criteria depending on what is available.
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
//provider = LocationManager.GPS_PROVIDER; // We want to use the GPS

// Initialize the location fields
        Location location = locationManager.getLastKnownLocation(provider);


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
    }

    private static final int PERMISSIONS_REQUEST_CODE = 11;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //makeCall();
            }
        }
    }

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

    private void initialize_values() {
        //productModel = (Product) getIntent().getSerializableExtra("product");
        int prdID = getIntent().getIntExtra("product", 1);

        productDatabase ud = productDatabase.getAppDatabase(context);
        //productData.setValue(ud.productDAO().getAllProduct());
        for (Product product : ud.productDAO().getAllProduct()) {
            if (prdID == product.getId()) {
                productModel = product;
                break;
            }
        }

        /*ImageView imageView = findViewById(R.id.productimage);
        List<Bitmap> bitmaps = new ArrayList<>();
        Bitmap myBitmapAgain = decodeBase64(productModel.getImages());
        bitmaps.add(myBitmapAgain);
        Bitmap myBitmapAgain1 = decodeBase64(productModel.getImagesone());
        bitmaps.add(myBitmapAgain1);
        Bitmap myBitmapAgain2 = decodeBase64(productModel.getImagestwo());
        bitmaps.add(myBitmapAgain2);*/

        //Glide.with(context).load(myBitmapAgain).placeholder(R.drawable.ic_menu_gallery).error(R.drawable.ic_photo_library_red_24dp).into(imageView);
        //imageView.setVisibility(View.GONE);
        //inflateImageSlider(bitmaps);

        productname = findViewById(R.id.productname);
        productprice = findViewById(R.id.productprice);
        old_price = findViewById(R.id.old_price);
        //addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);
        productdesc = findViewById(R.id.productdesc);
        quantityProductPage = findViewById(R.id.quantityProductPage);
        //addToWishlist = findViewById(R.id.add_to_wishlist);
        //ratingBar_single = findViewById(R.id.ratingBar_single);
        //customheader = findViewById(R.id.customheader);
        //custommessage = findViewById(R.id.custommessage);
        hideQuantity = findViewById(R.id.hideQuantity);
        //if (productModel.isDownloadable()) {
        //we do not need quantity to be displayed here
        hideQuantity.setVisibility(View.GONE);
        //}

        Spanned name = Html.fromHtml(Html.fromHtml(productModel.getName()).toString());
        Spanned desc = Html.fromHtml(Html.fromHtml(productModel.getDescription()).toString());
        String price = productModel.getLatitude();
        old_price.setText(productModel.getLongitude());
        //old_price.setVisibility(View.GONE);
        productprice.setText(price);
        latDou = Double.parseDouble(price.trim());
        lonDou = Double.parseDouble(productModel.getLongitude().trim());

        origin = new LatLng(getIntent().getDoubleExtra("lat", 0.0), getIntent().getDoubleExtra("lon", 0.0));
        dest = new LatLng(latDou, lonDou);

        Log.i("OLD_ORIGIN", origin.toString());
        Log.i("OLD_DESTINATION", dest.toString());

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Making call...", Toast.LENGTH_SHORT).show();
                makeCall(productModel.getButton_text());
            }
        });

/*
        String[] split_price = price.toString().split(" ");
        if (split_price.length > 1 && !split_price[0].isEmpty()) {
            productprice.setText(split_price[1]);
            old_price.setText(split_price[0]);
            old_price.setPaintFlags(old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            productprice.setText(split_price[0]);
            old_price.setVisibility(View.GONE);
        }*/
        productname.setText(name);
        productdesc.setText(desc);
        quantityProductPage.setText("1");
        //ratingBar_single.setRating(Float.parseFloat(productModel.getAverage_rating()));
        //ratingBar_single.setVisibility(View.GONE);

        //setting textwatcher for no of items field
        quantityProductPage.addTextChangedListener(productcount);
        //For Expanding Layout
        ExpansionLayout expansionLayout = findViewById(R.id.expansionLayoutDescription);
        expansionLayout.addListener(new ExpansionLayout.Listener() {
            @Override
            public void onExpansionChanged(ExpansionLayout expansionLayout, boolean expanded) {

            }
        });
        /*
        ExpansionLayout expansionLayoutReview = findViewById(R.id.expansionLayoutReview);
        expansionLayoutReview.addListener(new ExpansionLayout.Listener() {
            @Override
            public void onExpansionChanged(ExpansionLayout expansionLayout, boolean expanded) {

            }
        });
        */

        //show Item was added to wishList if true
      /*  db.collection(getString(R.string.customers_table)).document(sessionManagement.getUsername())
                .collection(context.getString(R.string.customers_wishlist)).document(String.valueOf(productModel.getId()))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        addToWishlist.playAnimation();
                    } else {
                        //leave wishlist alone
                    }
                } else {
                    Timber.e(task.getException());
                }
            }
        });*/


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

    public static File storeImageintoPath(Bitmap bitmap, String title, String randomvalues, Context context) {
        //File imagePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+ File.separator + appDirectoryName + File.separator);
        File imagePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator);
        if (!imagePath.exists())
            Log.d("TAGEXIST", "nowExit " + imagePath.mkdirs());

        OutputStream fOut = null;
        File file = new File(imagePath, title + ".png");

        //File file = new File(imagePath, title + "~" + System.currentTimeMillis() + ".png");

        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Log.e("TAGG", e.getMessage());
            // handle your exception here
        }

        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        } catch (Exception e) {
            Log.e("TAGG", e.getMessage());
            // handle your exception here
        }
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            Log.e("TAGG", e.getMessage());
            // handle your exception here
        }
        //saving Image To Phone Gallery
        /* ImagePath = MediaStore.Images.Media.insertImage(
          getContentResolver(),
          bitmap,
          newContactName + "~" + System.currentTimeMillis() + "_contactbackupapp",
          "contactbackupapp" + randomvalues
          //"Back Up"
         );*/
        ContentValues values = new ContentValues();
        //values.put(MediaStore.Images.Media.TITLE, title + "~" + System.currentTimeMillis() + "_contactbackupapp");
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, "decorationapp" + randomvalues);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.ImageColumns.BUCKET_ID, file.toString().toLowerCase(Locale.US).hashCode());
        values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, file.getName().toLowerCase(Locale.US));
        values.put("_data", file.getAbsolutePath());

        ContentResolver cr = context.getContentResolver();
        cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        return file;
    }

    private File saveReceivedImage(Bitmap bitmap, String imageName) {
        File outFile = null;
        try {
            File path = new File(context.getFilesDir(), "decoration" + File.separator + "images");
            if (!path.exists()) {
                path.mkdirs();
            }
            outFile = new File(path, imageName + ".jpeg");
            FileOutputStream outputStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            Log.e("TAGSAVE", "Saving received message failed with", e);
        } catch (IOException e) {
            Log.e("TAGSAVEEXCEP", "Saving received message failed with", e);
        }
        return outFile;
    }

    /*
        private void inflateImageSlider(List<Bitmap> sliderImages) {
            //private void inflateImageSlider(List<ImageProperties> sliderImages) {

            // Using Image Slider -----------------------------------------------------------------------
            productimage_slider = findViewById(R.id.productimage_slider);


            //populating Image slider
               // ArrayList<String> sliderImages = new ArrayList<>();
               // sliderImages.add("https://i0.wp.com/gotozeal.com/wp-content/uploads/2019/09/Food-processing-roles-cover.jpeg?fit=1024%2C576&ssl=1");
              //  sliderImages.add("https://www.printstop.co.in/images/flashgallary/large/calendar-diaries-home-banner.jpg");
               // sliderImages.add("https://www.printstop.co.in/images/flashgallary/large/calendar-diaries-banner.jpg");
                //sliderImages.add("https://i1.wp.com/gotozeal.com/wp-content/uploads/2019/09/wheat-bran-cover-1.jpeg?fit=1024%2C576&ssl=1");

            //for (ImageProperties imageProperties : sliderImages) {
            int cnt = 0;
            for (Bitmap imageProperties : sliderImages) {
                if (imageProperties == null) {
                    continue;
                }
                cnt++;
                DefaultSliderView sliderView = new DefaultSliderView(this);
                try {
    //                sliderView.image(storeImageintoPath(imageProperties, "dec1" + "_" + cnt, "dec", context));
                    sliderView.image(saveReceivedImage(imageProperties, "deco" + cnt));
                    //sliderView.image(saveBitmap(context, imageProperties,
                    //Bitmap.CompressFormat.JPEG, "image/jpeg", productModel.getName() + "_" + cnt));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //sliderView.image(imageProperties.getSrc());
                productimage_slider.addSlider(sliderView);
            }

            productimage_slider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);

        }
    */
    public void decrement(View view) {
        if (quantity > 1) {
            quantity--;
            quantityProductPage.setText(String.valueOf(quantity));
        }
    }

    public void increment(View view) {
        if (quantity < 500) {
            quantity++;
            quantityProductPage.setText(String.valueOf(quantity));
        } else {
            Toasty.error(context, getString(R.string.product_quantity_limit), Toast.LENGTH_LONG, true).show();
        }
    }

    //check that product count must not exceed 500
    TextWatcher productcount = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (quantityProductPage.getText().toString().equals("")) {
                quantityProductPage.setText("1");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //none
            if (Integer.parseInt(quantityProductPage.getText().toString()) >= 500) {
                Toasty.error(context, getString(R.string.product_quantity_limit), Toast.LENGTH_LONG, true).show();
            }
        }

    };


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

    public void addToCart(View view) {

     /*   if ( customheader.getText().toString().length() == 0 ||  custommessage.getText().toString().length() ==0 ){

            Snackbar.make(view, "Header or Message Empty", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else{
*/
        CartModel cartModel = new CartModel(Integer.parseInt(quantityProductPage.getText().toString()), productModel);
        db.collection(getString(R.string.customers_table)).document(sessionManagement.getUsername())
                .collection(getString(R.string.customers_cart)).document(String.valueOf(cartModel.getProduct().getId()))
                .set(cartModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toasty.success(context, getString(R.string.customers_cart_added, getString(R.string.customers_cart)), Toast.LENGTH_SHORT, true).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Timber.e(e);
                        Toasty.error(context, getString(R.string.network_error_message), Toast.LENGTH_SHORT, true).show();
                    }
                });

        //}
    }

    public void addToWishList(View view) {
        addToWishlist.playAnimation();
        CartModel cartModel = new CartModel(Integer.parseInt(quantityProductPage.getText().toString()), productModel);
        db.collection(getString(R.string.customers_table)).document(sessionManagement.getUsername())
                .collection(getString(R.string.customers_wishlist)).document(String.valueOf(cartModel.getProduct().getId()))
                .set(cartModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toasty.success(context, getString(R.string.customers_cart_added, getString(R.string.customers_wishlist)), Toast.LENGTH_SHORT, true).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Timber.e(e);
                        Toasty.error(context, getString(R.string.network_error_message), Toast.LENGTH_SHORT, true).show();
                    }
                });
    }

    public void goToCart(View view) {

       /* if ( customheader.getText().toString().length() == 0 ||  custommessage.getText().toString().length() ==0 ){

            Snackbar.make(view, "Header or Message Empty", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else {*/
        //addToCart(view);
        //nextActivity(context, Cart.class, "", null);
        //  }

        AddReview(null, context);


    }

    @Override
    public void onLocationChanged(Location location) {
        originNext = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(originNext).title("Moving Location:\n" + originNext.toString()));
        Log.i("TAG_ONCHANGE", location.toString());
    }

    @Override
    protected void onResume() {
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
        super.onResume();
        Log.v("TAG_RESUME", "Resuming");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);

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
            ((Activity) c.get()).finish();
        }
    }


    static String messageSave = "";

    private void AddReview(final OrderModel orderModel, final Context context) {
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
                                orderModel.setCustomer_id(Signup_gotozeal.customers1.getId());
                                orderModel.setStatus("false");
                                orderModel.setVersion(Signup_gotozeal.customers1.getUsername());
                                orderModel.setSet_paid(false);
                                orderModel.setNumber(productModel.getParent_id());
                                orderModel.setParent_id(productModel.getId());
                                orderModel.setCustomer_note(messageSave);
                                orderModel.setTotal("" + (Integer.parseInt(quantityProductPage.getText().toString()) * Float.parseFloat(productModel.getLatitude())));

                                new AddOrder(context, orderModel).execute();

                                //dialog.cancel();
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

        // Add a marker in Sydney and move the camera
        //LatLng Model_Town = new LatLng(28.7158727, 77.1910738);
//        Log.i("LATLONG", dest.toString());
        LatLng Model_Town = origin;//new LatLng(latDou, lonDou);
        mMap.addMarker(new MarkerOptions().position(Model_Town).title("Current Location:\n" + origin.toString()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Model_Town));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        mMap.clear();
        MarkerPoints.clear();
        MarkerPoints = new ArrayList<>();
        ShowDistanceDuration.setText("");
        // Adding new item to the ArrayList
        MarkerPoints.add(origin);
        MarkerPoints.add(dest);
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
        build_retrofit_and_get_response("driving");


        //Showing Distance to Cover
        double distance = distance(origin.latitude, origin.longitude, dest.latitude, dest.longitude, "K");
        /*int speed = 1 / 60;//a constant of HUMAN WALKING if speed is 1 second divided by 60minutes
        float time = (float) (distance / speed);
        int hours = (int) (time / 60); //since both are ints, you get an int
        int minutes = (int) (time % 60);*/
        //ShowDistanceDuration.setText("Distance:" + String.format("%.2f", distance) + " KM");
        //\nDuration:" + String.format("%d", hours) + ":" + String.format("%02d", minutes));


/*
        // Setting onclick event listener for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

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
                }

            }
        });
        */

        Button btnDriving = findViewById(R.id.btnDriving);
        //btnDriving.setVisibility(View.GONE);
        btnDriving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                build_retrofit_and_get_response("driving");
            }
        });

        Button btnWalk = findViewById(R.id.btnWalk);
        //btnWalk.setVisibility(View.GONE);
        btnWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                build_retrofit_and_get_response("walking");
            }
        });
    }

    private void build_retrofit_and_get_response(final String type) {
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

        Call<Example> call = service.getDistanceDuration("metric", origin.latitude + "," + origin.longitude, dest.latitude + "," + dest.longitude, type);
        //Call<Example> call = service.getDistanceDuration("metric", origin.toString(), dest.toString(), type);
        Log.i("TAG_SHOW_ORIGIN", origin.toString());
        Log.i("TAG_SHOW_DESTINATION", dest.toString());

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
                        ShowDistanceDuration.setText("TYPE:" + type + "\nDistance:" + distance + ", Duration:" + time);
                        String encodedString = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                        List<LatLng> list = decodePoly(encodedString);
                        mMap.clear();

                        line = mMap.addPolyline(new PolylineOptions()
                                .addAll(list)
                                .width(5)
                                //.width(20)
                                .color(Color.RED)
                                .geodesic(true)
                        );

                        MarkerOptions options1 = new MarkerOptions();
                        options1.position(origin);
                        options1.icon(BitmapDescriptorFactory.fromResource(R.drawable.locator));
                        mMap.addMarker(options1.title("Current Location - " + dest.toString()));
                        options1 = new MarkerOptions();
                        options1.position(dest);
                        options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        mMap.addMarker(options1.title("Destination Location - " + dest.toString()));

                    }
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("onFailure", t.toString());
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

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;


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
                                    Log.i("CURRENTLOCATION", origin.toString());
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
}
