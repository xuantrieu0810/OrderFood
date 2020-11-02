package com.lexuantrieu.orderfood.presenter.impl;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lexuantrieu.orderfood.model.Food;
import com.lexuantrieu.orderfood.network.RestClient;
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
    public void InsertOrderList(int tableid, int foodid, int quantity) {
        SetFoodOrderListService service = RestClient.createService(SetFoodOrderListService.class);
        service.InsertOrderList(tableid,foodid,quantity).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response->{
                    Log.i("LXT_Log","subscribe: "+response.toString());
                    if (!response.equals("error")) {
                        Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Lỗi không thêm được", Toast.LENGTH_SHORT).show();
                    }
                },throwable -> {
                    Log.e("LXT_Log", throwable.toString());
                    throwable.printStackTrace();
                });
    }

    @Override
    public void UpdateOrderList(int stt, int tableid, int foodid, int quantity) {
        SetFoodOrderListService service = RestClient.createService(SetFoodOrderListService.class);
        service.UpdateOrderList(stt, tableid,foodid,quantity).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response->{
                    Log.i("LXT_Log","subscribe: "+response.toString());
                    if (!response.equals("error")) {
                        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                },throwable -> {
                    Log.e("LXT_Log", throwable.toString());
                    throwable.printStackTrace();
                });
    }


}
