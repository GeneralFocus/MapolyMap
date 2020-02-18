package com.gaspercloud.gotozeal;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.gaspercloud.gotozeal.model.Customers;
import com.gaspercloud.gotozeal.network.CheckInternetConnection;
import com.gaspercloud.gotozeal.ui.actionProductActivity.Orders;
import com.gaspercloud.gotozeal.ui.store.StoreFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class HomeScreen extends AppCompatActivity {

    public static Customers customersHome;

    //private Handler mHandler;
    public static AppBarLayout appBarLayout;
    SessionManagement sessionManagement;
    private AppBarConfiguration mAppBarConfiguration;
    private String title;
    private Context context;

    public static String catname;

    // toolbar titles respected to selected nav menu item
    //private String[] activityTitles;
    //private ImageView avatarView;
    private NavigationView navigationView;
    //private SliderLayout sliderShow;

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

        // load toolbar titles from string resources
        //activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        setContentView(R.layout.activity_home_screen);
/*
        Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
        TextView appname = findViewById(R.id.appname);
        appname.setTypeface(typeface);
*/
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        sessionManagement = new SessionManagement(context);
        customersHome = Signup_gotozeal.customers1;
        Log.i("TAG_customersHome", customersHome.toString());


        //set Custom toolbar to activity -----------------------------------------------------------

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = getString(R.string.app_name);
        initCollapsingToolbar(title);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //drawer.setVisibility(View.GONE);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setVisibility(View.GONE);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home//,
                // , R.id.nav_store, R.id.nav_slideshow,
                //R.id.nav_store
                //, R.id.nav_slideshow,
                //R.id.nav_tools, R.id.nav_share, R.id.nav_send
        )
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //changing avatarView Icon
        View header = navigationView.getHeaderView(0);
        //avatarView = header.findViewById(R.id.avatarView);

        //get and set profile details
        //verify & fetch user in fireStore
        /*FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(getString(R.string.customers_table)).document(sessionManagement.getUsername());
        docRef.get(source).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    customersHome = documentSnapshot.toObject(CustomersOLD.class);
                    //Timber.i("customersHome: " + customersHome.toString());
                    //Glide.with(context).load(customersHome.getAvatar_url()).placeholder(R.mipmap.ic_launcher_round).error(R.mipmap.ic_launcher).into(avatarView);

                } else {
                    //Anything you want to do
                    //nextActivity(context, Signup_gotozeal.class, "", "", true);//goto /logIn Page
                }
            }
        });*/
        if (!checkPermissions()) {
            requestPermissions();
        }
        permissionCALL();

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

    private void permissionCALL() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //Toast.makeText(context, "3", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(context)
                            .setCancelable(false)
                            .setTitle("File Permission")
                            .setMessage("Hi there! Grant access to your gallery?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
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
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
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

    /* @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.home_screen, menu);
         return true;
     }
*/
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (tellFragments()) {
            return;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private boolean tellFragments() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            //if (f != null && f instanceof StoreFragment)
            if (f instanceof StoreFragment)
                return ((StoreFragment) f).onBackPressed_StoreFragment();
        }
        return false;
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar(final String title) {
        final CollapsingToolbarLayout collapsingToolbar =
                findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle(" ");
        appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(false);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    //collapsingToolbar.setTitle(title);
                    collapsingToolbar.setTitle(" ");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
        //inflateImageSlider();
    }

    private void inflateImageSlider() {
/*
        // Using Image Slider -----------------------------------------------------------------------
        sliderShow = findViewById(R.id.slider);

        //populating Image slider
        ArrayList<String> sliderImages = new ArrayList<>();
        sliderImages.add("https://i0.wp.com/gotozeal.com/wp-content/uploads/2019/09/Food-processing-roles-cover.jpeg?fit=1024%2C576&ssl=1");
        sliderImages.add("https://www.printstop.co.in/images/flashgallary/large/calendar-diaries-home-banner.jpg");
        sliderImages.add("https://www.printstop.co.in/images/flashgallary/large/calendar-diaries-banner.jpg");
        sliderImages.add("https://i1.wp.com/gotozeal.com/wp-content/uploads/2019/09/wheat-bran-cover-1.jpeg?fit=1024%2C576&ssl=1");

        for (String s : sliderImages) {
            DefaultSliderView sliderView = new DefaultSliderView(this);
            sliderView.image(s);
            sliderShow.addSlider(sliderView);
        }

        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
*/
    }

    @Override
    protected void onStop() {
        //sliderShow.stopAutoCycle();
        super.onStop();
    }

    @Override
    protected void onResume() {

        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
        //sliderShow.startAutoCycle();
        super.onResume();
    }


    public void Notifications(View view) {
        Toasty.info(context, "Set Notifications here", Toasty.LENGTH_SHORT, true).show();
        /*startActivity(new Intent(IndividualProduct.this, NotificationActivity.class));
        finish();*/
    }

    public void viewCart(View view) {
//        nextActivity(context, Cart.class, "", null);
        //nextActivity(context, Orders.class, "status", "false", false);
        startActivity(new Intent(HomeScreen.this, Orders.class).putExtra("status", false));
    }

    public void LogOutView(View view) {
        startActivity(new Intent(HomeScreen.this, Signup_gotozeal.class));
    }

    private void tapview() {
/*
        new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(findViewById(R.id.notifintro), "Notifications", "Latest offers will be available here !")
                                .targetCircleColor(R.color.colorAccent)
                                .titleTextColor(R.color.colorAccent)
                                .titleTextSize(25)
                                .descriptionTextSize(15)
                                .descriptionTextColor(R.color.accent)
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)
                                .transparentTarget(true)
                                .outerCircleColor(R.color.first),
                        TapTarget.forView(findViewById(R.id.view_profile), "Profile", "You can view and edit your profile here !")
                                .targetCircleColor(R.color.colorAccent)
                                .titleTextColor(R.color.colorAccent)
                                .titleTextSize(25)
                                .descriptionTextSize(15)
                                .descriptionTextColor(R.color.accent)
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)
                                .transparentTarget(true)
                                .outerCircleColor(R.color.third),
                        TapTarget.forView(findViewById(R.id.cart), "Your Cart", "Here is Shortcut to your cart !")
                                .targetCircleColor(R.color.colorAccent)
                                .titleTextColor(R.color.colorAccent)
                                .titleTextSize(25)
                                .descriptionTextSize(15)
                                .descriptionTextColor(R.color.accent)
                                .drawShadow(true)
                                .cancelable(false)// Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)
                                .transparentTarget(true)
                                .outerCircleColor(R.color.second),
                        TapTarget.forView(findViewById(R.id.visitingcards), "Categories", "Product Categories have been listed here !")
                                .targetCircleColor(R.color.colorAccent)
                                .titleTextColor(R.color.colorAccent)
                                .titleTextSize(25)
                                .descriptionTextSize(15)
                                .descriptionTextColor(R.color.accent)
                                .drawShadow(true)
                                .cancelable(false)// Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)
                                .transparentTarget(true)
                                .outerCircleColor(R.color.fourth))
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        sessionManagement.setFirstTime(false);
                        Toasty.success(context, " You are ready to go !", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        // Boo
                    }
                }).start();
*/
    }

    public interface OnBackPressed {
        boolean onBackPressed_StoreFragment();
    }


    int PERMISSION_ID = 4144;

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                (Activity) context,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }
}