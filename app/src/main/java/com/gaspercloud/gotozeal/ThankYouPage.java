package com.gaspercloud.gotozeal;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.gaspercloud.gotozeal.ui.actionProductActivity.Orders;

import static com.gaspercloud.gotozeal.Constants.nextActivity;

public class ThankYouPage extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you_page);
        context = this;

        boolean payment_status = Boolean.parseBoolean(getIntent().getSerializableExtra("payment_status").toString());

        LottieAnimationView trans_message_anime_no_payment = findViewById(R.id.trans_message_anime_no_payment);
        LottieAnimationView trans_message_anime_payment = findViewById(R.id.trans_message_anime_payment);
        if (payment_status) {
            trans_message_anime_no_payment.setVisibility(View.GONE);
            trans_message_anime_payment.playAnimation();
            trans_message_anime_payment.loop(true);
        } else {
            trans_message_anime_payment.setVisibility(View.GONE);
            trans_message_anime_no_payment.playAnimation();
            trans_message_anime_no_payment.loop(true);
        }

    }


    public void ThankYou(View view) {
        nextActivity(context, Orders.class, "", "", true);
    }

}
