package com.lexuantrieu.orderfood.service;

import com.lexuantrieu.orderfood.model.ProfileModel;
import com.lexuantrieu.orderfood.model.ResponseModel;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginService {

    @FormUrlEncoded
    @POST("loginUser.php")
    Observable<ResponseModel<ProfileModel>> requetLogin(
            @Field("username") String username
            , @Field("password") String password );
}
