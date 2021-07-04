package com.example.crowded.repository;

import com.example.crowded.dto.UsuarioDto;

import java.util.List;

public interface UsuarioService {

    List<UsuarioDto> getAll();

    UsuarioDto getUsuarioById(int id);

    int getLastInsertedIdUsuario();

    void insertOrUpdateUsuario(UsuarioDto usuario);

    void deleteAllUsuario();
}
