package com.lexuantrieu.orderfood.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.lexuantrieu.orderfood.network.RestClient;
import com.lexuantrieu.orderfood.network.Server;
import com.lexuantrieu.orderfood.presenter.SetFoodPresenter;
import com.lexuantrieu.orderfood.service.GetCategoryService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SetFoodPresenterImpl implements SetFoodPresenter {
    private Context context;
    private View view;
    private static final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEiLCJ1c2VybmFtZSI6ImFkbWluIiwiZnVsbG5hbWUiOiJMXHUwMGVhIFh1XHUwMGUybiBUcmlcdTFlYzF1IiwiZW1haWwiOiJ4dWFudHJpZXUwODEwQGdtYWlsLmNvbSIsImNyZWF0ZWRfYXQiOiIyMDIwLTExLTEyIDA1OjQ0OjM4In0.sw6q8HBeIirLwGg0SL_2Bsk7M6zdRyjIpV9UZjFzsd4";
    public SetFoodPresenterImpl(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void invokeData() {
        view.onInvokeDataPending();
        GetCategoryService service = RestClient.createService(GetCategoryService.class);
        service.getCategory("Bearer " + Server.TOKEN, TOKEN).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                /*.filter(data->{
                    List<CategoryModel> list = data.getData();
                    *//*for(CategoryModel t:list){
                        //custom data
                    }*//*
                    data.setData(list);
                    return true;
                })*/
                .subscribe(response-> {
                    Log.e("LXT_Log", new Gson().toJson(response));
                    if (response.getError().equals("null")) {
                        view.initAdapter(context, response.getData());
                        view.initSpinner();
                        view.onInvokeDataSuccess();
                    } else {
                        view.onInvokeDataFail();
                        Log.e("LXT_Log", "ErrorCode: " + response.getError());
                    }
                },throwable -> {
                    view.onInvokeDataFail();
                    throwable.printStackTrace();
                });
    }
}
