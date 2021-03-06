package com.lexuantrieu.orderfood.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    //    private static final String TOKEN_API = "";
    private static final String BASE_URL = ConfigServer.localhost;
    private static Gson gson = new GsonBuilder().setLenient().create();
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson)).client(getHttpClient().build()
            );
    private static Retrofit retrofit = builder.build();

    public static OkHttpClient.Builder getHttpClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
//        httpClient.addInterceptor(new Interceptor() {
//            @NotNull
//            @Override
//            public Response intercept(@NotNull Chain chain) throws IOException {
//                Request newRequest  = chain.request().newBuilder()
//                        .addHeader("Authorization", "Bearer "+TOKEN_API)
//                        .build();
//                return chain.proceed(newRequest);
//            }
//        });
        httpClient
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .pingInterval(1, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .eventListener(new EventListener() {
                    @Override
                    public void callStart(Call call) {
                        super.callStart(call);
                        Log.e("LXT_Log", "Call Start");
                    }

                    @Override
                    public void callEnd(Call call) {
                        super.callEnd(call);
                        Log.e("LXT_Log", "Call End");
                    }

                    @Override
                    public void callFailed(@NotNull Call call, @NotNull IOException ioe) {
                        super.callFailed(call, ioe);

                        Log.e("LXT_Log", "Call Fail");
                    }
                });
        return httpClient;
    }

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
