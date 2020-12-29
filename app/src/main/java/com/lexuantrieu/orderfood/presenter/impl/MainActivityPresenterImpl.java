package com.lexuantrieu.orderfood.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.lexuantrieu.orderfood.model.room.User;
import com.lexuantrieu.orderfood.model.room.database.AppDatabase;
import com.lexuantrieu.orderfood.network.RestClient;
import com.lexuantrieu.orderfood.presenter.MainActivityPresenter;
import com.lexuantrieu.orderfood.service.LogoutService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivityPresenterImpl implements MainActivityPresenter {

    private Context context;
    private MainActivityPresenter.View view;
    private AppDatabase db;
    private User user;

    public MainActivityPresenterImpl(Context context, MainActivityPresenter.View view) {
        this.context = context;
        this.view = view;
        db = AppDatabase.getInstance(context);
    }

    @Override
    public void invokeData() {
        db.getUserDao().getListUser().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(response -> {
//                Log.e(Utils.TAG, new Gson().toJson(response));
                user = (User) response.get(0);
                if(user != null){
                    view.onInvokeDataSuccess(user);
                } else {
                    view.onInvokeDataFail();
                }
            }, throwable -> {
                throwable.printStackTrace();
            });
    }

    @Override
    public void onLogout() {
        view.onLogoutPending();
        LogoutService service = RestClient.createService(LogoutService.class);
        service.requetLogout("Bearer " + user.getToken(), user.getUsername()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response-> {
//                    Log.i("LXT_Log", new Gson().toJson(response));
                    if (response.getError().equals("null")) {
                        //Xoa token
                        db.getUserDao().deleteUserById(user.getId()).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(res->{
                                    if(res>0) {
                                        view.onLogoutSuccess();
                                    }else
                                        view.onLogoutFail();
                                },throwable -> throwable.printStackTrace());
                        //
                    } else {
                        view.onLogoutFail();
                        Log.e("LXT_Log", "ErrorCode: " + response.getError());
                    }
                },throwable -> {
                    throwable.printStackTrace();
                });
    }
}
