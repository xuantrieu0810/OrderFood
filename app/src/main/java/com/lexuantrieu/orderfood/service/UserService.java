package com.lexuantrieu.orderfood.service;

import com.lexuantrieu.orderfood.model.ProfileModel;
import com.lexuantrieu.orderfood.model.ResponseModel;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserService {


    @FormUrlEncoded
    @POST("userlist.php?method=login")
    Observable<ResponseModel<ProfileModel>> requetLogin(
            @Field("username") String username
            , @Field("password") String password );

    @FormUrlEncoded
    @POST("userlist.php?method=logout")

    Observable<ResponseModel<String>> requetLogout(
//            @Header("Authorization") String authHeader,
            @Field("username") String username );
}
