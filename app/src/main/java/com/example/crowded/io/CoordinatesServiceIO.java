package com.example.crowded.io;

import com.example.crowded.dto.Io.UsuarioServiceDto;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CoordinatesServiceIO {

    @GET("/coordinates/population/{longitude}/{latitude}")
    Call<List<JsonObject>> getLocation(@Path("longitude") Double longitude,
                                               @Path("latitude") Double latitude);

}
