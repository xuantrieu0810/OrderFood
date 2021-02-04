package com.lexuantrieu.orderfood.service;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OrderListService {
    @FormUrlEncoded
    @POST("orderlist.php?method=add")
    Observable<String> AddItemOrderList(
            @Field("bill_id") int bill_id,
            @Field("table_id") int table_id,
            @Field("food_id") int food_id,
            @Field("quantity") int quantity);

    @FormUrlEncoded
    @POST("orderlist.php?method=update")
    Observable<String> UpdateItemOrderList(
            @Field("stt") int stt,
            @Field("bill_id") int bill_id,
            @Field("table_id") int table_id,
            @Field("food_id") int food_id,
            @Field("quantity") int quantity,
            @Field("comment") String commentFood);

}
