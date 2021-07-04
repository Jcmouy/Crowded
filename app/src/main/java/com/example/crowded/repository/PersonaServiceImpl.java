package com.example.crowded.repository;

import com.example.crowded.converter.PersonaConverter;
import com.example.crowded.database.AppExecutors;
import com.example.crowded.database.dao.PersonaDao;
import com.example.crowded.dto.PersonaDto;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PersonaServiceImpl implements PersonaService {
    private final PersonaDao personaDao;
    private PersonaConverter personaConverter = new PersonaConverter();
    private List<PersonaDto> lastUsuarios;
    private PersonaDto usuario;
    private Integer idLastInserted;

    public PersonaServiceImpl(PersonaDao personaDao) {
        this.personaDao = personaDao;
    }

    @Override
    public List<PersonaDto> getAll() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<PersonaDto>> result = executor.submit(new Callable<List<PersonaDto>>() {
            public List<PersonaDto> call() throws Exception {
                return personaConverter.mapListEntityToListDto(personaDao.loadAllPersonas());
            }
        });
        try {
            lastUsuarios = result.get();
        } catch (Exception exception) {
        }
        return lastUsuarios;
    }

    @Override
    public PersonaDto getPersonaById(int id) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<PersonaDto> result = executor.submit(new Callable<PersonaDto>() {
            public PersonaDto call() throws Exception {
                return personaConverter.mapEntityToDto(personaDao.loadPersonaById(id));
            }
        });
        try {
            usuario = result.get();
        } catch (Exception exception) {
        }
        return usuario;
    }

    @Override
    public int getLastInsertedIdPersona() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> result = executor.submit(new Callable<Integer>() {
            public Integer call() throws Exception {
                return personaDao.getLastInsertedIdPersona();
            }
        });
        try {
            idLastInserted = result.get();
        } catch (Exception exception) {
        }
        return idLastInserted;
    }

    @Override
    public void insertOrUpdatePersona(PersonaDto personaDto) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                personaDao.insertPersona(personaConverter.mapDtoToEntity(personaDto));
            }
        });
    }

    @Override
    public void deleteAllPersona() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                personaDao.deleteAll();
            }
        });
    }
}
