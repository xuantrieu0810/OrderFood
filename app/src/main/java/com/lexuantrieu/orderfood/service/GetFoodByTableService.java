package com.lexuantrieu.orderfood.service;

import com.lexuantrieu.orderfood.model.Food;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetFoodByTableService {
    @GET("getFoodByTable.php")
    Observable<List<Food>> GetFoodByTable(@Query("tableid") int tableid);
}
