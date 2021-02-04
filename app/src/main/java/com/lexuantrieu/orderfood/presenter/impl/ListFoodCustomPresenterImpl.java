package com.lexuantrieu.orderfood.presenter.impl;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lexuantrieu.orderfood.model.FoodModel;
import com.lexuantrieu.orderfood.network.ConfigServer;
import com.lexuantrieu.orderfood.network.RestClient;
import com.lexuantrieu.orderfood.presenter.ListFoodCustomPresenter;
import com.lexuantrieu.orderfood.service.FoodListService;
import com.lexuantrieu.orderfood.service.OrderListService;
import com.lexuantrieu.orderfood.utils.LibraryString;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ListFoodCustomPresenterImpl implements ListFoodCustomPresenter {

    private CompositeDisposable compositeDisposable;
    private Context context;
    private ListFoodCustomPresenter.View view;

    public ListFoodCustomPresenterImpl(Context context, ListFoodCustomPresenter.View view) {
        compositeDisposable = new CompositeDisposable();
        this.context = context;
        this.view = view;
    }

    @Override
    public void invokeData(int tableid, int func) {
        FoodListService service = RestClient.createService(FoodListService.class);
        compositeDisposable.add(service.GetFoodByTable(tableid, func).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .filter(data->{
//                    for(Food f:data){
//                        f.setNameFoodNonVN(LibaryString.covertStringToVN(f.getNameFood()));
//                    }
//                    return true;
//                })
                .subscribe(response->{
//                    Log.e("LXT_Log", "Response GetFoodByTable: "+new Gson().toJson(response));
                    if(response != null) {
                        for (FoodModel f : response) {
                            f.setImageFood(ConfigServer.urlImageProduct + f.getImageFood());
                            f.setNameFoodNonVN(LibraryString.covertStringToVN(f.getNameFood()));
                        }
                        view.initAdapter(context, response);
                        view.initRecyclerView();
                        view.onInvokeDataSuccess();
                    }
                    else {

                        view.onInvokeDataFail();
                        Toast.makeText(context, "ErrorCode: " + response, Toast.LENGTH_SHORT).show();
                    }
                },throwable -> {
                    Log.e("LXT_Log_Error","Response GetFoodByTable: "+throwable.getMessage());
                    throwable.printStackTrace();
                })
        );
    }

    @Override
    public void InsertOrderList(int bill_id, int table_id, int pos, FoodModel foodModel) {
        OrderListService service = RestClient.createService(OrderListService.class);
        compositeDisposable.add(service.AddItemOrderList(bill_id, table_id, foodModel.getIdFood(), foodModel.getCountFood()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Log.i("LXT_Log", "subscribe: " + response.toString());
                    int stt = -1;
                    try {
                        stt = (Integer.parseInt(response));
                    }catch (Exception e) {
                        Log.e("LXT_Log", e.toString());
                    }
                    if (!response.equals("fail") && stt!=-1) {
                        Log.i("LXT_Log", "position: " + pos);
                        foodModel.setStt(stt);
                        view.onSuccessSetFood(foodModel, pos);
                    } else {
                        view.onFailSetFood();
                    }
                },throwable -> {
                    Log.e("LXT_Log", throwable.toString());
                    throwable.printStackTrace();
                })
        );
    }

    @Override
    public void UpdateOrderList(int bill_id, int table_id, int pos, FoodModel foodModel) {
        OrderListService service = RestClient.createService(OrderListService.class);
        compositeDisposable.add(service.UpdateItemOrderList(foodModel.getStt(), bill_id, table_id, foodModel.getIdFood(), foodModel.getCountFood(), foodModel.getCommentFood())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Log.i("LXT_Log", "subscribe: " + response.toString());
                    if (!response.equals("error")) {
                        Log.i("LXT_Log", "position: " + pos);
                        view.onSuccessSetFood(foodModel, pos);
                    } else {
                        view.onFailSetFood();                    }
                },throwable -> {
                    Log.e("LXT_Log", throwable.toString());
                    throwable.printStackTrace();
                })
        );
    }


}
