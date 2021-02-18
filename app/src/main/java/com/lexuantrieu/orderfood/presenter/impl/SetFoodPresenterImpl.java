package com.lexuantrieu.orderfood.presenter.impl;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lexuantrieu.orderfood.network.RestClient;
import com.lexuantrieu.orderfood.presenter.SetFoodPresenter;
import com.lexuantrieu.orderfood.service.CategoryService;
import com.lexuantrieu.orderfood.utils.Utils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SetFoodPresenterImpl implements SetFoodPresenter {

    private CompositeDisposable compositeDisposable;
    private Context context;
    private View view;

    public SetFoodPresenterImpl(Context context, View view) {
        compositeDisposable = new CompositeDisposable();
        this.context = context;
        this.view = view;
    }

    @Override
    public void invokeData(int func) {
        view.onInvokeDataPending();
        //Lay token
        String token = Utils.GetTokenLocal(context);
        if (token.isEmpty()) {
            view.onInvokeDataFail();
            Log.e("LXT_Log", "Token null");
            return;
        }
        //
        CategoryService service = RestClient.createService(CategoryService.class);
        compositeDisposable.add(service.getCategory("Bearer " + token, func).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Log.e("LXT_Log", new Gson().toJson(response));
                    if (response.getError().equals("null")) {
                        view.initAdapter(context, response.getData());
                        view.initSpinner();
                        view.onInvokeDataSuccess();
                    } else {
                        view.onInvokeDataFail();
                        Log.e("LXT_Log", "ErrorCode: " + response.getError());
                        Toast.makeText(context, "ErrorCode: " + response.getError(), Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    view.onInvokeDataFail();
                    throwable.printStackTrace();
                })
        );
        //
        /*AppDatabase db = AppDatabase.getInstance(context);
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
                });*/
        //-end
    }
}
