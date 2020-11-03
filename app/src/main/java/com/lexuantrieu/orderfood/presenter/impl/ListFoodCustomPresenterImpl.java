package com.lexuantrieu.orderfood.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.lexuantrieu.orderfood.model.Food;
import com.lexuantrieu.orderfood.network.RestClient;
import com.lexuantrieu.orderfood.network.Server;
import com.lexuantrieu.orderfood.presenter.ListFoodCustomPresenter;
import com.lexuantrieu.orderfood.service.GetFoodByTableService;
import com.lexuantrieu.orderfood.service.SetFoodOrderListService;
import com.lexuantrieu.orderfood.utils.LibraryString;

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
//                    Log.e("LXT_Log",new Gson().toJson(response));
                    if(response != null) {
                        for(Food f:response){
                            f.setImageFood(Server.urlImage + f.getImageFood());
                            f.setNameFoodNonVN(LibraryString.covertStringToVN(f.getNameFood()));
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

    @Override
    public void InsertOrderList(int tableid, Food food, int quantity, int pos) {
        SetFoodOrderListService service = RestClient.createService(SetFoodOrderListService.class);
        service.InsertOrderList(tableid,food.getIdFood(),quantity).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response->{
                    Log.i("LXT_Log","subscribe: "+response.toString());
                    if (!response.equals("error")) {
                        try {
                            Log.i("LXT_Log", "position: " + pos);
                            food.setStt(Integer.parseInt(response));
                            food.setCountFood(quantity);
//                            adapterListener.onItemChange(pos, food);
                            view.onSuccessSetFood(food, pos);
                        }catch (Exception e) {
                            view.onFailSetFood();
                        }
                    } else {
                        view.onFailSetFood();
                    }
                },throwable -> {
                    Log.e("LXT_Log", throwable.toString());
                    throwable.printStackTrace();
                });
    }

    @Override
    public void UpdateOrderList(int tableid, Food food, int quantity, int pos) {
        SetFoodOrderListService service = RestClient.createService(SetFoodOrderListService.class);
        service.UpdateOrderList(food.getStt(), tableid,food.getIdFood(),quantity).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response->{
                    Log.i("LXT_Log","subscribe: "+response.toString());
                    if (!response.equals("error")) {
                        Log.i("LXT_Log", "position: " + pos);
                        food.setCountFood(quantity);
//                            adapterListener.onItemChange(pos,food);
                        view.onSuccessSetFood(food, pos);

                    } else {
                        view.onFailSetFood();                    }
                },throwable -> {
                    Log.e("LXT_Log", throwable.toString());
                    throwable.printStackTrace();
                });
    }


}
