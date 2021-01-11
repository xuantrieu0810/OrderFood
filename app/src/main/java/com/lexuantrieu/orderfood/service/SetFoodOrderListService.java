package com.lexuantrieu.orderfood.service;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SetFoodOrderListService {
    @FormUrlEncoded
    @POST("insertOrderList.php")
    Observable<String> InsertOrderList(
            @Field("bill_id") int bill_id
            , @Field("table_id") int table_id
            , @Field("food_id") int food_id
            , @Field("quantity") int quantity);

    @FormUrlEncoded
    @POST("updateOrderList.php")
    Observable<String> UpdateOrderList(
            @Field("stt") int stt
            , @Field("bill_id") int bill_id
            , @Field("table_id") int table_id
            , @Field("food_id") int food_id
            , @Field("quantity") int quantity);


    @GET("checkTableStatus.php")
    Observable<String> CheckTableStatus();
}
