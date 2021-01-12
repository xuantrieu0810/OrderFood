package com.lexuantrieu.orderfood.service;

import com.lexuantrieu.orderfood.model.CategoryModel;
import com.lexuantrieu.orderfood.model.ResponseModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GetCategoryService {
/*
    @GET("getCategory.php")
    Observable<List<CategoryModel>> getCategory();*/

    @FormUrlEncoded
    @POST("getCategory.php")
    Observable<ResponseModel<List<CategoryModel>>> getCategory(
            @Header("Authorization") String authHeader,
            @Field("FUNC") int func
    );
/*
    @POST("usersInfo/Authenticate")
    Call<LoginResponse> getLoginResponse(
            @Header("Token") String token,
            @Body LoginDatum loginDatum
    );*/
}
