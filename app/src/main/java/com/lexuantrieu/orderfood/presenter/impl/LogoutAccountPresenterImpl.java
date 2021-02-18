package com.lexuantrieu.orderfood.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.lexuantrieu.orderfood.model.room.User;
import com.lexuantrieu.orderfood.model.room.database.AppDatabase;
import com.lexuantrieu.orderfood.network.RestClient;
import com.lexuantrieu.orderfood.presenter.LogoutAccountPresenter;
import com.lexuantrieu.orderfood.service.UserService;
import com.lexuantrieu.orderfood.utils.Utils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LogoutAccountPresenterImpl implements LogoutAccountPresenter {

    private CompositeDisposable compositeDisposable;
    private Context context;
    private LogoutAccountPresenterImpl.View view;
    private AppDatabase db;
    private User user;

    public LogoutAccountPresenterImpl(Context context, LogoutAccountPresenterImpl.View view) {
        this.compositeDisposable = new CompositeDisposable();
        this.context = context;
        this.view = view;
        db = AppDatabase.getInstance(context);
    }

    @Override
    public void onLogout() {

        UserService service = RestClient.createService(UserService.class);
        compositeDisposable.add(service.requetLogout(user.getUsername()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
//                    Log.i("LXT_Log", new Gson().toJson(response));
                            if (response.getError().equals("null")) {
                                //Xoa token
                                compositeDisposable.add(db.getUserDao().deleteUserById(user.getId()).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(res -> {
                                            if (res > 0) {
                                                Utils.RemoveTokenLocal(context);
                                                view.onLogoutSuccess();
                                            } else
                                                view.onLogoutFail();
                                        }, Throwable::printStackTrace)
                                );
                            } else {
                                view.onLogoutFail();
                                Log.e("LXT_Log", "ErrorCode: " + response.getError());
                            }
                        }, throwable -> {
                            Log.e("LXT_Log_Error", "Response Logout: " + throwable.getMessage());
                        })
        );
    }
}
