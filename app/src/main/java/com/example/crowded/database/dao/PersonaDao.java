package com.example.crowded.database.dao;

import com.example.crowded.database.entity.Persona;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PersonaDao {

    @Query("SELECT * FROM PERSONA")
    boolean checkPersonas();

    @Query("SELECT * FROM PERSONA ORDER BY ID")
    List<Persona> loadAllPersonas();

    @Query("SELECT ID FROM PERSONA ORDER BY ID DESC LIMIT 1")
    int getLastInsertedIdPersona();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPersona(Persona persona);

    @Update
    void updatePersona(Persona persona);

    @Delete
    void delete(Persona persona);

    @Query("DELETE FROM PERSONA")
    void deleteAll();

    @Query("SELECT * FROM PERSONA WHERE id = :id")
    Persona loadPersonaById(int id);

}
