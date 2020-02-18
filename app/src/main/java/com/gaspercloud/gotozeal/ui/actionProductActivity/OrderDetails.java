package com.gaspercloud.gotozeal.ui.actionProductActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gaspercloud.gotozeal.HomeScreen;
import com.gaspercloud.gotozeal.R;
import com.gaspercloud.gotozeal.SessionManagement;
import com.gaspercloud.gotozeal.network.CheckInternetConnection;

import es.dmoral.toasty.Toasty;

import static com.gaspercloud.gotozeal.Constants.nextActivity;

public class OrderDetails extends AppCompatActivity {

    private Context context;
    private SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        context = this;


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.order_history));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
        session = new SessionManagement(context);
    }


    public void Notifications(View view) {
        Toasty.info(context, "Set Notifications here", Toasty.LENGTH_SHORT, true).show();
        /*startActivity(new Intent(IndividualProduct.this, NotificationActivity.class));
        finish();*/
    }

    public void storeHome(View view) {
        nextActivity(context, HomeScreen.class, "", "", true);
    }

    public void ThankYou(View view) {
        nextActivity(context, Orders.class, "", "", true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        nextActivity(context, Orders.class, "", "", true);
        //super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

    }
}
