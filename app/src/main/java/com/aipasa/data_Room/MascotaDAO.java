package com.aipasa.data_Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
@Dao
public interface MascotaDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Mascota> mascotas);

    @Query("SELECT * FROM mascotas")
    List<Mascota> getAll();

    @Query("DELETE FROM mascotas")
    void deleteAll();
}
