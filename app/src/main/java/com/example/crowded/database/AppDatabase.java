package com.example.crowded.database;

import android.content.Context;
import android.util.Log;

import com.example.crowded.database.converter.DateConverter;
import com.example.crowded.database.converter.EnumConverter;
import com.example.crowded.database.dao.PaisDao;
import com.example.crowded.database.dao.PersonaDao;
import com.example.crowded.database.dao.UsuarioDao;
import com.example.crowded.database.entity.Pais;
import com.example.crowded.database.entity.Persona;
import com.example.crowded.database.entity.Usuario;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Persona.class, Pais.class, Usuario.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class, EnumConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "appCrowdedDB";
    private static AppDatabase sInstance;

    public abstract PersonaDao personaDao();
    public abstract PaisDao paisDao();
    public abstract UsuarioDao usuarioDao();

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }
}
