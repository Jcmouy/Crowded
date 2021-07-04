package com.example.crowded.view.model;

import android.content.Context;

import com.example.crowded.database.AppDatabase;
import com.example.crowded.repository.PaisService;
import com.example.crowded.repository.PaisServiceImpl;
import com.example.crowded.repository.PersonaService;
import com.example.crowded.repository.PersonaServiceImpl;
import com.example.crowded.repository.UsuarioService;
import com.example.crowded.repository.UsuarioServiceImpl;

public class Injection {

    public static PersonaService providePersonaService(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        return new PersonaServiceImpl(database.personaDao());
    }

    public static UsuarioService provideUsuarioService(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        return new UsuarioServiceImpl(database.usuarioDao());
    }


    public static PaisService providePaisService(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        return new PaisServiceImpl(database.paisDao());
    }

}
