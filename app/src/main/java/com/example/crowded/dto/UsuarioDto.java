package com.example.crowded.dto;

import java.io.Serializable;

public class UsuarioDto implements Serializable {

    private int id;

    private PersonaDto persona;

    private String imagenPath;

    public UsuarioDto() {
    }

    public UsuarioDto(int id, PersonaDto persona, String imagenPath) {
        this.id = id;
        this.persona = persona;
        this.imagenPath = imagenPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PersonaDto getPersona() {
        return persona;
    }

    public void setPersona(PersonaDto persona) {
        this.persona = persona;
    }

    public String getImagenPath() {
        return imagenPath;
    }

    public void setImagenPath(String imagenPath) {
        this.imagenPath = imagenPath;
    }

    @Override
    public String toString() {
        return "UsuarioDto{" +
                "id=" + id +
                ", persona=" + persona +
                ", imagenPath='" + imagenPath + '\'' +
                '}';
    }
}
