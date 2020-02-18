package com.gaspercloud.gotozeal.ui.actionProductActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.gaspercloud.gotozeal.Constants;
import com.gaspercloud.gotozeal.FashionMainActivity;
import com.gaspercloud.gotozeal.HomeScreen;
import com.gaspercloud.gotozeal.R;
import com.gaspercloud.gotozeal.SessionManagement;
import com.gaspercloud.gotozeal.Signup_gotozeal;
import com.gaspercloud.gotozeal.adapter.List_Order_Adapter;
import com.gaspercloud.gotozeal.model.OrderModel;
import com.gaspercloud.gotozeal.network.CheckInternetConnection;
import com.gaspercloud.gotozeal.ui.home.HomeViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

import static com.gaspercloud.gotozeal.HomeScreen.customersHome;

public class Orders extends AppCompatActivity {

    //to get user session data
    private SessionManagement session;
    private RecyclerView order_recycler;

    //Getting reference to Firebase FireStore
    private FirebaseFirestore db;

    private LottieAnimationView tv_no_item;
    private RelativeLayout activityOrderlist;
    private LottieAnimationView emptycart;
    private ShimmerFrameLayout order_ShimmerFrameLayout;


    private Context context;


    private HomeViewModel homeViewModel;

    private List_Order_Adapter order_adapter;
    private List<OrderModel> orderModelList;
    boolean isLoading = false;
    private boolean _status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        context = this;

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


        order_ShimmerFrameLayout = findViewById(R.id.order_ShimmerFrameLayout);
        order_recycler = findViewById(R.id.order_recycler);
        tv_no_item = findViewById(R.id.tv_no_cards);
        activityOrderlist = findViewById(R.id.activityOrderlist);
        emptycart = findViewById(R.id.empty_cart);

        _status = getIntent().getBooleanExtra("status", false);
        orderModelList = new ArrayList<>();
        order_adapter = new List_Order_Adapter(context, orderModelList, this, _status);

        if (order_recycler != null) {
            //to enable optimization of recyclerview
            order_recycler.setHasFixedSize(true);
        }
        RecyclerView.LayoutManager mLayoutManager_data = new LinearLayoutManager(context);
        order_recycler.setLayoutManager(mLayoutManager_data);
        order_recycler.setItemAnimator(new DefaultItemAnimator());
        order_recycler.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        order_recycler.setAdapter(order_adapter);


        prepareOrderData(Signup_gotozeal.customers1.getId(), context, null, null, _status);
        //initScrollListener();

        permissionCALL();
    }

    private void permissionCALL() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                //Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
                if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                    //Toast.makeText(context, "3", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(context)
                            .setCancelable(false)
                            .setTitle("Call Permission")
                            .setMessage("Hi there! We can't call anyone without the call permission, could you please grant it?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CODE);
                                }
                            })
                            /*.setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(context, ":(", Toast.LENGTH_SHORT).show();
                                }
                            })*/
                            .show();
                } else {
                    //Toast.makeText(context, "4", Toast.LENGTH_SHORT).show();
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CODE);
                }
            } else {
                //Toast.makeText(context, "5", Toast.LENGTH_SHORT).show();
                // makeCall();
            }
        } else {
            //Toast.makeText(context, "6", Toast.LENGTH_SHORT).show();
            //makeCall();
        }
    }

    public void Notifications(View view) {
        Toasty.info(context, "Set Notifications here", Toasty.LENGTH_SHORT, true).show();
        /*startActivity(new Intent(IndividualProduct.this, NotificationActivity.class));
        finish();*/
    }

    public void storeHome(View view) {
        if (_status) {
            Constants.nextActivity(context, FashionMainActivity.class, "", "", true);
        } else {
            Constants.nextActivity(context, HomeScreen.class, "", "", true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        order_ShimmerFrameLayout.startShimmer();
    }

    @Override
    public void onStop() {
        super.onStop();
        order_ShimmerFrameLayout.stopShimmer();
    }


    private void prepareOrderData(Integer customer, Context context,
                                  final Integer offset, Integer per_page, final boolean status) {
        order_recycler.setVisibility(View.GONE);

        homeViewModel.listOrders(customer, context, per_page).observe(this, new Observer<List<OrderModel>>() {
            @Override
            public void onChanged(List<OrderModel> ordModels) {
                order_recycler.setVisibility(View.VISIBLE);
                orderModelList.clear();
                for (OrderModel model : ordModels) {
                    if (status) {//for designer
                        if (model.getNumber() == Signup_gotozeal.customers1.getId()) {
                            orderModelList.add(model);
                        }
                    } else {
                        if (model.getCustomer_id() == Signup_gotozeal.customers1.getId()) {
                            orderModelList.add(model);
                        }
                    }
                }
                //orderModelList.addAll(ordModels);
                if (ordModels.size() > 0) {
                    order_adapter.notifyDataSetChanged();
                    if (offset != null) {
                        order_recycler.scrollToPosition(offset + 1);
                    }
                } else {
                    activityOrderlist.setVisibility(View.GONE);
                    emptycart.setVisibility(View.VISIBLE);
                }
                tv_no_item.setVisibility(View.GONE);

                order_ShimmerFrameLayout.stopShimmer();
                order_ShimmerFrameLayout.setVisibility(View.GONE);
                isLoading = false;
            }
        });
    }

    private void initScrollListener() {
        order_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == orderModelList.size() - 1) {
                        //bottom of list!
                        //loadMore();
                        orderModelList.add(null);
                        order_adapter.notifyItemInserted(orderModelList.size() - 1);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    orderModelList.remove(orderModelList.size() - 1);
                                } catch (Exception e) {
                                    Timber.e(e);
                                }
                                int scrollPosition = orderModelList.size();
                                order_adapter.notifyItemRemoved(scrollPosition);
                                int offset = scrollPosition;
                                int per_page = offset + 10;
                                prepareOrderData(customersHome.getId(), context, offset, per_page, false);
                            }
                        }, 2000);

                        isLoading = true;
                    }
                }
            }
        });


    }

    private static final int PERMISSIONS_REQUEST_CODE = 11;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //makeCall();
            }
        }
    }

}
