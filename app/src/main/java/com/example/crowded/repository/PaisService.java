package com.example.crowded.repository;

import com.example.crowded.dto.PaisDto;

import java.util.List;

public interface PaisService {

    List<PaisDto> getAll();

    PaisDto getPaisById(int id);

    void insertOrUpdatePais(PaisDto pais);

    int getLastInsertedIdPais();

    void deleteAllPais();
}
