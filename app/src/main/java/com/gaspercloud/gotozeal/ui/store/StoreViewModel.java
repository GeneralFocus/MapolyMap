package com.gaspercloud.gotozeal.ui.store;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gaspercloud.gotozeal.entitiesDB.productDatabase;
import com.gaspercloud.gotozeal.model.Product;

import java.util.ArrayList;
import java.util.List;

public class StoreViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<Product>> productData;
    private MutableLiveData<List<Product>> productDataNew;

    public StoreViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Store fragment");
        productData = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }


    public LiveData<List<Product>> get_ProductData(int category, Context context, Integer offset, Integer per_page) {
        return getMutableLiveData_ProductData(category, context, offset, per_page);
    }

    private MutableLiveData<List<Product>> getMutableLiveData_ProductData(int category, Context context, Integer offset, Integer per_page) {
/*
        GotoZealService service = RetrofitClient.getRetrofitInstance(context.getString(R.string.BASE_URL), context.getString(R.string.c_username), context.getString(R.string.c_password)).create(GotoZealService.class);

        Call<List<Product>> call = service.Products(category, offset, per_page);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> productList = response.body();
                if (productList != null) {
                    Timber.i("productList "+productList.toString());
                    productData.setValue(productList);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Timber.e(t);
            }
        });
*/
        if (category == -1) {
            productDatabase ud = productDatabase.getAppDatabase(context);
            productData.setValue(ud.productDAO().getGroupProduct());
        } else {
            productDatabase ud = productDatabase.getAppDatabase(context);
            productData.setValue(ud.productDAO().getAllProduct());
        }
        return productData;

    }
}