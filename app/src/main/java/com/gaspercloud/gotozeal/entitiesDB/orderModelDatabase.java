package com.gaspercloud.gotozeal.entitiesDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.gaspercloud.gotozeal.interfaceDao.orderModelDAO;
import com.gaspercloud.gotozeal.model.OrderModel;

@Database(entities = {OrderModel.class}, version = 1, exportSchema = false)
public abstract class orderModelDatabase extends RoomDatabase {
    public abstract orderModelDAO orderModelDAO();

    private static orderModelDatabase INSTANCE;

    public static orderModelDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), orderModelDatabase.class, "OrderModel")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
