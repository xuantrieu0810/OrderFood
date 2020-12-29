package com.lexuantrieu.orderfood.presenter.impl;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lexuantrieu.orderfood.model.room.User;
import com.lexuantrieu.orderfood.model.room.database.AppDatabase;
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
        //Lay token
        AppDatabase db = AppDatabase.getInstance(context);
        db.getUserDao().getListUser().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    User user = (User) response.get(0);
                    if(user != null){
                        CallService(user.getToken());
                    } else {
                        view.onInvokeDataFail();
                        Log.e("LXT_Log", "ErrorCode: " + response);
                        Toast.makeText(context, "ErrorCode: " + response, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                });
        //-end
    }

    private void CallService(String token) {
        GetCategoryService service = RestClient.createService(GetCategoryService.class);
        service.getCategory("Bearer " + token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response-> {
                    Log.e("LXT_Log", new Gson().toJson(response));
                    if (response.getError().equals("null")) {
                        view.initAdapter(context, response.getData());
                        view.initSpinner();
                        view.onInvokeDataSuccess();
                    } else {
                        view.onInvokeDataFail();
                        Log.e("LXT_Log", "ErrorCode: " + response.getError());
                        Toast.makeText(context, "ErrorCode: " + response.getError(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                },throwable -> {
                    view.onInvokeDataFail();
                    throwable.printStackTrace();
                });
    }
}
