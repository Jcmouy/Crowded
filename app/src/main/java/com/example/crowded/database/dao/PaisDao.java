package com.example.crowded.database.dao;

import com.example.crowded.database.entity.Pais;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PaisDao {

    @Query("SELECT * FROM PAIS")
    boolean checkPaises();

    @Query("SELECT * FROM PAIS ORDER BY ID")
    List<Pais> getAll();

    @Query("SELECT ID FROM PAIS ORDER BY ID DESC LIMIT 1")
    int getLastInsertedIdPais();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertPais(Pais pais);

    @Update
    void updatePais(Pais pais);

    @Delete
    void delete(Pais pais);

    @Query("DELETE FROM PAIS")
    void deleteAllPaises();

    @Query("SELECT * FROM PAIS WHERE id = :id")
    Pais findById(int id);

}
