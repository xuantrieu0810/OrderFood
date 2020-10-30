package com.lexuantrieu.orderfood.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.lexuantrieu.orderfood.network.RestClient;
import com.lexuantrieu.orderfood.presenter.SetFoodPresenter;
import com.lexuantrieu.orderfood.service.GetCategoryService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SetFoodPresenterImpl implements SetFoodPresenter {
    private Context context;
    private View view;

    public SetFoodPresenterImpl(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void invokeData() {
        view.onInvokeDataPending();
        GetCategoryService service = RestClient.createService(GetCategoryService.class);
        service.getCategory().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response->{
                    Log.e("LXT_Log",new Gson().toJson(response));
                    if(response != null) {
                        view.initAdapter(context, response);
                        view.initSpinner();
                        view.onInvokeDataSuccess();
                    }
                    else {
                        view.onInvokeDataFail();
                    }
                },throwable -> {
                    view.onInvokeDataFail();
                    throwable.printStackTrace();
                });
    }
}
