package com.lexuantrieu.orderfood.service;

import com.lexuantrieu.orderfood.model.FoodModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetFoodByTableService {
    @GET("getFoodByTable.php")
    Observable<List<FoodModel>> GetFoodByTable(@Query("tableid") int tableid);
}
