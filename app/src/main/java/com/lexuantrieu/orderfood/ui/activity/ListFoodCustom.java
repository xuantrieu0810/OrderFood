package com.lexuantrieu.orderfood.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.FoodModel;
import com.lexuantrieu.orderfood.presenter.ListFoodCustomPresenter;
import com.lexuantrieu.orderfood.presenter.impl.ListFoodCustomPresenterImpl;
import com.lexuantrieu.orderfood.ui.adapter.FoodAdapter;
import com.lexuantrieu.orderfood.ui.dialog.AlertDialogFragment;
import com.lexuantrieu.orderfood.utils.CheckConnection;

import java.util.ArrayList;
import java.util.List;

public class ListFoodCustom extends AppCompatActivity implements ListFoodCustomPresenter.View {

    ListFoodCustomPresenter presenter;
    ProgressDialog progressDialog;
    int tableID = 9; //default = -1
    RecyclerView recyclerView;
    ArrayList<FoodModel> arrayFoodModel;
    FoodAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food_custom);
        init();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null);
        recyclerView.setItemAnimator(null);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        if (CheckConnection.isNetworkAvailable(getApplicationContext())) {
            presenter.invokeData(tableID);
        } else {
            AlertDialogFragment dialogFragment = new AlertDialogFragment(this, "Không có internet", "Kết nối lại", resultOk -> {
                if (resultOk == Activity.RESULT_OK) {
                    presenter.invokeData(tableID);
                } else {
                    finish();
                }
            });
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            dialogFragment.show(fragmentManager, "Dialog");
        }
    }

    private void init() {
        presenter = new ListFoodCustomPresenterImpl(this, this);
        recyclerView = findViewById(R.id.recyclerViewSearch);
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_food, menu);
        MenuItem item = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setIconified(true);
        searchView.setFocusable(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //-------------------------
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void SetCountFood(int position, int quantity, FoodModel foodModel) {
        int stt = foodModel.getStt();
        if (stt != -1 && foodModel.getStatusFood() == 0) {
            presenter.UpdateOrderList(tableID, foodModel, quantity, position);
        } else {
            presenter.InsertOrderList(tableID, foodModel, quantity, position);
        }
    }

    @Override
    public void onInvokeDataSuccess() {
        onStopProcessBar();
    }

    @Override
    public void onInvokeDataFail() {
        onStopProcessBar();
        AlertDialogFragment dialogFragment = new AlertDialogFragment(this, "Lỗi tải dữ liệu", "Tải lại", resultOk -> {
            if (resultOk == Activity.RESULT_OK) {
                presenter.invokeData(tableID);
            } else {
                finish();
            }
        });
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        dialogFragment.show(fragmentManager, "Dialog");
    }

    @Override
    public void onInvokeDataPending() {
        onStartProcessBar("Chờ chút...");
    }

    @Override
    public void onStartProcessBar(String message) {
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onStopProcessBar() {
        progressDialog.dismiss();
    }


    @Override
    public void initAdapter(Context context, List<FoodModel> listData) {
        arrayFoodModel = (ArrayList<FoodModel>) listData;
        adapter = new FoodAdapter(this, arrayFoodModel);
    }

    @Override
    public void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFailSetFood() {
        Toast.makeText(this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessSetFood(FoodModel foodModel, int pos) {
        arrayFoodModel.set(pos, foodModel);
        adapter.notifyItemChanged(pos);
    }
}