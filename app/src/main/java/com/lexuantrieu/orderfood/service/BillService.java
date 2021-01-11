package com.lexuantrieu.orderfood.service;

import com.lexuantrieu.orderfood.model.ResponseModel;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface BillService {
    @FormUrlEncoded
    @POST("getBillId.php")
    Observable<ResponseModel<String>> GetBillId(
            @Header("Authorization") String authHeader,
            @Field("table_id") int table_id);

    @FormUrlEncoded
    @POST("payBill.php")
    Observable<ResponseModel<String>> PayBill(
            @Header("Authorization") String authHeader,
            @Field("bill_id") int bill_id,
            @Field("table_id") int table_id);
}
