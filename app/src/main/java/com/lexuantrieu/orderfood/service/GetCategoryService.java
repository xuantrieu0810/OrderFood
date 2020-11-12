package com.lexuantrieu.orderfood.service;

import com.lexuantrieu.orderfood.model.ResponseModel;

import io.reactivex.Observable;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GetCategoryService {
/*
    @GET("getCategory.php")
    Observable<List<CategoryModel>> getCategory();*/

    @POST("getCategory.php")
    Observable<ResponseModel> getCategory(
            @Header("Token") String token,
            @Query("tableid") int tableid
    );
/*
    @POST("usersInfo/Authenticate")
    Call<LoginResponse> getLoginResponse(
            @Header("Token") String token,
            @Body LoginDatum loginDatum
    );*/
}
