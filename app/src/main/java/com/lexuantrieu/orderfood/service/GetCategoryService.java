package com.lexuantrieu.orderfood.service;

import com.lexuantrieu.orderfood.model.CategoryModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface GetCategoryService {

    @GET("getCategory.php")
    Observable<List<CategoryModel>> getCategory();
}
