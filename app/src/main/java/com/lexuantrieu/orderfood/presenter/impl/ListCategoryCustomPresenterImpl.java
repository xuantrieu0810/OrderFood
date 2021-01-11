package com.lexuantrieu.orderfood.presenter.impl;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lexuantrieu.orderfood.network.RestClient;
import com.lexuantrieu.orderfood.presenter.ListCategoryCustomPresenter;
import com.lexuantrieu.orderfood.service.GetCategoryService;
import com.lexuantrieu.orderfood.utils.Utils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ListCategoryCustomPresenterImpl implements ListCategoryCustomPresenter {
    private Context context;
    private ListCategoryCustomPresenter.View view;

    public ListCategoryCustomPresenterImpl(Context context, ListCategoryCustomPresenter.View view) {
        this.context = context;
        this.view = view;
    }


    @Override
    public void invokeData() {
        view.onInvokeDataPending();
        //Lay token
        String token = Utils.GetTokenLocal(context);
        if(token.isEmpty()) {
            view.onInvokeDataFail();
            Log.e("LXT_Log", "Token null");
            return;
        }
        //
        GetCategoryService service = RestClient.createService(GetCategoryService.class);
        service.getCategory("Bearer " + token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response-> {
                    Log.e("LXT_Log", new Gson().toJson(response));
                    if (response.getError().equals("null")) {
                        view.initAdapter(context, response.getData());
                        view.initRecyclerView();
                        view.onInvokeDataSuccess();
                    } else {
                        view.onInvokeDataFail();
                        Log.e("LXT_Log", "ErrorCode: " + response.getError());
                        Toast.makeText(context, "ErrorCode: " + response.getError(), Toast.LENGTH_SHORT).show();
                    }
                },throwable -> {
                    view.onInvokeDataFail();
                    throwable.printStackTrace();
                });
    }



}