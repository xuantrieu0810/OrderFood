package com.lexuantrieu.orderfood.presenter.impl;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lexuantrieu.orderfood.model.CategoryModel;
import com.lexuantrieu.orderfood.network.ConfigServer;
import com.lexuantrieu.orderfood.network.RestClient;
import com.lexuantrieu.orderfood.presenter.ListCategoryCustomPresenter;
import com.lexuantrieu.orderfood.service.CategoryService;
import com.lexuantrieu.orderfood.utils.Utils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ListCategoryCustomPresenterImpl implements ListCategoryCustomPresenter {

    private CompositeDisposable compositeDisposable;
    private Context context;
    private ListCategoryCustomPresenter.View view;

    public ListCategoryCustomPresenterImpl(Context context, ListCategoryCustomPresenter.View view) {
        compositeDisposable = new CompositeDisposable();
        this.context = context;
        this.view = view;
    }


    @Override
    public void invokeData(int func) {
        //Lay token
        String token = Utils.GetTokenLocal(context);
        if(token.isEmpty()) {
            view.onInvokeDataFail();
            Log.e("LXT_Log", "Token null");
            return;
        }
        //
        CategoryService service = RestClient.createService(CategoryService.class);

        compositeDisposable.add(service.getCategory("Bearer " + token, func).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response-> {
//                    Log.e("LXT_Log","Response GetCategory: "+ new Gson().toJson(response));
                    if (response.getError().equals("null")) {
//                        List<CategoryModel> listRes = response.getData();
                        for (CategoryModel cat : response.getData()) {
                            cat.setImage(ConfigServer.urlImageCategory + cat.getImage());
                        }
                        view.initAdapter(context, response.getData());
                        view.initRecyclerView();
                        view.onInvokeDataSuccess();
                    }
                    else
                    {
                        view.onInvokeDataFail();
                        Log.e("LXT_Log", "ErrorCode: " + response.getError());
                        Toast.makeText(context, "ErrorCode: " + response.getError(), Toast.LENGTH_SHORT).show();
                    }
                },throwable -> {
                    Log.e("LXT_Log_Error","Response GetCategory: "+throwable.getMessage());
                    throwable.printStackTrace();
                })
        );
    }
}