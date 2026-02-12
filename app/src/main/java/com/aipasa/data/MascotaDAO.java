package com.aipasa.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MascotaDAO {

    @Insert
    void insertar(Mascota mascota);

    @Query("SELECT * FROM mascotas ORDER BY fecha DESC")
    List<Mascota> obtenerTodas();

    @Query("SELECT * FROM mascotas WHERE estado = 'adopcion' ORDER BY fecha DESC")
    List<Mascota> obtenerAdopciones();

    @Query("SELECT * FROM mascotas WHERE estado = 'perdido' ORDER BY fecha DESC")
    List<Mascota> obtenerPerdidos();
}
