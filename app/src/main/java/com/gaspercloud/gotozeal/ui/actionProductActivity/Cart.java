package com.gaspercloud.gotozeal.ui.actionProductActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.gaspercloud.gotozeal.Constants;
import com.gaspercloud.gotozeal.HomeScreen;
import com.gaspercloud.gotozeal.R;
import com.gaspercloud.gotozeal.SessionManagement;
import com.gaspercloud.gotozeal.adapter.List_Product_Cart_Adapter;
import com.gaspercloud.gotozeal.adapter.List_Product_Cart_AdapterOLD;
import com.gaspercloud.gotozeal.model.CartModel;
import com.gaspercloud.gotozeal.model.CartModelOLD;
import com.gaspercloud.gotozeal.network.CheckInternetConnection;
import com.gaspercloud.gotozeal.ui.home.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

import static com.gaspercloud.gotozeal.Constants.formatnumbers;
import static com.gaspercloud.gotozeal.Constants.nextActivity;

public class Cart extends AppCompatActivity {


    //to get user session data
    private SessionManagement session;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;

    //Getting reference to Firebase FireStore
    private FirebaseFirestore db;

    private LottieAnimationView tv_no_item;
    private LinearLayout activitycartlist;
    private LottieAnimationView emptycart;


    private Context context;
    private HomeViewModel homeViewModel;

    private TextView total_cart_value, text_action_bottom2;


    private List_Product_Cart_AdapterOLD list_product_cart_adapter;
    private List<CartModelOLD> cartcollect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        context = this;

        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.customers_cart));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
        session = new SessionManagement(context);

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);//to get SubCategories


        mRecyclerView = findViewById(R.id.recyclerview);
        tv_no_item = findViewById(R.id.tv_no_cards);
        activitycartlist = findViewById(R.id.activity_cart_list);
        emptycart = findViewById(R.id.empty_cart);
        cartcollect = new ArrayList<>();
        list_product_cart_adapter = new List_Product_Cart_AdapterOLD(context, cartcollect, this, true);

        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(list_product_cart_adapter);
        total_cart_value = findViewById(R.id.total_cart_value);
        text_action_bottom2 = findViewById(R.id.text_action_bottom2);
        text_action_bottom2.setVisibility(View.GONE);

        homeViewModel.getTotalcartcost().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float aFloat) {
                //String amount2decimalPlcaes = String.format(Locale.ENGLISH, "%.2f", aFloat);
                total_cart_value.setText(getString(R.string.cart_total_cost, getString(R.string.gotozeal_total), formatnumbers(aFloat)));
//                total_cart_value.setText(getString(R.string.cart_total_cost, getString(R.string.gotozeal_total), amount2decimalPlcaes));
                if (aFloat > 0) {
                    text_action_bottom2.setVisibility(View.VISIBLE);
                } else {
                    text_action_bottom2.setVisibility(View.GONE);
                }

            }
        });

        homeViewModel.getMutableLiveCartModel().observe(this, new Observer<List<CartModelOLD>>() {
            @Override
            public void onChanged(List<CartModelOLD> cartModels) {
                cartcollect.clear();
                cartcollect.addAll(cartModels);
                float totalCost = 0;
                for (CartModelOLD cartModel :
                        cartModels) {
                    totalCost += cartModel.getNo_of_items() * Float.parseFloat(cartModel.getProduct().getPrice());
                }
                homeViewModel.getTotalcartcost().setValue(totalCost);
                if (cartModels.size() > 0) {
                    list_product_cart_adapter.notifyDataSetChanged();
                } else {
                    activitycartlist.setVisibility(View.GONE);
                    emptycart.setVisibility(View.VISIBLE);
                }
                tv_no_item.setVisibility(View.GONE);
            }
        });


        //if (session.getCartValue() > 0) {
        //setValues Here
        List_getMutableLiveCartModel(db, context, session);
        /*} else {
            tv_no_item.setVisibility(View.GONE);
            activitycartlist.setVisibility(View.GONE);
            emptycart.setVisibility(View.VISIBLE);
        }*/

    }

    public void Notifications(View view) {
        Toasty.info(context, "Set Notifications here", Toasty.LENGTH_SHORT, true).show();
        /*startActivity(new Intent(IndividualProduct.this, NotificationActivity.class));
        finish();*/
    }

    public void storeHome(View view) {
        Constants.nextActivity(context, HomeScreen.class, "", "", true);
    }

    public void checkout(View view) {
        nextActivity(context, PaymentActivity.class, "cartvalue_total", homeViewModel.getTotalcartcost().getValue().toString(), false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

    }


    private void List_getMutableLiveCartModel(FirebaseFirestore db, Context context, SessionManagement sessionManagement) {
        db.collection(context.getString(R.string.customers_table)).document(sessionManagement.getUsername())
                .collection(context.getString(R.string.customers_cart))
                .get()

                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            homeViewModel.getMutableLiveCartModel().setValue(task.getResult().toObjects(CartModelOLD.class));
                        } else {
                            Timber.e(task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Timber.e(e);
            }
        });
    }

}
