package com.gaspercloud.gotozeal.ui.actionProductActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

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

public class WishList extends AppCompatActivity {


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


    private List_Product_Cart_AdapterOLD list_product_cart_adapter;
    private List<CartModelOLD> cartcollect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        context = this;

        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.customers_wishlist));

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
        list_product_cart_adapter = new List_Product_Cart_AdapterOLD(context, cartcollect, this, false);

        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(list_product_cart_adapter);

        homeViewModel.getMutableLiveCartModel().observe(this, new Observer<List<CartModelOLD>>() {
            @Override
            public void onChanged(List<CartModelOLD> cartModels) {
                cartcollect.clear();
                cartcollect.addAll(cartModels);
                if (cartModels.size() > 0) {
                    list_product_cart_adapter.notifyDataSetChanged();
                } else {
                    activitycartlist.setVisibility(View.GONE);
                    emptycart.setVisibility(View.VISIBLE);
                }
                tv_no_item.setVisibility(View.GONE);
                Timber.i("wishlist check b " + cartModels.size());
                Timber.i("wishlist check b " + cartModels.toString());
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
                .collection(context.getString(R.string.customers_wishlist))
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