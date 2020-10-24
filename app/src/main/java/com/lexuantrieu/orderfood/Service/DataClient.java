package com.lexuantrieu.orderfood.Service;

import com.lexuantrieu.orderfood.model.Category;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DataClient {

    @GET("getCategory.php")
    Call<List<Category>> GetCategory();

    @Multipart
    @POST("uploadImage.php")
    Call<String> UploadPhoto(@Part MultipartBody.Part photo);

    @FormUrlEncoded
    @POST("insertFood.php")
    Call<String> InsertFood(@Field("catid") String catid
                            ,@Field("name") String name
                            ,@Field("slug") String slug
                            ,@Field("image") String image
                            ,@Field("number") String number
                            ,@Field("price") String price
                            ,@Field("pricesale") String pricesale
                            ,@Field("created_by") String created_by
                            ,@Field("status") String status);
    @FormUrlEncoded
    @POST("checkNameFood.php")
    Call<String> CheckExistsName(@Field("name") String name);
}
