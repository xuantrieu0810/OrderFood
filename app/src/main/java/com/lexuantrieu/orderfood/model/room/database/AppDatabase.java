package com.lexuantrieu.orderfood.model.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lexuantrieu.orderfood.model.room.User;
import com.lexuantrieu.orderfood.model.room.dao.UserDao;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase appDatabase = null;
    private static Context context;
    private static String DB_NAME ="dborderfood";

    public abstract UserDao getUserDao();
    public static AppDatabase getInstance(Context context){
        AppDatabase.context = context;
        if(appDatabase==null){
            init();
        }
        return appDatabase;
    }

    private static void init() {
        appDatabase= Room.databaseBuilder(context,AppDatabase.class,DB_NAME)
                .allowMainThreadQueries()
//                .addMigrations(Migration_1_to_3)
                .build();

    }
/*
    private static Migration Migration_1_to_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };
    private static Migration Migration_2_to_1 = new Migration(2,1) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };
    private static Migration Migration_2_to_3=new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE note "
                    + " ADD COLUMN ableToDelete INTEGER");
        }
    };
    private static Migration Migration_1_to_3=new Migration(1,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE note "
                    + " ADD COLUMN ableToDelete INTEGER NOT NULL DEFAULT 0");
        }
    };

 */
}
