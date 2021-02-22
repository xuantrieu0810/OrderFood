package com.lexuantrieu.orderfood.service;

import com.lexuantrieu.orderfood.model.FoodModel;
import com.lexuantrieu.orderfood.model.OrderedModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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
            @Field("quantity") int quantity,
            @Field("comment") String commentFood,
            @Field("status") int status
    );

    @FormUrlEncoded
    @POST("orderlist.php?method=get_order_list_by_table")
    Observable<List<FoodModel>> GetFoodOrderList(
            @Field("table_id") int tableid,
            @Field("bill_id") int billid
    );

    @GET("orderlist.php?method=get_order_list_all")
    Observable<List<OrderedModel>> GetFoodOrderListAll(

    );
}
