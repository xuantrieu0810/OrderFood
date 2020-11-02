package com.lexuantrieu.orderfood.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private static final String BASE_URL = Server.localhost;
    private static Gson gson = new GsonBuilder().setLenient().create();;
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson)).client(getHttpClient().build());


    public static OkHttpClient.Builder getHttpClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient
                .readTimeout(5,TimeUnit.SECONDS)
                .writeTimeout(5,TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .pingInterval(1, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .eventListener(new EventListener() {
                    @Override
                    public void callStart(Call call) {
                        super.callStart(call);
                        Log.e("Start call", "Start");
                    }

                    @Override
                    public void callEnd(Call call) {
                        super.callEnd(call);
                        Log.e("Stop call", "End");
                    }
                });
        return httpClient;
    }

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
