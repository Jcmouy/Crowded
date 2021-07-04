package com.example.crowded.database.entity;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Usuario",
        foreignKeys = {@ForeignKey(entity = Persona.class,
                parentColumns = "id",
                childColumns = "persona_id")})
public class Usuario implements Serializable {

    @PrimaryKey
    @NonNull
    public int id;

    @ColumnInfo(name = "persona_id")
    public String personaId;

    @ColumnInfo(name = "imagenPath")
    public String imagenPath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPersonaId() {
        return personaId;
    }

    public void setPersonaId(@NonNull String personaId) {
        this.personaId = personaId;
    }

    public String getImagenPath() {
        return imagenPath;
    }

    public void setImagenPath(@NonNull String imagenPath) {
        this.imagenPath = imagenPath;
    }
}
