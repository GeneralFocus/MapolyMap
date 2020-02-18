package com.gaspercloud.gotozeal.ui.home;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gaspercloud.gotozeal.R;
import com.gaspercloud.gotozeal.entitiesDB.orderModelDatabase;
import com.gaspercloud.gotozeal.model.CartModelOLD;
import com.gaspercloud.gotozeal.model.OrderModel;
import com.gaspercloud.gotozeal.model.ProductCategories;
import com.gaspercloud.gotozeal.restService.RetrofitClient;
import com.gaspercloud.gotozeal.restService.restInterface.GotoZealService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Integer> parentID;
    private MutableLiveData<List<ProductCategories>> productCategoriesMutableLiveData;
    private MutableLiveData<List<CartModelOLD>> mutableLiveCartModel;
    private MutableLiveData<List<OrderModel>> mutableLiveOrderModel;
    private MutableLiveData<Float> totalcartcost;

    /*
    setValue() method must be called from the main thread.
    But if you need set a value from a background thread, postValue() should be used.
     */

    public HomeViewModel() {
        parentID = new MutableLiveData<>();
        //parentID.setValue(0);
        mText = new MutableLiveData<>();
        productCategoriesMutableLiveData = new MutableLiveData<>();
        mutableLiveCartModel = new MutableLiveData<>();
        mutableLiveOrderModel = new MutableLiveData<>();
        totalcartcost = new MutableLiveData<>();
        //mText.setValue("Loading ...");
    }
/*public LiveData<Integer> getParentID() {
        return parentID;
    }*/

    public MutableLiveData<Float> getTotalcartcost() {
        return totalcartcost;
    }

    public MutableLiveData<Integer> getParentID() {
        return parentID;
    }

    public MutableLiveData<List<CartModelOLD>> getMutableLiveCartModel() {
        return mutableLiveCartModel;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<ProductCategories>> get_forProductCategories(int parentID, Context context, boolean hide_empty, List<Integer> exclude) {
        return getMutableLiveData_forProductCategories(parentID, context, hide_empty, exclude);
    }

    private MutableLiveData<List<ProductCategories>> getMutableLiveData_forProductCategories(int parentID, Context context, boolean hide_empty, List<Integer> exclude) {

        GotoZealService service = RetrofitClient.getRetrofitInstance(context.getString(R.string.BASE_URL), context.getString(R.string.c_username), context.getString(R.string.c_password)).create(GotoZealService.class);

        Call<List<ProductCategories>> call = service.ProductCategories(parentID, hide_empty, exclude);

        call.enqueue(new Callback<List<ProductCategories>>() {
            @Override
            public void onResponse(Call<List<ProductCategories>> call, Response<List<ProductCategories>> response) {
                List<ProductCategories> productList = response.body();
                if (productList != null) {
                    productCategoriesMutableLiveData.setValue(productList);
                }
            }

            @Override
            public void onFailure(Call<List<ProductCategories>> call, Throwable t) {
                Timber.e(t);
            }
        });


        return productCategoriesMutableLiveData;
    }

    public LiveData<List<OrderModel>> listOrders(Integer customer, Context context, Integer per_page) {
        return getlistOrders(customer, context, per_page);
    }

    private MutableLiveData<List<OrderModel>> getlistOrders(Integer customer, Context context, Integer per_page) {

      /*  GotoZealService service = RetrofitClient.getRetrofitInstance(context.getString(R.string.BASE_URL), context.getString(R.string.c_username), context.getString(R.string.c_password)).create(GotoZealService.class);

        Call<List<OrderModel>> call = service.GetOrders(customer, per_page);

        call.enqueue(new Callback<List<OrderModel>>() {
            @Override
            public void onResponse(Call<List<OrderModel>> call, Response<List<OrderModel>> response) {
                List<OrderModel> orderList = response.body();
                if (orderList != null) {
                    mutableLiveOrderModel.setValue(orderList);
                }
            }

            @Override
            public void onFailure(Call<List<OrderModel>> call, Throwable t) {
                Timber.e(t);
            }
        });*/

        orderModelDatabase ud = orderModelDatabase.getAppDatabase(context);
        ArrayList<OrderModel> orderModels = new ArrayList<>();
        for (OrderModel orderModel : ud.orderModelDAO().getAllUsers()) {
            orderModels.add(orderModel);
            Log.i("CHECK", orderModels.toString());
        }
        mutableLiveOrderModel.setValue(orderModels);

        return mutableLiveOrderModel;
    }
}