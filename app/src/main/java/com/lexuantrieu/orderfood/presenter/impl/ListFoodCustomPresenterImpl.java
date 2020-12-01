package com.lexuantrieu.orderfood.presenter.impl;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lexuantrieu.orderfood.model.FoodModel;
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
                        for (FoodModel f : response) {
                            f.setImageFood(Server.urlImage + f.getImageFood());
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
                    view.onInvokeDataFail();
                    Log.e("LXT_Log", throwable.toString());
                    throwable.printStackTrace();
                });
    }

    @Override
    public void InsertOrderList(int tableid, FoodModel foodModel, int quantity, int pos) {
        SetFoodOrderListService service = RestClient.createService(SetFoodOrderListService.class);
        service.InsertOrderList(tableid, foodModel.getIdFood(), quantity).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Log.i("LXT_Log", "subscribe: " + response.toString());
                    if (!response.equals("error")) {
                        try {
                            Log.i("LXT_Log", "position: " + pos);
                            foodModel.setStt(Integer.parseInt(response));
                            foodModel.setCountFood(quantity);
//                            adapterListener.onItemChange(pos, food);
                            view.onSuccessSetFood(foodModel, pos);
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
    public void UpdateOrderList(int tableid, FoodModel foodModel, int quantity, int pos) {
        SetFoodOrderListService service = RestClient.createService(SetFoodOrderListService.class);
        service.UpdateOrderList(foodModel.getStt(), tableid, foodModel.getIdFood(), quantity).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Log.i("LXT_Log", "subscribe: " + response.toString());
                    if (!response.equals("error")) {
                        Log.i("LXT_Log", "position: " + pos);
                        foodModel.setCountFood(quantity);
//                            adapterListener.onItemChange(pos,food);
                        view.onSuccessSetFood(foodModel, pos);

                    } else {
                        view.onFailSetFood();                    }
                },throwable -> {
                    Log.e("LXT_Log", throwable.toString());
                    throwable.printStackTrace();
                });
    }


}
