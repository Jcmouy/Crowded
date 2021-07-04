package com.example.crowded.repository;

import com.example.crowded.converter.PaisConverter;
import com.example.crowded.database.AppExecutors;
import com.example.crowded.database.dao.PaisDao;
import com.example.crowded.dto.PaisDto;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PaisServiceImpl implements PaisService {
    private final PaisDao paisDao;
    private List<PaisDto> lastPaises;
    private PaisDto pais;
    private PaisConverter paisConverter = new PaisConverter();
    private int idLastInserted;

    public PaisServiceImpl(PaisDao paisDao) {
        this.paisDao = paisDao;
    }

    @Override
    public List<PaisDto> getAll() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<PaisDto>> result = executor.submit(new Callable<List<PaisDto>>() {
            public List<PaisDto> call() throws Exception {
                return paisConverter.mapListEntityToListDto(paisDao.getAll());
            }
        });
        try {
            lastPaises = result.get();
        } catch (Exception exception) {
        }
        return lastPaises;
    }

    @Override
    public PaisDto getPaisById(int id) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<PaisDto> result = executor.submit(new Callable<PaisDto>() {
            public PaisDto call() throws Exception {
                return paisConverter.mapEntityToDto(paisDao.findById(id));
            }
        });
        try {
            pais = result.get();
        } catch (Exception exception) {
        }
        return pais;
    }

    @Override
    public int getLastInsertedIdPais() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> result = executor.submit(new Callable<Integer>() {
            public Integer call() throws Exception {
                return paisDao.getLastInsertedIdPais();
            }
        });
        try {
            idLastInserted = result.get();
        } catch (Exception exception) {
        }
        return idLastInserted;
    }

    @Override
    public void insertOrUpdatePais(PaisDto paisDto) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                paisDao.insertPais(paisConverter.mapDtoToEntity(paisDto));
            }
        });
    }

    @Override
    public void deleteAllPais() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                paisDao.deleteAllPaises();
            }
        });
    }
}
