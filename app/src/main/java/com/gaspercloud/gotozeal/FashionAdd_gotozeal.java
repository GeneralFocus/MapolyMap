package com.gaspercloud.gotozeal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.gaspercloud.gotozeal.entitiesDB.customersDatabase;
import com.gaspercloud.gotozeal.entitiesDB.productDatabase;
import com.gaspercloud.gotozeal.model.Customers;
import com.gaspercloud.gotozeal.model.Product;
import com.gaspercloud.gotozeal.model.State;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

import static com.gaspercloud.gotozeal.Constants.COUNTRY_CODE_NG;
import static com.gaspercloud.gotozeal.Constants.countDown;
import static com.gaspercloud.gotozeal.Constants.nextActivity;

public class FashionAdd_gotozeal extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {//}, LocationListener {


    //For Registration
    static EditText name;
    static EditText price;
    static EditText shortdesc, longdesc, whomToCall;
    static EditText password;
    Spinner state;
    CheckBox terms;
    Switch switchUser;
    Button login, signup;
    static ImageView storeimage;//, storeimage1, storeimage2;

    static Spinner spinnerCategory;

    // add items into spinner dynamically
    public void addItemsOnSpinner2() {

        spinnerCategory = findViewById(R.id.spinnerCategory);
        List<String> list = new ArrayList<>();
        list.add("General");
        list.add("School of Business & Management Studies");
        list.add("School of Communication & Information Technology");
        list.add("School of Engineering");
        list.add("School of Environmental Studies");
        list.add("School of Science and Technology");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(dataAdapter);
    }


    private static Bitmap thumbnail, thumbnail1, thumbnail2;

    //For Login
    EditText password_Login;
    EditText phone_Login;
    //CheckBox terms_Login;
    Button signup_Login, forgetpassword_Login, login_login;

    //For Verification
    EditText phone_pin;
    Button resend_pin, signup_pin;
    TextView view_pin2;


    static ProgressBar progressBar;
    Button learnMore;
    Context context;

    static View viewToHide_Register, viewToHide_Login, viewToHide_pin;

    String state_S;
    static private boolean status_progress;
    static private int start;

    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;

    //firebase auth object
    private FirebaseAuth mAuth;

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db;

    //hold response data
    private static Customers customers1Fashion;

    private String password_secured;

    boolean doesUserExist;
    private DocumentReference docRef;
    SessionManagement sessionManagement;

    private List<State> stateList;
    private int GALLERY = 1, CAMERA = 2;
    private static int SELECTCONSTANT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        thumbnail = null;
        thumbnail1 = null;
        thumbnail2 = null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        /*if (user != null) {
            // User is signed in
            //FirebaseAuth.getInstance().signOut();
            nextActivity(context, HomeScreen.class, "", "", true);//goto /logIn Page
        }*/
        super.onCreate(savedInstanceState);


        customers1Fashion = new Customers();
        stateList = new ArrayList<>();

        setContentView(R.layout.activity_fashionadd_gotozeal);
/*
        if (!checkPermission()) {
            askPermission();
        }*/

        sessionManagement = new SessionManagement(context);

        //initializing objects
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        doesUserExist = false;

        start = 1;
        status_progress = false;


        password_Login = findViewById(R.id.password_Login);
        storeimage = findViewById(R.id.storeimage);
        //storeimage1 = findViewById(R.id.storeimage1);
        //storeimage2 = findViewById(R.id.storeimage2);
        phone_Login = findViewById(R.id.phone_Login);
        //terms_Login = findViewById(R.id.terms_Login);
        signup_Login = findViewById(R.id.signup_Login);
        signup_Login.setOnClickListener(this);
        login_login = findViewById(R.id.login_login);
        login_login.setOnClickListener(this);
        forgetpassword_Login = findViewById(R.id.forgetpassword_Login);
        forgetpassword_Login.setOnClickListener(this);

        //For Verification
        phone_pin = findViewById(R.id.phone_pin);
        resend_pin = findViewById(R.id.resend_pin);
        resend_pin.setEnabled(false);//it is enable when sms sent count reaches threshold of 1 minutes
        resend_pin.setBackgroundColor(getResources().getColor(R.color.editTextBG));
        resend_pin.setOnClickListener(this);
        signup_pin = findViewById(R.id.signup_pin);
        signup_pin.setEnabled(false);
        signup_pin.setBackgroundColor(getResources().getColor(R.color.editTextBG));
        signup_pin.setOnClickListener(this);
        view_pin2 = findViewById(R.id.view_pin2);

        //For Verification
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        shortdesc = findViewById(R.id.shortdesc);
        whomToCall = findViewById(R.id.whomToCall);
        longdesc = findViewById(R.id.longdesc);

        switchUser = findViewById(R.id.switchUser);
        password = findViewById(R.id.password);
        state = findViewById(R.id.city);
        //state.setOnItemSelectedListener(this);

        //stateMethodList(db);//fetch states Available

        terms = findViewById(R.id.terms);
        //terms.setVisibility(View.GONE);
        signup = findViewById(R.id.signup);
        signup.setOnClickListener(this);
        login = findViewById(R.id.login);
        //login.setOnClickListener(this);
        learnMore = findViewById(R.id.learnMore);
        learnMore.setVisibility(View.GONE);
        //learnMore.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        viewToHide_Register = findViewById(R.id.viewToHide_Register);
        viewToHide_Login = findViewById(R.id.viewToHide_Login);
        viewToHide_pin = findViewById(R.id.viewToHide_pin);
        hideItems(status_progress, start);//progressBar view is not needed at first

        addItemsOnSpinner2();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        // create GoogleApiClient
        //      createGoogleApi();
    }
/*
    private GoogleApiClient googleApiClient;

    // Create GoogleApiClient instance
    private void createGoogleApi() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }*/

    private void stateMethodList(FirebaseFirestore db) {
        //fetch All States
        db.collection(getString(R.string.customers_state))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Timber.i("StateCheck " + document.getId() + " => " + document.getData());
                                /*Gson gson = new Gson();
                                String toJson = gson.toJson(document.getData());//convert to Json
                                State state = gson.fromJson(toJson, State.class);//convert to Object
                                */
                                State state = document.toObject(State.class);//convert to Object
                                stateList.add(state);
                            }
                            ArrayAdapter<State> dataAdapter = new ArrayAdapter<>(context,
                                    android.R.layout.simple_spinner_item, stateList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            state.setAdapter(dataAdapter);
                        } else {
                            Timber.e(task.getException());
                        }
                    }
                });
    }

    static void hideItems(boolean status_progress, int start) {

/*

                viewToHide_Login.setVisibility(start == 0 ? View.VISIBLE : View.GONE );
                viewToHide_Register.setVisibility(start == 1 ? View.GONE :  View.VISIBLE);
                viewToHide_pin.setVisibility(start == 2 ? View.GONE : status_progress ? View.GONE : View.VISIBLE);
 */
        progressBar.setVisibility(status_progress ? View.VISIBLE : View.GONE);

        switch (start) {
            case 0:
                viewToHide_Login.setVisibility(status_progress ? View.GONE : View.VISIBLE);
                viewToHide_Register.setVisibility(View.GONE);
                viewToHide_pin.setVisibility(View.GONE);
                break;
            case 1:
                //if (stateList.size() > 0) {
                viewToHide_Login.setVisibility(View.GONE);
                viewToHide_Register.setVisibility(status_progress ? View.GONE : View.VISIBLE);
                viewToHide_pin.setVisibility(View.GONE);
                /*} else {
                    //Toast.makeText(context, getString(R.string.network_error_message), Toast.LENGTH_SHORT).show();
                    Toasty.warning(context, getString(R.string.network_error_message), Toast.LENGTH_SHORT, true).show();
                    stateMethodList(db);//fetch states Available
                }*/
                break;
            case 2:
                viewToHide_Login.setVisibility(View.GONE);
                viewToHide_Register.setVisibility(View.GONE);
                viewToHide_pin.setVisibility(status_progress ? View.GONE : View.VISIBLE);
                break;
        }


    }

    @Override
    public void onBackPressed() {
        //if (start == 0) {//only close app when on the firstView i.e LoginView
        super.onBackPressed();
      /*  } else {
            start--;
            status_progress = false;
            hideItems(status_progress, start);
        }*/
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        State stateSelectedItem = (State) parent.getSelectedItem();
        state_S = stateSelectedItem.getCode();

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public void onClick(View v) {
        status_progress = false;//do not show progressBar
        String code;
        switch (v.getId()) {
            case R.id.signup:
                //String email_S = email.getText().toString();
                String name_S = name.getText().toString();
                String price_S = price.getText().toString();
                String shortdesc_S = shortdesc.getText().toString();
                String whomToCall_S = whomToCall.getText().toString();
                final String longdesc_S = longdesc.getText().toString();
                /*String password_S = password.getText().toString();//random(50);
                String email_S = getString(R.string.no_email_set, username_S);*/

                /*
                if (email_S.isEmpty()){
                    email.setError(getString(R.string.empty_error_message));
                    email.setFocusable(true);
                    return;
                }
                */

                if (name_S.isEmpty()) {
                    name.setError(getString(R.string.empty_error_message));
                    name.setFocusable(true);
                    return;
                }
                if (price_S.isEmpty()) {
                    price.setError(getString(R.string.empty_error_message));
                    price.setFocusable(true);
                    return;
                }

                if (shortdesc_S.isEmpty()) {
                    shortdesc.setError(getString(R.string.empty_error_message));
                    shortdesc.setFocusable(true);
                    return;
                }

                if (longdesc_S.isEmpty()) {
                    longdesc.setError(getString(R.string.empty_error_message));
                    longdesc.setFocusable(true);
                    return;
                }

                if (whomToCall_S.isEmpty() || whomToCall_S.length() < 11) {
                    whomToCall.setError(getString(R.string.empty_error_message));
                    whomToCall.setFocusable(true);
                    return;
                }

                if (thumbnail == null) {
                    Toast.makeText(context, "Image is not set.", Toast.LENGTH_SHORT).show();
                    return;
                }
                /*
                if (thumbnail1 == null) {
                    Toast.makeText(context, "Exterior image is not set.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (thumbnail2 == null) {
                    Toast.makeText(context, "Other image is not set.", Toast.LENGTH_SHORT).show();
                    return;
                }
                */

                //convert password to secured for fireStore
                //password_secured = md5(password_S);

                state_S = "Abeokuta";
                /*if (state_S.isEmpty()) {
                    state.setFocusable(true);
                    return;
                }*/

                /*if (!terms.isChecked()) {
                    //Toast.makeText(context, getString(R.string.no_terms), Toast.LENGTH_SHORT).show();
                    Toasty.warning(context, getString(R.string.no_terms), Toast.LENGTH_SHORT, true).show();
                    terms.setError(getString(R.string.no_terms));
                    terms.setFocusable(true);
                    return;
                }*/


                Product product = new Product();
                product.setName(name_S);
                product.setSlug(spinnerCategory.getSelectedItem().toString());
                product.setLatitude(price_S);
                product.setLongitude(shortdesc_S);
                product.setDescription(longdesc_S);
                product.setParent_id(Signup_gotozeal.customers1.getId());
                product.setButton_text(whomToCall_S);
                try {
                    String myBase64Image = encodeToBase64(thumbnail, Bitmap.CompressFormat.JPEG, 100);
                    //String myBase64Image1 = encodeToBase64(thumbnail1, Bitmap.CompressFormat.JPEG, 100);
                    //String myBase64Image2 = encodeToBase64(thumbnail2, Bitmap.CompressFormat.JPEG, 100);
                    //Bitmap myBitmapAgain = decodeBase64(myBase64Image);
                    product.setImages(myBase64Image);
                    /*product.setImagesone(myBase64Image1);
                    product.setImagestwo(myBase64Image2);*/
                } catch (Exception c) {
                    Log.i("TAG_IMMAGE_ERROR", c.getMessage());
                }
                Timber.d(product.toString());
                start = 1;
                status_progress = true;//show progressBar
                hideItems(status_progress, start);
                //Toast.makeText(context, getString(R.string.network_loading_message), Toast.LENGTH_LONG).show();
                Toasty.info(context, "Adding new Tour location", Toast.LENGTH_SHORT, true).show();
                // for signup
                //Create a handler for the RetrofitInstance interface//

                status_progress = false;//hide progressBar
                new AddFashion(context, product).execute();

                break;
            case R.id.login:
                //go Back to Login
                //nextActivity(context, MainActivity.class, "", "");
                start = 0;
                hideItems(status_progress, start);
                break;
            case R.id.login_login:
                //register user
                start = 1;
                hideItems(status_progress, start);
                break;
            case R.id.signup_pin:
                //login user
                code = phone_pin.getText().toString().trim();
                if (code.isEmpty()) {
                    phone_pin.setError(getString(R.string.empty_error_message));
                    phone_pin.setFocusable(true);
                    return;
                }
                if (code.length() < 6) {
                    phone_pin.setError(getString(R.string.pin_error_message));
                    phone_pin.setFocusable(true);
                    return;
                }
                //verifying the code entered manually
                verifyVerificationCode(code);
                break;
            case R.id.resend_pin:
                //resend user code
                sendVerificationCode(customers1Fashion.getUsername());
                break;
            case R.id.learnMore:
                //send user to learnMore page
                Toast.makeText(context, "Learn More coming soon.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.signup_Login:
                start = 0;
                final String phone_Login_1 = phone_Login.getText().toString().trim();
                if (phone_Login_1.isEmpty()) {
                    phone_Login.setError(getString(R.string.empty_error_message));
                    phone_Login.setFocusable(true);
                    return;
                }
                if (!TextUtils.isDigitsOnly(phone_Login_1) || phone_Login_1.length() < 11) {
                    phone_Login.setError(getString(R.string.mobile_error_message));
                    phone_Login.setFocusable(true);
                    return;
                }

                final String password_Login_1 = password_Login.getText().toString().trim();
                if (password_Login_1.isEmpty()) {
                    password_Login.setError(getString(R.string.empty_error_message));
                    password_Login.setFocusable(true);
                    return;
                }
                if (password_Login_1.length() < 3) {
                    password_Login.setError(getString(R.string.names_error_message, getString(R.string._password)));
                    password_Login.setFocusable(true);
                    return;
                }

                status_progress = true;//show progressBar
                hideItems(status_progress, start);
                Timber.d("Document Snapshot: ");
                Customers customers2 = new Customers();
                customers2.setUsername(phone_Login_1);
                customers2.setPassword(password_Login_1);
                new LoginUser(context, customers2).execute();

                /*
                //verify & fetch user in fireStore
                docRef = db.collection(getString(R.string.customers_table)).document(phone_Login_1);

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                doesUserExist = true;
                            } else {
                                doesUserExist = false;
                            }


                            status_progress = false;//show progressBar
                            if (doesUserExist) {
                                docRef.get(source).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Timber.d("Document Snapshot: " + documentSnapshot.exists());
                                        if (documentSnapshot.exists()) {
                                            customers1Fashion = documentSnapshot.toObject(Customers.class);
                                            password_secured = md5(password_Login_1);
                                            if (customers1Fashion.getPassword().equals(password_secured)) {
                                                start = 2;
                                                hideItems(status_progress, start);
                                                sendVerificationCode(customers1Fashion.getUsername());
                                                customers1Fashion.setPassword(null);//to porevent sniffing of password
                                            } else {
                                                hideItems(status_progress, start);
                                                Snackbar snackbar = Snackbar.make(findViewById(R.id.viewToHide_Login), getString(R.string.credential_error_message), Snackbar.LENGTH_LONG);
                                                snackbar.setAction(getString(R.string.register_button), new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        //do something If available
                                                        emptyValues(0);
                                                        start = 1;
                                                        hideItems(status_progress, start);
                                                    }
                                                });
                                                snackbar.show();
                                            }

                                        } else {
                                            //Anything you want to do
                                        }
                                    }
                                });
                            } else {
                                Snackbar snackbar = Snackbar.make(findViewById(R.id.viewToHide_Login), getString(R.string.error_message_exist), Snackbar.LENGTH_LONG);
                                snackbar.setAction(getString(R.string.register_button), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //do something If available
                                        emptyValues(0);
                                        start = 1;
                                        phone.setText(phone_Login_1);
                                        hideItems(status_progress, start);
                                    }
                                });
                                snackbar.show();
                                hideItems(status_progress, start);
                            }

                        }
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.warning(context, getString(R.string.network_error_message), Toast.LENGTH_LONG, true).show();
                                //Toast.makeText(context, getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
                                Timber.e(e);
                                status_progress = false;
                                hideItems(status_progress, start);
                            }
                        });*/

                break;
        }
    }

    private void emptyValues(int frame) {
        if (frame == 0) {
            //For Login
            password_Login.setText("");
            phone_Login.setText("");
            //terms_Login.setChecked(true);
        } else if (frame == 1) {
            //For Registration
            name.setText("");
            price.setText("");
            shortdesc.setText("");
            longdesc.setText("");
            password.setText("");
            state.setSelection(0, true);
            terms.setChecked(true);
        } else if (frame == 2) {
            phone_pin.setText("");
            resend_pin.setEnabled(false);//it is enable when sms sent count reaches threshold of 1 minutes
            resend_pin.setBackgroundColor(getResources().getColor(R.color.editTextBG));

            signup_pin.setEnabled(false);
            signup_pin.setBackgroundColor(getResources().getColor(R.color.editTextBG));
        }
    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                COUNTRY_CODE_NG + "" + mobile.substring(1),
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                phone_pin.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toasty.error(context, e.getMessage(), Toast.LENGTH_LONG, true).show();
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            status_progress = false;
            start = 0;
            hideItems(status_progress, start);
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
            Toasty.info(context, getString(R.string.sms_verification_code_message), Toast.LENGTH_LONG, true).show();
            //Toast.makeText(context, getString(R.string.sms_verification_code_message), Toast.LENGTH_LONG).show();
            countDown(60000, 1000, view_pin2, context, resend_pin);

            signup_pin.setEnabled(true);
            signup_pin.setBackgroundColor(getResources().getColor(R.color.primaryColor));
        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(FashionAdd_gotozeal.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sessionManagement.createLoginSession(customers1Fashion.getUsername());
                            //verification successful we will start the profile activity
                            nextActivity(context, HomeScreen.class, "", "", true);
                        } else {

                            //verification unsuccessful.. display an error message
                            String message = getString(R.string.error_general_message);

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = getString(R.string.error_verify_message);
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.viewToHide_pin), message, Snackbar.LENGTH_LONG);
                            //Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction(getString(R.string.code_resend), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //resend user code
                                    sendVerificationCode(customers1Fashion.getUsername());
                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }

    private Location lastLocation;

    private void writeActualLocation(Location location) {
        price.setText("Lat: " + location.getLatitude());
        shortdesc.setText("Long: " + location.getLongitude());

    }
/*
    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        writeActualLocation(location);
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
*/

    static class AddFashion extends AsyncTask<Void, Void, Void> {
        private Product u;
        private WeakReference<Context> c;

        public AddFashion(Context c, Product u) {
            this.c = new WeakReference<>(c);
            this.u = u;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            productDatabase ud = productDatabase.getAppDatabase(c.get());
            ud.productDAO().insert(u);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(c.get(), "New Location was added successfully.", Toast.LENGTH_SHORT).show();
            //((Activity) c.get()).finish();
            start = 1;
            status_progress = false;//show progressBar
            hideItems(status_progress, start);


            name.setText("");
            price.setText("");
            shortdesc.setText("");
            longdesc.setText("");
            whomToCall.setText("");
            spinnerCategory.setSelection(0);
            storeimage.setImageDrawable(c.get().getResources().getDrawable(R.drawable.bt_ic_camera_dark));
            ((Activity) c.get()).finish();
        }
    }

    class LoginUser extends AsyncTask<Void, Void, Void> {
        private Customers u;
        private WeakReference<Context> c;

        public LoginUser(Context c, Customers u) {
            this.c = new WeakReference<>(c);
            this.u = u;
            Log.i("Login_GotoZeal", u.toString());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            customers1Fashion = new Customers();
            customersDatabase ud = customersDatabase.getAppDatabase(c.get());
            List<Customers> customersList = ud.customersDAO().getAllUsers();
            for (Customers customers : customersList) {
                Log.i("CHECK", customers.toString());
            }
            customers1Fashion = ud.customersDAO().LoginUsers(u.getUsername(), u.getPassword());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (customers1Fashion != null) {
                customers1Fashion.setPassword(null);//to porevent sniffing of password
                //verification successful we will start the profile activity
                nextActivity(context, HomeScreen.class, "", "", true);
            } else {
                status_progress = false;
                Snackbar snackbar = Snackbar.make(findViewById(R.id.viewToHide_Login), getString(R.string.credential_error_message), Snackbar.LENGTH_LONG);
                snackbar.setAction(getString(R.string.register_button), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //do something If available
                        emptyValues(0);

                        start = 1;
                        hideItems(status_progress, start);
                    }
                });
                snackbar.show();
                start = 0;
                hideItems(status_progress, start);

            }
            //((Activity) c.get()).finish();
        }
    }

    public void ImageButton(View v) {
        SELECTCONSTANT = 1;
        showPictureDialog();
    }

    public void ImageButton1(View v) {
        SELECTCONSTANT = 2;
        showPictureDialog();
    }

    public void ImageButton2(View v) {
        SELECTCONSTANT = 3;
        showPictureDialog();
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    /*String path = saveImage(bitmap);
                    loadImageFromStorage(path, imageview);
                    Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    */
                    switch (SELECTCONSTANT) {
                        case 1:
                            thumbnail = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                            storeimage.setImageBitmap(thumbnail);
                            break;
                        case 2:
                            thumbnail1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                            //storeimage1.setImageBitmap(thumbnail1);
                            break;
                        case 3:
                            thumbnail2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                            //storeimage2.setImageBitmap(thumbnail2);
                            break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            switch (SELECTCONSTANT) {
                case 1:
                    thumbnail = (Bitmap) data.getExtras().get("data");
                    storeimage.setImageBitmap(thumbnail);
                    break;
                case 2:
                    thumbnail1 = (Bitmap) data.getExtras().get("data");
                    //storeimage1.setImageBitmap(thumbnail1);
                    break;
                case 3:
                    thumbnail2 = (Bitmap) data.getExtras().get("data");
                    //storeimage2.setImageBitmap(thumbnail2);
                    break;
            }            /*
            String path = saveImage(thumbnail);
            loadImageFromStorage(path, imageview);
            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
            */
        }
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

/*
    private final int REQ_PERMISSION = 999;

    // Check for permission to access Location
    private boolean checkPermission() {
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED);
    }

    // Asks for permission
    private void askPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                REQ_PERMISSION
        );
    }

    // Verify user's response of the permission requested
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    getLastKnownLocation();
                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
        }
    }

    // App cannot work without the permissions
    private void permissionsDenied() {
        // TODO close app and warn user
        Toast.makeText(context, "To add new location, you need to allow permission to access your Location.", Toast.LENGTH_SHORT).show();
        finish();
    }

    // Get last known location
    private void getLastKnownLocation() {
        if (checkPermission()) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                Log.i("FashioAdd", "LasKnown location. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());
                writeActualLocation(lastLocation);
                startLocationUpdates();
            } else {
                startLocationUpdates();
            }
        } else askPermission();
    }

    private LocationRequest locationRequest;
    // Defined in mili seconds.
    // This number in extremely low, and should be used only for debug
    private final int UPDATE_INTERVAL = 1000;
    private final int FASTEST_INTERVAL = 900;

    // Start location Updates
    private void startLocationUpdates() {
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if (checkPermission())
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }*/


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
                                    price.setText(location.getLatitude() + "");
                                    shortdesc.setText(location.getLongitude() + "");
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
            price.setText(mLastLocation.getLatitude() + "");
            shortdesc.setText(mLastLocation.getLongitude() + "");
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

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
}
