package com.lexuantrieu.orderfood.presenter.impl;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lexuantrieu.orderfood.model.OrderedModel;
import com.lexuantrieu.orderfood.network.RestClient;
import com.lexuantrieu.orderfood.presenter.KitchenActivityPresenter;
import com.lexuantrieu.orderfood.service.OrderListService;
import com.lexuantrieu.orderfood.utils.Utils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class KitchenActivityPresenterImpl implements KitchenActivityPresenter {

    private CompositeDisposable compositeDisposable;
    private Context context;
    private KitchenActivityPresenter.View view;

    public KitchenActivityPresenterImpl(Context context, KitchenActivityPresenter.View view) {
        compositeDisposable = new CompositeDisposable();
        this.context = context;
        this.view = view;
    }

    @Override
    public void invokeData() {
        OrderListService service = RestClient.createService(OrderListService.class);
        compositeDisposable.add(service.GetFoodOrderListAll().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Log.e(Utils.TAG, "Response GetFoodOrderList: "+new Gson().toJson(response));
                    if (response != null) {
                        view.onInvokeDataSuccess(response);
                    } else {
                        view.onInvokeDataFail();
                        Toast.makeText(context, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    Log.e(Utils.TAG_ERROR, "Response GetFoodOrdered: " + throwable.getMessage());
                    throwable.printStackTrace();
                })
        );
    }

    @Override
    public void changeStatusOrderList(int pos, OrderedModel foodModel, boolean isLongClick) {
        OrderListService service = RestClient.createService(OrderListService.class);
        int newStatus = isLongClick ? foodModel.getStatus() - 1 : foodModel.getStatus() + 1;

        compositeDisposable.add(service.UpdateItemOrderList(foodModel.getStt(), foodModel.getQuantity(), foodModel.getComment()==null?"":foodModel.getComment(), newStatus)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Log.i(Utils.TAG, "subscribe: " + response.toString());
                    if (!response.equals("error")) {
                        Log.i(Utils.TAG, "position: " + pos);
                        foodModel.setStatus(newStatus);
                        view.onSuccessSetStatus(foodModel, pos);
                    } else {
                        view.onFailSetStatus();
                    }
                }, throwable -> {
                    Log.e(Utils.TAG, throwable.toString());
                    throwable.printStackTrace();
                })
        );
    }


}
