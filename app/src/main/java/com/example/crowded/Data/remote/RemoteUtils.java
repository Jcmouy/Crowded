package com.example.crowded.Data.remote;

import com.example.crowded.BuildConfig;
import com.example.crowded.io.PersonaServiceIO;
import com.example.crowded.io.RetrofitClient;
import com.example.crowded.io.SmsServiceIO;
import com.example.crowded.io.UsuarioServiceIO;

public class RemoteUtils {

    private RemoteUtils(){}

    public static SmsServiceIO getSmsService(){
        return RetrofitClient.getClient(BuildConfig.BASE_URL).create(SmsServiceIO.class);
    }

    public static PersonaServiceIO getPersonaServiceIo(){
        return RetrofitClient.getClient(BuildConfig.BASE_URL).create(PersonaServiceIO.class);
    }

    public static UsuarioServiceIO getUsuarioServiceIo(){
        return RetrofitClient.getClient(BuildConfig.BASE_URL).create(UsuarioServiceIO.class);
    }

}
