package com.gaspercloud.gotozeal.entitiesDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.gaspercloud.gotozeal.interfaceDao.productDAO;
import com.gaspercloud.gotozeal.model.Product;

@Database(entities = {Product.class}, version = 1, exportSchema = false)
public abstract class productDatabase extends RoomDatabase {
    public abstract productDAO productDAO();

    private static productDatabase INSTANCE;

    public static productDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), productDatabase.class, "Product")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
