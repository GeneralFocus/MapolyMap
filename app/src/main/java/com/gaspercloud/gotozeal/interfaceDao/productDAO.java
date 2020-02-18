package com.gaspercloud.gotozeal.interfaceDao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.gaspercloud.gotozeal.model.Product;

import java.util.List;

@Dao
public interface productDAO {

    @Insert
    Long insert(Product u);

    @Query("SELECT * FROM 'Product' ORDER BY 'id' DESC")
    List<Product> getAllProduct();


    @Query("SELECT * FROM Product GROUP BY slug")
    List<Product> getGroupProduct();

    @Query("SELECT * FROM 'Product' WHERE 'parent_id' =:parentID ORDER BY 'id' DESC")
    List<Product> whereGetAllProduct(int parentID);

    @Query("SELECT * FROM 'Product' WHERE 'id' =:id")
    Product getProduct(int id);

    @Update
    void update(Product u);

    @Delete
    void delete(Product u);

}
