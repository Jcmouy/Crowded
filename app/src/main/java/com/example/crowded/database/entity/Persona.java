package com.example.crowded.database.entity;

import com.example.crowded.database.converter.DateConverter;

import org.threeten.bp.LocalDate;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "Persona",
foreignKeys = {@ForeignKey(entity = Pais.class,
                parentColumns = "id",
                childColumns = "pais_id")})
public class Persona implements Serializable {

    @PrimaryKey
    @NonNull
    public int id;

    @ColumnInfo(name = "codigo")
    @NonNull
    public String codigo;

    @ColumnInfo(name = "nombres")
    @NonNull
    public String nombres;

    @ColumnInfo(name = "apellidos")
    @NonNull
    public String apellidos;

    @ColumnInfo(name = "fechaNacimiento")
    @NonNull
    @TypeConverters({DateConverter.class})
    public LocalDate fechaNacimiento;

    @ColumnInfo(name = "correoElectronico")
    @NonNull
    public String correoElectronico;

    @ColumnInfo(name = "telefono")
    @NonNull
    public String telefono;

    @ColumnInfo(name = "pais_id")
    @NonNull
    public int paisId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(@NonNull String codigo) {
        this.codigo = codigo;
    }

    @NonNull
    public String getNombres() {
        return nombres;
    }

    public void setNombres(@NonNull String nombres) {
        this.nombres = nombres;
    }

    @NonNull
    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(@NonNull String apellidos) {
        this.apellidos = apellidos;
    }

    @NonNull
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(@NonNull LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    @NonNull
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(@NonNull String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    @NonNull
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(@NonNull String telefono) {
        this.telefono = telefono;
    }

    @NonNull
    public int getPaisId() {
        return paisId;
    }

    public void setPaisId(@NonNull int paisId) {
        this.paisId = paisId;
    }
}
