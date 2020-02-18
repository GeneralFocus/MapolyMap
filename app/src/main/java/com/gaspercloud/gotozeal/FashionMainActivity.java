package com.gaspercloud.gotozeal;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gaspercloud.gotozeal.adapter.List_Product_Data_Adapter;
import com.gaspercloud.gotozeal.entitiesDB.productDatabase;
import com.gaspercloud.gotozeal.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class FashionMainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private static Context context;

    private static List_Product_Data_Adapter data_productAdapter;
    private static List<Product> data_productList;
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_fashion_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Do you want to add a new Location?", Snackbar.LENGTH_LONG)
                        .setAction("Add Now", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(FashionMainActivity.this, FashionAdd_gotozeal.class);
                                startActivity(intent);
                            }
                        }).show();
            }
        });

        data_productList = new ArrayList<>();
        data_productAdapter = new List_Product_Data_Adapter(context, data_productList, this, null, false, null);

        rv = findViewById(R.id.rv);
        RecyclerView.LayoutManager mLayoutManager_data = new LinearLayoutManager(context);
        rv.setLayoutManager(mLayoutManager_data);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        rv.setAdapter(data_productAdapter);

        Toast.makeText(context, "Loading all locations", Toast.LENGTH_LONG).show();
        new ViewProducts(context).execute();
        //initScrollListener();


       /* orderModelDatabase ud = orderModelDatabase.getAppDatabase(context);
        ArrayList<OrderModel> orderModels = new ArrayList<>();
        for (OrderModel orderModel : ud.orderModelDAO().getAllUsers()) {
            orderModels.add(orderModel);
            if (orderModel.getParent_id() == Signup_gotozeal.customers1.getId() && !orderModel.getCustomer_note().isEmpty()) {
                Snackbar.make(toolbar, "You have new Feedbacks.", Snackbar.LENGTH_LONG)
                        .setAction("View Order", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(FashionMainActivity.this, Orders.class).putExtra("status", true));

                            }
                        })
                        .show();
                break;
            }
            Log.i("CHECK", orderModels.toString());
        }*/
    }


    public void LogOutView(View view) {
        startActivity(new Intent(FashionMainActivity.this, Signup_gotozeal.class));
    }

    private void prepareProductData(Context context, final Integer offset, Integer per_page) {
        //home_swiperefresh.setRefreshing(true);
        //rv.setVisibility(View.GONE);
        //progressBar.setVisibility(View.VISIBLE);
        //textView.setVisibility(View.VISIBLE);

        rv.setVisibility(View.VISIBLE);
        data_productList.clear();
        productDatabase ud = productDatabase.getAppDatabase(context);

        data_productList.addAll(ud.productDAO().whereGetAllProduct(Signup_gotozeal.customers1.getId()));

        data_productAdapter.notifyDataSetChanged();
        /*if (offset != null) {
            rv.scrollToPosition(offset + 1);
        }
        //home_swiperefresh.setRefreshing(false);
        //progressBar.setVisibility(View.GONE);
        //textView.setVisibility(View.GONE);
        // Stopping Shimmer Effect's animation after data is loaded to ListView
        parentShimmerLayout_store_list.stopShimmer();
        parentShimmerLayout_store_list.setVisibility(View.GONE);
        isLoading = false;*/

    }

    private void initScrollListener() {
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == data_productList.size() - 1) {
                        //bottom of list!
                        //loadMore();
                        data_productList.add(null);
                        data_productAdapter.notifyItemInserted(data_productList.size() - 1);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    data_productList.remove(data_productList.size() - 1);
                                } catch (Exception e) {
                                    Timber.e(e);
                                }
                                int scrollPosition = data_productList.size();
                                data_productAdapter.notifyItemRemoved(scrollPosition);
                                int offset = scrollPosition;
                                int per_page = offset + 10;
                                //prepareProductData(HomeFragmentOLD.parentID, context, offset, per_page);
                            }
                        }, 2000);

                        isLoading = true;
                    }
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(context, "Loading all locations", Toast.LENGTH_LONG).show();
        new ViewProducts(context).execute();
        //prepareProductData(context, null, null);
    }

    public void viewCart(View view) {
//        nextActivity(context, Cart.class, "", null);
        //nextActivity(context, Orders.class, "status", "false", false);
        //startActivity(new Intent(FashionMainActivity.this, Orders.class).putExtra("status", true));
        startActivity(new Intent(FashionMainActivity.this, HomeScreen.class));
    }

    static class ViewProducts extends AsyncTask<Void, Void, Void> {
        private WeakReference<Context> c;
        productDatabase ud;

        public ViewProducts(Context c) {
            this.c = new WeakReference<>(c);
            Log.i("Login_GotoZeal_Product", "Login_GotoZeal_Product");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //prepareProductData(context, null, null);
            //rv.setVisibility(View.VISIBLE);
            data_productList.clear();
            ud = productDatabase.getAppDatabase(context);

            //data_productList.addAll(ud.productDAO().whereGetAllProduct(Signup_gotozeal.customers1.getId()));
            //data_productList.addAll(ud.productDAO().getAllProduct());
            for (Product product : ud.productDAO().getAllProduct()) {
                //Log.i("getAllProduct: ", product.toString());
                //if (product.getParent_id() == Signup_gotozeal.customers1.getId()) {
                data_productList.add(product);
                //}
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            data_productAdapter.notifyDataSetChanged();
        }
    }
}