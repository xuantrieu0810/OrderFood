package com.lexuantrieu.orderfood.presenter.impl;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.TableModel;
import com.lexuantrieu.orderfood.model.room.User;
import com.lexuantrieu.orderfood.model.room.database.AppDatabase;
import com.lexuantrieu.orderfood.network.RestClient;
import com.lexuantrieu.orderfood.presenter.ListTableActivityPresenter;
import com.lexuantrieu.orderfood.service.GetListTableService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ListTableActivityPresenterImpl implements ListTableActivityPresenter {

    private Context context;
    private ListTableActivityPresenterImpl.View view;

    public ListTableActivityPresenterImpl(Context context, ListTableActivityPresenterImpl.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void invokeData() {
        view.onInvokeDataPending();
        //Lay token
        AppDatabase db = AppDatabase.getInstance(context);
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
                });
        //-end

    }

    private void CallService(String token) {GetListTableService service = RestClient.createService(GetListTableService.class);
        service.getListTable("Bearer " + token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(data->{
                    List<TableModel> list = data.getData();
                    for(TableModel m: list){
                        m.setImage(R.drawable.imagepreview);
                    }
                    return true;
                })
                .subscribe(response->{
//                    Log.e("LXT_Log", new Gson().toJson(response));
                    if (response.getError().equals("null")) {
                        view.initAdapter(context, response.getData());
                        view.initGridView();
                        view.onInvokeDataSuccess();
                    } else {
                        view.onInvokeDataFail();
                        Log.e("LXT_Log", "ErrorCode: " + response.getError());
                        Toast.makeText(context, "ErrorCode: " + response.getError(), Toast.LENGTH_SHORT).show();
                    }
                },throwable -> {
                    view.onInvokeDataFail();
                    throwable.printStackTrace();
                });

    }
}
