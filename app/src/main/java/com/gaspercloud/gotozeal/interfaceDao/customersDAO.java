package com.gaspercloud.gotozeal.interfaceDao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.gaspercloud.gotozeal.model.Customers;

import java.util.List;

@Dao
public interface customersDAO {

    @Insert
    Long insert(Customers u);

    @Query("SELECT * FROM 'Customers' ORDER BY 'id' DESC")
    List<Customers> getAllUsers();

    @Query("SELECT * FROM 'Customers' WHERE 'id' =:id")
    Customers getCustomers(int id);

    @Query("SELECT * FROM Customers WHERE username =:username AND password =:password")
    Customers LoginUsers(String username, String password);

    @Update
    void update(Customers u);

    @Delete
    void delete(Customers u);

}
