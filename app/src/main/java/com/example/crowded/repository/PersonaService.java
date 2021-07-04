package com.example.crowded.repository;

import com.example.crowded.dto.PersonaDto;

import java.util.List;

public interface PersonaService {

    List<PersonaDto> getAll();

    PersonaDto getPersonaById(int id);

    int getLastInsertedIdPersona();

    void insertOrUpdatePersona(PersonaDto persona);

    void deleteAllPersona();
}
