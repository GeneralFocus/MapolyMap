package com.gaspercloud.gotozeal.interfaceDao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.gaspercloud.gotozeal.model.OrderModel;
import com.gaspercloud.gotozeal.model.Product;

import java.util.List;

@Dao
public interface orderModelDAO {

    @Insert
    Long insert(OrderModel u);

    @Query("SELECT * FROM 'OrderModel' ORDER BY 'id' ASC")
    List<OrderModel> getAllUsers();

    @Query("SELECT * FROM 'OrderModel' WHERE 'customer_id' =:customer_id ORDER BY 'id' DESC")
    List<OrderModel> getAllUsers(int customer_id);

    @Query("SELECT * FROM 'OrderModel' WHERE 'id' =:id")
    OrderModel getOrderModel(int id);

    @Query("SELECT * FROM 'OrderModel' WHERE 'customer_id' =:customer_id")
    OrderModel getCustomerOrderModel(int customer_id);

    @Update
    void update(OrderModel u);

    @Delete
    void delete(OrderModel u);

}
