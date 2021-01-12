package com.lexuantrieu.orderfood.presenter.impl;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lexuantrieu.orderfood.model.room.User;
import com.lexuantrieu.orderfood.model.room.database.AppDatabase;
import com.lexuantrieu.orderfood.network.RestClient;
import com.lexuantrieu.orderfood.presenter.MainActivityPresenter;
import com.lexuantrieu.orderfood.service.LogoutService;
import com.lexuantrieu.orderfood.service.SetFoodOrderListService;
import com.lexuantrieu.orderfood.utils.Utils;

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
                user = response.get(0);
                if(user != null){
                    view.onInvokeDataSuccess(user);
                } else {
                    view.onInvokeDataFail();
                }
            }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void onLogout() {
        //Lay token
        String token = Utils.GetTokenLocal(context);
        if(token.isEmpty()) {
            Log.e("LXT_Log", "Token null");
            view.onLogoutFail();
            return;
        }
        view.onLogoutPending();
        //
        LogoutService service = RestClient.createService(LogoutService.class);
        service.requetLogout(user.getUsername()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response-> {
//                    Log.i("LXT_Log", new Gson().toJson(response));
                    if (response.getError().equals("null")) {
                        //Xoa token
                        db.getUserDao().deleteUserById(user.getId()).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(res->{
                                    if(res>0) {
                                        Utils.RemoveTokenLocal(context);
                                        view.onLogoutSuccess();
                                    }else
                                        view.onLogoutFail();
                                }, Throwable::printStackTrace);
                        //
                    } else {
                        view.onLogoutFail();
                        Log.e("LXT_Log", "ErrorCode: " + response.getError());
                    }
                }, Throwable::printStackTrace);
    }

    @Override
    public void checkTableStatus() {
        SetFoodOrderListService service = RestClient.createService(SetFoodOrderListService.class);
        service.CheckTableStatus().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Log.i("LXT_Log", "subscribe: " + response.toString());
                    if (!response.equals("error")) {
                        Toast.makeText(context, "Đã Update", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "LỖI", Toast.LENGTH_SHORT).show();
                    }
                },throwable -> {
                    Log.e("LXT_Log", throwable.toString());
                    throwable.printStackTrace();
                });
    }
}
