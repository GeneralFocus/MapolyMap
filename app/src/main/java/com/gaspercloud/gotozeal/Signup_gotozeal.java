package com.gaspercloud.gotozeal;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.gaspercloud.gotozeal.entitiesDB.customersDatabase;
import com.gaspercloud.gotozeal.entitiesDB.productDatabase;
import com.gaspercloud.gotozeal.model.Customers;
import com.gaspercloud.gotozeal.model.Product;
import com.gaspercloud.gotozeal.model.State;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

import static com.gaspercloud.gotozeal.Constants.COUNTRY_CODE_NG;
import static com.gaspercloud.gotozeal.Constants.countDown;
import static com.gaspercloud.gotozeal.Constants.md5;
import static com.gaspercloud.gotozeal.Constants.nextActivity;
import static com.gaspercloud.gotozeal.model.Product.getAllProducts;

public class Signup_gotozeal extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //For Registration
    EditText phone;
    EditText surname;
    EditText othernames, address;
    EditText password;
    Spinner state;
    CheckBox terms;
    Switch switchUser;
    Button login, signup;

    //For Login
    EditText password_Login;
    EditText phone_Login;
    //CheckBox terms_Login;
    Button signup_Login, forgetpassword_Login, login_login, resend_pin_login, signup_pin_register;

    //For Verification
    EditText phone_pin;
    Button resend_pin, signup_pin;
    TextView view_pin2;
    ImageView imageView_Login;


    static ProgressBar progressBar;
    Button learnMore;
    Context context;

    static View viewToHide_Register, viewToHide_Login, viewToHide_pin, relativeView20loginsignup;

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
    public static Customers customers1;

    private String password_secured;

    boolean doesUserExist;
    private DocumentReference docRef;
    SessionManagement sessionManagement;

    private List<State> stateList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        /*if (user != null) {
            // User is signed in
            //FirebaseAuth.getInstance().signOut();
            nextActivity(context, HomeScreen.class, "", "", true);//goto /logIn Page
        }*/
        super.onCreate(savedInstanceState);


        customers1 = new Customers();
        stateList = new ArrayList<>();

        setContentView(R.layout.activity_signup_gotozeal);
        if (!checkPermissions()) {
            requestPermissions();
        }

        sessionManagement = new SessionManagement(context);

        //initializing objects
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        doesUserExist = false;

        //start = 0;
        start = 3;
        status_progress = false;

        password_Login = findViewById(R.id.password_Login);
        imageView_Login = findViewById(R.id.imageView_Login);
        imageView_Login.setVisibility(View.GONE);
        phone_Login = findViewById(R.id.phone_Login);
        //terms_Login = findViewById(R.id.terms_Login);
        signup_Login = findViewById(R.id.signup_Login);
        signup_Login.setOnClickListener(this);
        login_login = findViewById(R.id.login_login);
        login_login.setOnClickListener(this);
        resend_pin_login = findViewById(R.id.resend_pin_login);
        resend_pin_login.setOnClickListener(this);
        signup_pin_register = findViewById(R.id.signup_pin_register);
        signup_pin_register.setOnClickListener(this);
        forgetpassword_Login = findViewById(R.id.forgetpassword_Login);
        forgetpassword_Login.setOnClickListener(this);
        forgetpassword_Login.setVisibility(View.GONE);

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
        phone = findViewById(R.id.phone);
        surname = findViewById(R.id.surname);
        othernames = findViewById(R.id.othernames);
        address = findViewById(R.id.address);
        switchUser = findViewById(R.id.switchUser);
        password = findViewById(R.id.password);
        state = findViewById(R.id.city);
        state.setOnItemSelectedListener(this);

        //stateMethodList(db);//fetch states Available

        terms = findViewById(R.id.terms);
        terms.setVisibility(View.GONE);
        signup = findViewById(R.id.signup);
        signup.setOnClickListener(this);
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
        learnMore = findViewById(R.id.learnMore);
        learnMore.setVisibility(View.GONE);
        learnMore.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        viewToHide_Register = findViewById(R.id.viewToHide_Register);
        viewToHide_Login = findViewById(R.id.viewToHide_Login);
        viewToHide_pin = findViewById(R.id.viewToHide_pin);
        relativeView20loginsignup = findViewById(R.id.relativeView20loginsignup);
        hideItems(status_progress, start);//progressBar view is not needed at first


        productDatabase ud = productDatabase.getAppDatabase(context);
        if (ud.productDAO().getAllProduct() == null || ud.productDAO().getAllProduct().size() <= 0) {
            //create new DaTa
            Log.i("TAG_NEW", "Creating New Products");
            for (Product product : getAllProducts()) {
                ud.productDAO().insert(product);
            }
        }
    }

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
                /*viewToHide_Login.setVisibility(View.GONE);
                viewToHide_Register.setVisibility(View.GONE);
                viewToHide_pin.setVisibility(status_progress ? View.GONE : View.VISIBLE);
                break;*/
            case 3:
                viewToHide_Login.setVisibility(View.GONE);
                viewToHide_Register.setVisibility(View.GONE);
                break;
        }


    }

    @Override
    public void onBackPressed() {
        if (start == 0) {//only close app when on the firstView i.e LoginView
            super.onBackPressed();
        } else {
            start--;
            status_progress = false;
            hideItems(status_progress, start);
        }
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
                String first_name_S = surname.getText().toString();
                String last_name_S = othernames.getText().toString();
                String address_S = address.getText().toString();
                final String username_S = phone.getText().toString();
                String password_S = password.getText().toString();//random(50);
                String email_S = getString(R.string.no_email_set, username_S);

                /*
                if (email_S.isEmpty()){
                    email.setError(getString(R.string.empty_error_message));
                    email.setFocusable(true);
                    return;
                }
                */

                if (username_S.isEmpty()) {
                    phone.setError(getString(R.string.empty_error_message));
                    phone.setFocusable(true);
                    return;
                }
                if (address_S.isEmpty()) {
                    address.setError(getString(R.string.empty_error_message));
                    address.setFocusable(true);
                    return;
                }
                if (!TextUtils.isDigitsOnly(username_S) || username_S.length() < 11) {
                    phone.setError(getString(R.string.mobile_error_message));
                    phone.setFocusable(true);
                    return;
                }
                if (first_name_S.isEmpty()) {
                    surname.setError(getString(R.string.empty_error_message));
                    surname.setFocusable(true);
                    return;
                }
                if (first_name_S.length() < 3) {
                    surname.setError(getString(R.string.names_error_message, getString(R.string._name)));
                    surname.setFocusable(true);
                    return;
                }
                if (last_name_S.isEmpty()) {
                    othernames.setError(getString(R.string.empty_error_message));
                    othernames.setFocusable(true);
                    return;
                }
                if (last_name_S.length() < 3) {
                    othernames.setError(getString(R.string.names_error_message, getString(R.string._password)));
                    othernames.setFocusable(true);
                    return;
                }

                if (password_S.isEmpty()) {
                    password.setError(getString(R.string.empty_error_message));
                    password.setFocusable(true);
                    return;
                }
                if (password_S.length() < 3) {
                    password.setError(getString(R.string.names_error_message, getString(R.string._password)));
                    password.setFocusable(true);
                    return;
                }

                //convert password to secured for fireStore
                password_secured = md5(password_S);

                state_S = "Abeokuta";
                /*if (state_S.isEmpty()) {
                    state.setFocusable(true);
                    return;
                }*/

                if (!terms.isChecked()) {
                    //Toast.makeText(context, getString(R.string.no_terms), Toast.LENGTH_SHORT).show();
                    Toasty.warning(context, getString(R.string.no_terms), Toast.LENGTH_SHORT, true).show();
                    terms.setError(getString(R.string.no_terms));
                    terms.setFocusable(true);
                    return;
                }


                final Customers customers = new Customers(email_S, first_name_S, last_name_S, username_S, password_S, address_S, switchUser.isChecked());

                Timber.d(customers.toString());
                start = 1;
                status_progress = true;//show progressBar
                hideItems(status_progress, start);
                //Toast.makeText(context, getString(R.string.network_loading_message), Toast.LENGTH_LONG).show();
                //Toasty.info(context, getString(R.string.network_loading_message), Toast.LENGTH_LONG, true).show();
                // for signup
                //Create a handler for the RetrofitInstance interface//

                status_progress = false;//hide progressBar
                new AddUser(context, customers).execute();
              /*
                GotoZealService service = RetrofitClient.getRetrofitInstance(getString(R.string.BASE_URL), getString(R.string.c_username), getString(R.string.c_password)).create(GotoZealService.class);

                Call<Customers> call = service.createUser(customers);

                //Execute the request asynchronously//

                call.enqueue(new Callback<Customers>() {
                    @Override
                    //Handle a successful response//
                    public void onResponse(Call<Customers> call, Response<Customers> response) {
                        int status_progressCode = response.code();
                        customers1 = response.body();

                        Timber.i(response.toString());
                        if (status_progressCode != STATUS_CREATED_GOTOZEAL) {
                            //Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show();
                            Toasty.error(context, response.message(), Toast.LENGTH_LONG, true).show();
                        } else {

                            customers1.setPassword(password_secured);
                            db.collection(getString(R.string.customers_table)).document(customers1.getUsername())
                                    .set(customers1)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            emptyValues(1);
                                            //Toast.makeText(context, getString(R.string.register_success_message), Toast.LENGTH_LONG).show();
                                            Toasty.success(context, getString(R.string.register_success_message), Toast.LENGTH_LONG, true).show();
                                            phone_Login.setText(customers1.getUsername());
                                            password_Login.requestFocus();
                                            start = 0;
                                            status_progress = false;//show progressBar
                                            hideItems(status_progress, start);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //Toast.makeText(context, getString(R.string.register_success_message), Toast.LENGTH_LONG).show();
                                            //Alert Admin accountwas created but info now on FireStore table
                                            Timber.e(e);
                                        }
                                    });


                            //nextActivity(context, LoginActivity.class, "phone", username_S);//goto /logIn Page
                        }
                    }

                    @Override
                    //Handle execution failures//
                    public void onFailure(Call<Customers> call, Throwable throwable) {
                        //If the request fails, then display the following toast//
                        //Toast.makeText(context, getString(R.string.network_error_message), Toast.LENGTH_SHORT).show();
                        Toasty.warning(context, getString(R.string.network_error_message), Toast.LENGTH_SHORT, true).show();
                        Timber.e(throwable);
                        //Timber.i(call.request().toString());
                        status_progress = false;
                        hideItems(status_progress, start);
                    }
                });
                */
                break;
            case R.id.signup_pin_register:
                //go Back to Login
                //nextActivity(context, MainActivity.class, "", "");
                start = 1;
                hideItems(status_progress, start);
                relativeView20loginsignup.setVisibility(View.GONE);
                break;

            case R.id.login:
            case R.id.login_login:
                start = 3;
                hideItems(status_progress, start);
                relativeView20loginsignup.setVisibility(View.VISIBLE);
                break;
            case R.id.resend_pin_login:
                //register user
                start = 0;
                hideItems(status_progress, start);
                relativeView20loginsignup.setVisibility(View.GONE);
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
                sendVerificationCode(customers1.getUsername());
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
                                            customers1 = documentSnapshot.toObject(Customers.class);
                                            password_secured = md5(password_Login_1);
                                            if (customers1.getPassword().equals(password_secured)) {
                                                start = 2;
                                                hideItems(status_progress, start);
                                                sendVerificationCode(customers1.getUsername());
                                                customers1.setPassword(null);//to porevent sniffing of password
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
            phone.setText("");
            surname.setText("");
            othernames.setText("");
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
                .addOnCompleteListener(Signup_gotozeal.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sessionManagement.createLoginSession(customers1.getUsername());
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
                                    sendVerificationCode(customers1.getUsername());
                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }


    static class AddUser extends AsyncTask<Void, Void, Void> {
        private Customers u;
        private WeakReference<Context> c;

        public AddUser(Context c, Customers u) {
            this.c = new WeakReference<>(c);
            this.u = u;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            customersDatabase ud = customersDatabase.getAppDatabase(c.get());
            ud.customersDAO().insert(u);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(c.get(), u.isIs_paying_customer() ? "Hurray! you can view and add new location to MAPOLY Tour" : "You are now a member of MAPOLY Tour", Toast.LENGTH_SHORT).show();
            //((Activity) c.get()).finish();
            start = 0;
            status_progress = false;//show progressBar
            hideItems(status_progress, start);
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
            customers1 = new Customers();
            customersDatabase ud = customersDatabase.getAppDatabase(c.get());
            List<Customers> customersList = ud.customersDAO().getAllUsers();
            for (Customers customers : customersList) {
                Log.i("CHECK", customers.toString());
            }
            customers1 = ud.customersDAO().LoginUsers(u.getUsername(), u.getPassword());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (customers1 != null) {
                customers1.setPassword(null);//to porevent sniffing of password
                //verification successful we will start the profile activity
                if (customers1.isIs_paying_customer()) {
                    //Toast.makeText(context, "Logging to Decoration Dashboard", Toast.LENGTH_SHORT).show();
                    nextActivity(context, FashionMainActivity.class, "", "", true);
                } else {
                    //Toast.makeText(context, "Logging to Mem Dashboard", Toast.LENGTH_SHORT).show();
                    nextActivity(context, HomeScreen.class, "", "", true);
                }
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

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    int PERMISSION_ID = 44;

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }
}
