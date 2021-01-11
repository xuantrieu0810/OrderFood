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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.FoodModel;
import com.lexuantrieu.orderfood.presenter.ListFoodCustomPresenter;
import com.lexuantrieu.orderfood.presenter.impl.ListFoodCustomPresenterImpl;
import com.lexuantrieu.orderfood.ui.activity.ListOrderedActivity;
import com.lexuantrieu.orderfood.ui.adapter.FoodAdapter;
import com.lexuantrieu.orderfood.ui.adapter.listener.FoodAdapterListener;
import com.lexuantrieu.orderfood.ui.dialog.AlertDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentCategory extends Fragment implements ListFoodCustomPresenter.View, FoodAdapterListener {

    ListFoodCustomPresenter presenter;
    ProgressDialog progressDialog;
    int tableID = -1; //default = -1
    int billID = -1; //default = -1
    String tableName;
    RecyclerView recyclerView;
    ArrayList<FoodModel> arrayFoodModel;
    FoodAdapter adapter;

    Context mContext;
    View viewFrag;
    //    ArrayList<FoodModel> arrayFood;
//    AllFoodAdapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public FragmentCategory() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = container.getContext();
        viewFrag = inflater.inflate(R.layout.fragment_all_food,container,false);
//        recyclerView = (RecyclerView) viewFrag.findViewById(R.id.recyclerViewFood_FragAllFood);
        arrayFoodModel = new ArrayList<>();
        adapter = new FoodAdapter(mContext,arrayFoodModel, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null);
//        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        init();
        presenter.invokeData(tableID);
        return viewFrag;
    }

    private void init() {
        presenter = new ListFoodCustomPresenterImpl(mContext, this);
        recyclerView = viewFrag.findViewById(R.id.recyclerViewFood_FragAllFood);
        progressDialog = new ProgressDialog(mContext);
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public void ChangeFoodQuantity(int position, int quantity, FoodModel foodModel) {
        int stt = foodModel.getStt();
        if (stt != -1 && foodModel.getStatusFood() == 0) {
            presenter.UpdateOrderList(billID, tableID, foodModel, quantity, position);
        } else {
            presenter.InsertOrderList(billID, tableID, foodModel, quantity, position);
        }
    }
//    private void SetCountFood(int position, int  quantity) {
//        MainActivity.database.SetCountFood(position,quantity,arrayFood.get(position));
//        GetListFood();
//        adapter.notifyItemChanged(position);
//    }
/*

    //----------------------------------------------------------------------------------------------
    public void InputCountFood(String foodName, final int position) {

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_input_count_food);
        final TextView txtNameFood = dialog.findViewById(R.id.txtNameFood_dial);//Title
        final EditText edtInputCount = dialog.findViewById(R.id.edtCountFood_dial);//Input
        Button btnSubmit = dialog.findViewById(R.id.buttonSubmit_dial);
        Button btnCancel = dialog.findViewById(R.id.buttonCancel_dial);
        txtNameFood.setText(foodName);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( edtInputCount.getText().toString().equals("")){}
                else {
                    int countTmp = Integer.parseInt(edtInputCount.getText().toString());
                    if(countTmp>=0 && countTmp<=99) {
                        SetCountFood(position , countTmp);

                        dialog.dismiss();
                    }
                    else Toast.makeText(mContext, "Giá trị không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
    //----------------------------------------------------------------------------------------------
    public void ClickButtonAdd(int position) {
        int countTmp = arrayFood.get(position).getCountFood();
        if(countTmp < 99) {
            countTmp++;
            SetCountFood(position , countTmp);

        }
        else
            Toast.makeText(mContext, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
    }
    //----------------------------------------------------------------------------------------------
    public void ClickButtonSub(int position) {
        int countTmp = arrayFood.get(position).getCountFood();
        if(countTmp > 0) {
            countTmp--;
            SetCountFood(position , countTmp);

        }
        else
            Toast.makeText(mContext, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
    }
    //----------------------------------------------------------------------------------------------
    public void ClickButtonChange(int position) {
        SetCountFood(position , 1);

    }
    //----------------------------------------------------------------------------------------------
    public void InputCommentFood(String foodName, final int position, String cmt) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_input_comment);
        final TextView txtNameFood = dialog.findViewById(R.id.txtNameFood_dial);//Title
        final EditText edtInputComment = dialog.findViewById(R.id.editCommentFood_dial);//Input
        Button btnSubmit = dialog.findViewById(R.id.buttonSubmit_dial);
        Button btnCancel = dialog.findViewById(R.id.buttonCancel_dial);
        txtNameFood.setText(foodName);
        edtInputComment.setText(cmt);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cmtFood = edtInputComment.getText().toString().trim();
                if(arrayFood.get(position).getStt()!=0){
                    arrayFood.get(position).setCommentFood(cmtFood);
                    MainActivity.database.InsertCommentFood(arrayFood.get(position).getStt(), cmtFood);
//                    adapter.notifyItemChanged(position);
                    dialog.dismiss();
                }
                else Toast.makeText(mContext, "Khong the cap nhat", Toast.LENGTH_SHORT).show();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
    //----------------------------------------------------------------------------------------------
*/

    @Override
    public void onInvokeDataSuccess() {
        onStopProcessBar();
    }

    @Override
    public void onInvokeDataFail() {
        onStopProcessBar();
        AlertDialogFragment dialogFragment = new AlertDialogFragment(mContext, "Lỗi tải dữ liệu", "Tải lại", resultOk -> {
            if (resultOk == Activity.RESULT_OK) {
                presenter.invokeData(tableID);
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
    public void initAdapter(Context context, List<FoodModel> listData) {
        arrayFoodModel = (ArrayList<FoodModel>) listData;
        adapter = new FoodAdapter(mContext, arrayFoodModel, (FoodAdapterListener) this);
    }

    @Override
    public void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFailSetFood() {
        Toast.makeText(mContext, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessSetFood(FoodModel foodModel, int pos) {
        arrayFoodModel.set(pos, foodModel);
        adapter.notifyItemChanged(pos);
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
    public void onResume() {
        super.onResume();
        presenter.invokeData(tableID);
        onStopProcessBar();
    }
}

