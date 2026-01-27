package com.aipasa.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Mascota.class}, version = 1)
public abstract class MascotaDatabase extends RoomDatabase {

    private static MascotaDatabase INSTANCE;

    public abstract MascotaDao mascotaDao();

    public static MascotaDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    MascotaDatabase.class,
                    "petfect_db"
            ).allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}
