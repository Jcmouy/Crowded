package com.example.crowded.io;

import com.example.crowded.dto.PersonaDto;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PersonaServiceIO {

    @POST("api/persona")
    Call<JsonObject> createPersona(@Body PersonaDto personaDTO);


}
