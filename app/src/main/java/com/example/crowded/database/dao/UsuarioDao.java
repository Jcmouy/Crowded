package com.example.crowded.database.dao;

import com.example.crowded.database.entity.Usuario;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UsuarioDao {

    @Query("SELECT * FROM USUARIO")
    boolean checkUsuarios();

    @Query("SELECT * FROM USUARIO ORDER BY ID")
    List<Usuario> loadAllUsuarios();

    @Query("SELECT ID FROM USUARIO ORDER BY ID DESC LIMIT 1")
    int getLastInsertedIdUsuario();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsuario(Usuario usuario);

    @Update
    void updateUsuario(Usuario usuario);

    @Delete
    void delete(Usuario usuario);

    @Query("DELETE FROM USUARIO")
    void deleteAll();

    @Query("SELECT * FROM USUARIO WHERE id = :id")
    Usuario loadUsuarioById(int id);

}
