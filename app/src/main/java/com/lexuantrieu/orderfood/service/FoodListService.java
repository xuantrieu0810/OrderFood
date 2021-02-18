package com.lexuantrieu.orderfood.service;

import com.lexuantrieu.orderfood.model.FoodModel;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface FoodListService {

    @FormUrlEncoded
    @POST("getFoodByTable.php")
    Observable<List<FoodModel>> GetFoodMultiType(
            @Field("table_id") int tableid,
            @Query("method") String method);

    @Multipart
    @POST("uploadImage.php")
    Call<String> UploadPhoto(@Part MultipartBody.Part photo);

    @FormUrlEncoded
    @POST("insertFood.php")
    Call<String> InsertFood(@Field("cat_id") int catid
            , @Field("name") String name
            , @Field("slug") String slug
            , @Field("image") String image
            , @Field("number") int number
            , @Field("price") float price
            , @Field("price_sale") String price_sale
            , @Field("created_by") int created_by
            , @Field("status") int status);

    @FormUrlEncoded
    @POST("checkNameFood.php")
    Call<String> CheckExistsName(@Field("name") String name);
}
