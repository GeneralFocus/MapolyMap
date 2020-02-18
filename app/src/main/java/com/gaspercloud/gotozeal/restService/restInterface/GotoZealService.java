package com.gaspercloud.gotozeal.restService.restInterface;

import com.gaspercloud.gotozeal.model.Customers;
import com.gaspercloud.gotozeal.model.OrderModel;
import com.gaspercloud.gotozeal.model.OrderModelOLD;
import com.gaspercloud.gotozeal.model.Product;
import com.gaspercloud.gotozeal.model.ProductCategories;
import com.gaspercloud.gotozeal.model.ProductOLD;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GotoZealService {

    //Customers EndPoint
    @POST("/wp-json/wc/v3/customers")
    Call<Customers> createUser(@Body Customers user);//create a new customer

    //Products Categories EndPoint
    @GET("/wp-json/wc/v3/products/categories")
    Call<List<ProductCategories>> ProductCategories(@Query("parent") Integer parentId, @Query("hide_empty") boolean hide_empty, @Query("exclude") List<Integer> exclude);//to fetch parent (top level) categories

    //Products EndPoint
    @GET("/wp-json/wc/v3/products")
    Call<List<ProductOLD>> Products(@Query("category") int category, @Query("offset") Integer offset, @Query("per_page") Integer per_page);//to fetch parent (top level) categories

    //Orders EndPoint
    @POST("/wp-json/wc/v3/orders")
    Call<OrderModelOLD> createOrder(@Body OrderModelOLD order);

    @GET("/wp-json/wc/v3/orders")
    Call<List<OrderModel>> GetOrders(@Query("customer") int customer, @Query("per_page") Integer per_page);
}
