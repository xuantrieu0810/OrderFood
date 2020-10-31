package com.lexuantrieu.orderfood.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.lexuantrieu.orderfood.model.Food;
import com.lexuantrieu.orderfood.network.RestClient;
import com.lexuantrieu.orderfood.presenter.ListFoodCustomPresenter;
import com.lexuantrieu.orderfood.service.GetFoodByTableService;
import com.lexuantrieu.orderfood.utils.LibaryString;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ListFoodCustomPresenterImpl implements ListFoodCustomPresenter {
    private Context context;
    private ListFoodCustomPresenter.View view;

    public ListFoodCustomPresenterImpl(Context context, ListFoodCustomPresenter.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void invokeData(int tableid) {
        view.onInvokeDataPending();
        GetFoodByTableService service = RestClient.createService(GetFoodByTableService.class);
        service.GetFoodByTable(tableid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .filter(data->{
//                    for(Food f:data){
//                        f.setNameFoodNonVN(LibaryString.covertStringToVN(f.getNameFood()));
//                    }
//                    return true;
//                })
                .subscribe(response->{
                    Log.e("LXT_Log",new Gson().toJson(response));
                    if(response != null) {
                        for(Food f:response){
                            f.setNameFoodNonVN(LibaryString.covertStringToVN(f.getNameFood()));
                        }
                        view.initAdapter(context, response);
                        view.initRecyclerView();
                        view.onInvokeDataSuccess();
                    }
                    else {
                        view.onInvokeDataFail();
                    }
                },throwable -> {
                    view.onInvokeDataFail();
                    Log.e("LXT_Log", throwable.toString());
                    throwable.printStackTrace();
                });
    }

}
