package com.lexuantrieu.orderfood.service;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SetFoodOrderListService {
    @FormUrlEncoded
    @POST("insertOrderList.php")
    Observable<String> InsertOrderList(@Field("tableid") int tableid
            , @Field("foodid") int foodid
            , @Field("quantity") int quantity);

    @FormUrlEncoded
    @POST("insertOrderList.php")
    Observable<String> UpdateOrderList(@Field("stt") int stt
            ,@Field("tableid") int tableid
            , @Field("foodid") int foodid
            , @Field("quantity") int quantity);
}
