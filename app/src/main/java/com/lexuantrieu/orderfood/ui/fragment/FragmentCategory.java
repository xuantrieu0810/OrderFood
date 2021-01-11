package com.lexuantrieu.orderfood.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.CategoryModel;
import com.lexuantrieu.orderfood.presenter.ListCategoryCustomPresenter;
import com.lexuantrieu.orderfood.presenter.impl.ListCategoryCustomPresenterImpl;
import com.lexuantrieu.orderfood.ui.activity.ListOrderedActivity;
import com.lexuantrieu.orderfood.ui.activity.OrderActivity;
import com.lexuantrieu.orderfood.ui.activity.SearchActivity;
import com.lexuantrieu.orderfood.ui.adapter.CategoryAdapter;
import com.lexuantrieu.orderfood.ui.adapter.listener.ItemClickListener;
import com.lexuantrieu.orderfood.ui.dialog.AlertDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentCategory extends Fragment implements ListCategoryCustomPresenter.View, ItemClickListener {

    Context mContext;
    ListCategoryCustomPresenter presenter;
    ProgressDialog progressDialog;
    int tableID = -1; //default = -1
    int billID = -1; //default = -1
    String tableName;
    RecyclerView recyclerView;
    ArrayList<CategoryModel> arrayCatModel;
    CategoryAdapter adapter;
    View viewFrag;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        billID = OrderActivity.billID;
        tableID = OrderActivity.tableID;
        tableName = OrderActivity.tableName;
    }
    public FragmentCategory() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = container.getContext();
        viewFrag = inflater.inflate(R.layout.fragment_category,container,false);
        init();
        presenter = new ListCategoryCustomPresenterImpl(this.getContext(), this);
        progressDialog = new ProgressDialog(mContext);
        arrayCatModel = new ArrayList<>();
        int spanCount = 2;//Số cột nếu thiết lập lưới đứng, số dòng nếu lưới ngang
        //int orientation = GridLayoutManager.VERTICAL;//Lưới ngang
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, spanCount);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(null);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL));
        //recyclerView.setHasFixedSize(true);//ds không thêm/ bớt
        //recyclerView.addItemDecoration(new SpacesItemDecoration(20));
        presenter.invokeData();
        return viewFrag;
    }

    private void init() {
        recyclerView = viewFrag.findViewById(R.id.recyclerViewCategory_FragCategory);
    }

    @Override
    public void onInvokeDataSuccess() {
        onStopProcessBar();
    }

    @Override
    public void onInvokeDataFail() {
        onStopProcessBar();
        AlertDialogFragment dialogFragment = new AlertDialogFragment(mContext, "Lỗi tải dữ liệu", "Tải lại", resultOk -> {
            if (resultOk == Activity.RESULT_OK) {
                presenter.invokeData();
            } else {
                getActivity().getFragmentManager().popBackStack();
            }
        });
        dialogFragment.show(getFragmentManager(), "Dialog");
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
    public void initAdapter(Context context, List<CategoryModel> listData) {
        arrayCatModel = (ArrayList<CategoryModel>) listData;
//        adapter = new CategoryAdapter(mContext, arrayCatModel);
        adapter = new CategoryAdapter(mContext, arrayCatModel, this);
    }

    @Override
    public void initRecyclerView() {
        recyclerView.setAdapter(adapter);
    }

    //------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //-------------------------
            case android.R.id.home:
                getActivity().getFragmentManager().popBackStack();
                return true;
            case R.id.itemCart:
                //check sl orderlist
                Intent intent = new Intent(mContext, ListOrderedActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view, int position, boolean isLongClick) {
        if (!isLongClick){
            Intent intent = new Intent(mContext, SearchActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("tableId",tableID);
            bundle.putString("tableName",tableName);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            Toast.makeText(mContext, ""+arrayCatModel.get(position).getName(), Toast.LENGTH_SHORT).show();
        }
    }
}

