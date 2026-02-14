package com.aipasa.data_Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Mascota.class}, version = 1)
public abstract class MascotaDatabase extends RoomDatabase {

    private static MascotaDatabase INSTANCE;

    public abstract MascotaDAO mascotaDAO();

    public static MascotaDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    MascotaDatabase.class,
                    "mascota_db"
            ).build();
        }
        return INSTANCE;
    }
}

