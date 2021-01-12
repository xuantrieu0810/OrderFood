package com.lexuantrieu.orderfood.service;

import com.lexuantrieu.orderfood.model.ResponseModel;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LogoutService {

    @FormUrlEncoded
    @POST("logoutUser.php")

    Observable<ResponseModel<String>> requetLogout(
//            @Header("Authorization") String authHeader,
            @Field("username") String username );
}
