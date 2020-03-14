package com.bhex.wallet.common.db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.bhex.wallet.common.db.dao.BHWalletDao;
import com.bhex.wallet.common.db.entity.BHWalletExt;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/4
 * Time: 22:22
 */
@Database(entities = {BHWalletExt.class} , version = 1)
public abstract  class AppDataBase extends RoomDatabase {

    private static final String DB_NAME ="bh_db";


    private static volatile AppDataBase INSTANCE;

    public abstract BHWalletDao bhWalletDao();

    public static AppDataBase getInstance(Application context){
        if(INSTANCE==null){
            synchronized (AppDataBase.class){
                if(INSTANCE==null){
                    INSTANCE = buildDatabase(context);
                }
            }
        }
        return INSTANCE;
    }

    private static AppDataBase buildDatabase(Application context) {
        return Room.databaseBuilder(context,AppDataBase.class,DB_NAME).addCallback(new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
            }

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        }).allowMainThreadQueries().build();
    }


}
