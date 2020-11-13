package com.lexuantrieu.orderfood.service;

import com.lexuantrieu.orderfood.model.CategoryModel;
import com.lexuantrieu.orderfood.model.ResponseModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface GetCategoryService {
/*
    @GET("getCategory.php")
    Observable<List<CategoryModel>> getCategory();*/

    @GET("getCategory.php")
    Observable<ResponseModel<List<CategoryModel>>> getCategory(
            @Header("Authorization") String authHeader,
            @Query("tokenGET") String token
    );
/*
    @POST("usersInfo/Authenticate")
    Call<LoginResponse> getLoginResponse(
            @Header("Token") String token,
            @Body LoginDatum loginDatum
    );*/
}
