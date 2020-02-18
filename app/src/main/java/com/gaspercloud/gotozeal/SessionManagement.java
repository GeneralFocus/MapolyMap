package com.gaspercloud.gotozeal;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.firebase.auth.FirebaseAuth;

import static com.gaspercloud.gotozeal.Constants.EMPTY;
import static com.gaspercloud.gotozeal.Constants.nextActivity;

public class SessionManagement {


    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;


    // First time logic Check
    public static final String FIRST_TIME = "firsttime";

    // Sharedpref file name
    private static final String PREF_NAME = "GotoZealPref";

    // User name (make variable public to access from outside)
    private static final String KEY_USERNAME = "username";

    // number of items in our cart
    //private static final String KEY_CART_TOTAL = "cartvalue_total";

    // check first time app launch
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    // Constructor
    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String username) {
        // Storing username in pref
        editor.putString(KEY_USERNAME, username);
        // commit changes
        editor.commit();
    }


    /**
     * Get stored session data
     */
    public String getUsername() {
        // return user
        return pref.getString(KEY_USERNAME, EMPTY);
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        //logOut from FireBase
        FirebaseAuth.getInstance().signOut();

        nextActivity(_context, Signup_gotozeal.class, "", "", true);//goto /logIn Page
    }

/*
    public float getCartValue() {
        return pref.getFloat(KEY_CART_TOTAL, 0);
    }*/

    public Boolean getFirstTime() {
        return pref.getBoolean(FIRST_TIME, true);
    }

    public void setFirstTime(Boolean n) {
        editor.putBoolean(FIRST_TIME, n);
        editor.commit();
    }

/*
    public void increaseCartValue(float amount) {
        //float val = getCartValue() + amount;
        editor.putFloat(KEY_CART_TOTAL, amount);
        editor.commit();
        Timber.e("Cart Value PE  Var value : " + amount + " Cart Value :" + getCartValue() + " ");
    }

    public void decreaseCartValue(float amount) {
        float val = getCartValue() - amount;
        editor.putFloat(KEY_CART_TOTAL, val);
        editor.commit();
        Timber.e("Cart Value PE Var value : " + val + "Cart Value :" + getCartValue() + " ");
    }


    public void setCartValue(float count) {
        editor.putFloat(KEY_CART_TOTAL, count);
        editor.commit();
    }*/

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}

