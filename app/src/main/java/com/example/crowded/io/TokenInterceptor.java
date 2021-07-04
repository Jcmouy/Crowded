package com.example.crowded.io;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {
    private static String sessionToken;

    public static void setSessionToken(String sessionToken) {
        TokenInterceptor.sessionToken = sessionToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request newRequest=chain.request().newBuilder()
                .header("Authorization","Bearer "+ sessionToken)
                .build();

        return chain.proceed(newRequest);
    }
}
