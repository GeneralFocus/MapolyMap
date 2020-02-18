package com.gaspercloud.gotozeal.entitiesDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.gaspercloud.gotozeal.interfaceDao.customersDAO;
import com.gaspercloud.gotozeal.model.Customers;

@Database(entities = {Customers.class}, version = 1, exportSchema = false)
public abstract class customersDatabase extends RoomDatabase {

    public abstract customersDAO customersDAO();

    private static customersDatabase INSTANCE;

    public static customersDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), customersDatabase.class, "Customers")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
