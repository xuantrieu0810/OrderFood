package com.lexuantrieu.orderfood.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.OrderedModel;
import com.lexuantrieu.orderfood.presenter.KitchenActivityPresenter;
import com.lexuantrieu.orderfood.presenter.impl.KitchenActivityPresenterImpl;
import com.lexuantrieu.orderfood.ui.adapter.KitchenAdapter;
import com.lexuantrieu.orderfood.ui.adapter.listener.KitchenAdapterListener;
import com.lexuantrieu.orderfood.ui.dialog.AlertDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class KitchenActivity extends AppCompatActivity implements KitchenActivityPresenter.View , SwipeRefreshLayout.OnRefreshListener ,KitchenAdapterListener {

    KitchenActivityPresenter presenter;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    ArrayList<OrderedModel> arrayOrder;
    KitchenAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen);
        //-----------------------------------------------
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Nhà bếp");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Back
        //----------------------------------------------------------
        init();
        presenter = new KitchenActivityPresenterImpl(this, this);
        progressDialog = new ProgressDialog(this);
        arrayOrder = new ArrayList<>();



        recyclerView.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(null);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        onStartProcessBar("Đang load...");
        presenter.invokeData();
    }
    private void init() {
        recyclerView = findViewById(R.id.rcvKitchen);
        refreshLayout = findViewById(R.id.swipe_rf_kitchen);
    }


    private void DialogTutorial() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Hướng dẫn");
        dialog.setMessage("\n- Các trạng thái Button"+
                "\n\n+ Button màu đỏ: món ăn vừa được thực khách gọi." +
                "\n+ Button màu vàng: món ăn đang được nhà bếp tiến hành chế biến."+
                "\n+ Button màu xanh: món ăn đã được chế biến xong."+
                "\n\n- Nhấn vào button đề chuyển các trạng thái."+
                "\n- Nhấn và giữ để quay lại trạng thái trước đó.");
        dialog.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }

    @Override
    public void ChangeStatusFoodItem(int position, OrderedModel orderedModel, boolean isLongClick) {
        String title = !isLongClick ? "Xác nhận hoàn tất":"Hoàn tác";
        String mess = "\nBạn có chắc không?";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(mess).setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onStartProcessBar("Chờ...");
                        presenter.changeStatusOrderList(position, orderedModel , isLongClick);
                    }
                })
                .setNegativeButton("Hủy", null);
        builder.create().show();
    }

    @Override
    public void onInvokeDataSuccess(List<OrderedModel> listData) {
        arrayOrder = (ArrayList<OrderedModel>) listData;
        adapter = new KitchenAdapter(this, arrayOrder, this);
        recyclerView.setAdapter(adapter);
        onStopProcessBar();
    }

    @Override
    public void onInvokeDataFail() {
        onStopProcessBar();
        AlertDialogFragment dialogFragment = new AlertDialogFragment(this, "Lỗi tải dữ liệu", "Tải lại", resultOk -> {
            if (resultOk == Activity.RESULT_OK) {
                presenter.invokeData();
            } else {
                finish();
                onBackPressed();
            }
        });
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        dialogFragment.show(fragmentManager, "DialogListFood");
    }

    @Override
    public void onStartProcessBar(String message) {
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onStopProcessBar() {
        if (refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
        if (progressDialog.isShowing()) progressDialog.dismiss();
    }


    @Override
    public void onFailSetStatus() {
        onStopProcessBar();
        Toast.makeText(this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessSetStatus(OrderedModel orderedModel, int pos) {
        if (orderedModel.getStatus() == 4) {
            arrayOrder.remove(pos);
            adapter.notifyItemRemoved(pos);
        }
        else {
            arrayOrder.set(pos, orderedModel);
            adapter.notifyItemChanged(pos);
        }
        onStopProcessBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.invokeData();
        onStopProcessBar();
    }

    @Override
    public void onRefresh() {
        onStartProcessBar("Đang load...");
        presenter.invokeData();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_in_kitchen, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuTutorial:
                DialogTutorial();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}