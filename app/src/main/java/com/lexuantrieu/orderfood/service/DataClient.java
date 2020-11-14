package com.lexuantrieu.orderfood.service;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DataClient {

    @Multipart
    @POST("uploadImage.php")
    Call<String> UploadPhoto(@Part MultipartBody.Part photo);

    @FormUrlEncoded
    @POST("insertFood.php")
    Call<String> InsertFood(@Field("catid") int catid
            , @Field("name") String name
            , @Field("slug") String slug
            , @Field("image") String image
            , @Field("number") int number
            , @Field("price") float price
            , @Field("pricesale") String pricesale
            , @Field("created_by") int created_by
            , @Field("status") int status);

    @FormUrlEncoded
    @POST("checkNameFood.php")
    Call<String> CheckExistsName(@Field("name") String name);
}
