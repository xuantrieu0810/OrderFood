package com.lexuantrieu.orderfood.service;

import com.lexuantrieu.orderfood.model.ResponseModel;
import com.lexuantrieu.orderfood.model.TableModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface GetListTableService {

    @GET("getListTable.php")
    Observable<ResponseModel<List<TableModel>>> getListTable(
            @Header("Authorization") String authHeader
    );
}
