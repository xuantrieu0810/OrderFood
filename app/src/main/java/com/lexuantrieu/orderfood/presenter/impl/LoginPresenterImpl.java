package com.lexuantrieu.orderfood.presenter.impl;


import android.content.Context;
import android.util.Log;

import com.lexuantrieu.orderfood.model.ProfileModel;
import com.lexuantrieu.orderfood.model.room.User;
import com.lexuantrieu.orderfood.model.room.database.AppDatabase;
import com.lexuantrieu.orderfood.network.RestClient;
import com.lexuantrieu.orderfood.presenter.LoginPresenter;
import com.lexuantrieu.orderfood.service.LoginService;
import com.lexuantrieu.orderfood.utils.LibraryString;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenterImpl implements LoginPresenter {

    private final Context context;
    private final View view;
    private AppDatabase db;
    private List<User> userList;
    public LoginPresenterImpl(Context context, View view, AppDatabase db) {

        this.context = context;
        this.view = view;
        this.db = db;
        this.userList = new ArrayList<>();
    }

    @Override
    public void onCheckToken() {
        view.onStartProcessBar("Loading...");
        db.getUserDao().getListUser().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.get(0).getToken() != null) {
                        view.onLoginSuccess();                    }
                }, throwable -> {
                    view.onStopProcessBar();
                    throwable.printStackTrace();
                });
    }

    @Override
    public void requestLogin(String username, String password) {
        String passwordSHA1 = null;
        try {
            passwordSHA1 = LibraryString.SHA1(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        view.onLoginPending();
        LoginService service = RestClient.createService(LoginService.class);
        service.requetLogin(username, passwordSHA1).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getError().equals("null")) {
                        ProfileModel mUser = response.getData();
                        User user = new User(mUser.getUsername(),mUser.getFullname(),mUser.getRole(),mUser.getToken());
                        db.getUserDao().insertNote(user);
                        //Luu token
                        view.onLoginSuccess();
                        return;
                    } else {
                        view.onLoginFail();
                        Log.e("LXT_Log", "ErrorCode: " + response.getError());
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                });

    }
}
