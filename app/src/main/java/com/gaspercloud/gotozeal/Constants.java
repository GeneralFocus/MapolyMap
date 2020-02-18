package com.gaspercloud.gotozeal;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import com.gaspercloud.gotozeal.model.Product;
import com.gaspercloud.gotozeal.model.ProductOLD;
import com.google.firebase.firestore.Source;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class Constants {

    // Source can be CACHE, SERVER, or DEFAULT.
    public static Source source = Source.CACHE;

    public static String COUNTRY_CODE_NG = "+234";
    public static String EMPTY = "";


    public static int STATUS_OK_GOTOZEAL = 200;
    public static int STATUS_CREATED_GOTOZEAL = 201;

    public static void nextActivity(Context context, Class aClass, String key, String value, boolean newTask) {
        Intent intent = new Intent(context, aClass);
        if (newTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        intent.putExtra(key, value);
        context.startActivity(intent);
    }

    public static void nextActivity(Context context, Class aClass, String key, ProductOLD value) {
        Intent intent = new Intent(context, aClass);
        if (value != null) {
            intent.putExtra(key, value);
        }
        context.startActivity(intent);
    }

    public static void nextActivityProduct(Context context, Class aClass, String key, Product value) {
        Intent intent = new Intent(context, aClass);
        if (value != null) {
            intent.putExtra(key, value);
        }
        context.startActivity(intent);
    }

    public static String random(int MAX_LENGTH) {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public static void countDown(int millinInFuture, int countDownInterval, final TextView view, final Context context, final Button enableButton) {
        new CountDownTimer(millinInFuture, countDownInterval) {

            public void onTick(long millisUntilFinished) {
                enableButton.setEnabled(false);
                enableButton.setBackgroundColor(context.getResources().getColor(R.color.editTextBG));
                view.setText(context.getString(R.string.sms_prompt_message, millisUntilFinished / 1000));
            }

            public void onFinish() {
                enableButton.setEnabled(true);
                enableButton.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                view.setText(context.getString(R.string.resend_sms_prompt_message));
            }
        }.start();
    }

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Date getDateFromString(String datetoSaved) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            return format.parse(datetoSaved);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getDateFromString(Date datetoSaved) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return format.format(datetoSaved);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date get30daysDateInterval() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, +30);//add 30 days
        return calendar.getTime();
    }

    public static int calculatePercentage(float obtained, float total) {
        return (int) (obtained * 100 / total);
    }

    public static String formatnumbers(Object amount) {
        //DecimalFormat formatter = new DecimalFormat("#,###.00");
        //return formatter.format(amount);

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "NG"));
        /*
        // Format currency for Germany locale in German locale,
        // the decimal point symbol is a dot and currency symbol
        // is €.
        format = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        currency = format.format(number);
        System.out.println("Currency in Germany: " + currency);
        */
        return format.format(amount);
    }

    public static String createTransactionID() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
}
