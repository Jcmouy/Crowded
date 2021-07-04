package com.example.crowded.repository;

import com.example.crowded.converter.UsuarioConverter;
import com.example.crowded.database.AppExecutors;
import com.example.crowded.database.dao.UsuarioDao;
import com.example.crowded.dto.UsuarioDto;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioDao usuarioDao;
    private UsuarioConverter usuarioConverter = new UsuarioConverter();
    private List<UsuarioDto> lastUsuarios;
    private UsuarioDto usuario;
    private Integer idLastInserted;

    public UsuarioServiceImpl(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    @Override
    public List<UsuarioDto> getAll() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<UsuarioDto>> result = executor.submit(new Callable<List<UsuarioDto>>() {
            public List<UsuarioDto> call() throws Exception {
                return usuarioConverter.mapListEntityToListDto(usuarioDao.loadAllUsuarios());
            }
        });
        try {
            lastUsuarios = result.get();
        } catch (Exception exception) {
        }
        return lastUsuarios;
    }

    @Override
    public UsuarioDto getUsuarioById(int id) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<UsuarioDto> result = executor.submit(new Callable<UsuarioDto>() {
            public UsuarioDto call() throws Exception {
                return usuarioConverter.mapEntityToDto(usuarioDao.loadUsuarioById(id));
            }
        });
        try {
            usuario = result.get();
        } catch (Exception exception) {
        }
        return usuario;
    }

    @Override
    public int getLastInsertedIdUsuario() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> result = executor.submit(new Callable<Integer>() {
            public Integer call() throws Exception {
                return usuarioDao.getLastInsertedIdUsuario();
            }
        });
        try {
            idLastInserted = result.get();
        } catch (Exception exception) {
        }
        return idLastInserted;
    }

    @Override
    public void insertOrUpdateUsuario(UsuarioDto usuarioDto) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                usuarioDao.insertUsuario(usuarioConverter.mapDtoToEntity(usuarioDto));
            }
        });
    }

    @Override
    public void deleteAllUsuario() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                usuarioDao.deleteAll();
            }
        });
    }
}
