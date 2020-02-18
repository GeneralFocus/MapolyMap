package com.gaspercloud.gotozeal;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import co.paystack.android.PaystackSdk;
import timber.log.Timber;

public class ApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Open Logging when in Debug Mode
        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());

        //Initialize PayStack SDK
        PaystackSdk.initialize(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
