package com.lexuantrieu.orderfood.service;

import com.lexuantrieu.orderfood.model.FoodModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetFoodByTableService {
    @FormUrlEncoded
    @POST("getFoodByTable.php")
    Observable<List<FoodModel>> GetFoodByTable(
            @Field("table_id") int tableid,
            @Field("FUNC") int func );
}
