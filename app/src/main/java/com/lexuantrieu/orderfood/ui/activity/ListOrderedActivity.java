package com.lexuantrieu.orderfood.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.FoodModel;
import com.lexuantrieu.orderfood.presenter.ListFoodCustomPresenter;
import com.lexuantrieu.orderfood.presenter.impl.ListFoodCustomPresenterImpl;
import com.lexuantrieu.orderfood.ui.adapter.ListOrderedAdapter;
import com.lexuantrieu.orderfood.ui.adapter.listener.FoodAdapterListener;
import com.lexuantrieu.orderfood.ui.dialog.AlertDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class ListOrderedActivity extends AppCompatActivity implements ListFoodCustomPresenter.View, FoodAdapterListener, SwipeRefreshLayout.OnRefreshListener {

    ListFoodCustomPresenter presenter;
    ProgressDialog progressDialog;
    SwipeRefreshLayout refreshLayout;
    String tableName;
    int tableID=-1, billID =-1;
    TextView txtTotal;
    ListView listViewCart;
    ArrayList<FoodModel> arrayCart;
    ListOrderedAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //-----------------------------------------------------------
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tableName = bundle.getString("tableName");
            tableID= bundle.getInt("tableId");
            billID = bundle.getInt("billId");
            if(tableID==-1|| billID==-1){

                Log.d("LXT_Log", "table_id: "+tableID+"- bill_id: "+billID);
                finish();
                Toast.makeText(this, "Xảy ra lỗi.", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            finish();
            Toast.makeText(this, "Xảy ra lỗi.", Toast.LENGTH_SHORT).show();
            return;
        }
        setContentView(R.layout.activity_list_ordered);
        //-----------------------------------------------------------
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Danh sách món đã đặt");
        actionBar.setSubtitle(tableName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Back
        //----------------------------------------------------------
//        init();
        presenter = new ListFoodCustomPresenterImpl(this, this);
        refreshLayout = findViewById(R.id.swipe_rf_list_ordered);
        progressDialog = new ProgressDialog(this);
        listViewCart = findViewById(R.id.listViewCart);
        txtTotal = findViewById(R.id.txtTotalCart);
        arrayCart = new ArrayList<>();
        adapter = new ListOrderedAdapter(this, R.layout.item_list_ordered, arrayCart, this);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        //---
        onStartProcessBar("Đang load...");
        presenter.invokeData(tableID,-3);

//        GetListInfoBill();
    }//end of onCreate

    @Override
    public void onInvokeDataSuccess() {
        onStopProcessBar();
    }

    @Override
    public void onInvokeDataFail() {
        onStopProcessBar();
        AlertDialogFragment dialogFragment = new AlertDialogFragment(this, "Lỗi tải dữ liệu", "Tải lại", resultOk -> {
            if (resultOk == Activity.RESULT_OK) {
                presenter.invokeData(tableID,-3);
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
        if(refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
        if(progressDialog.isShowing()) progressDialog.dismiss();
    }

    @Override
    public void initAdapter(Context context, List<FoodModel> listData) {
        arrayCart = (ArrayList<FoodModel>) listData;
        adapter = new ListOrderedAdapter(this, R.layout.item_list_ordered, arrayCart, this);
    }

    @Override
    public void initRecyclerView() {
        listViewCart.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFailSetFood() {
        onStopProcessBar();
        Toast.makeText(this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessSetFood(FoodModel foodModel, int pos) {
        if(foodModel.getCountFood()==0)
            arrayCart.clear();
        arrayCart.set(pos, foodModel);
        adapter.notifyDataSetChanged();
        onStopProcessBar();
    }

    //------------------------------------------------------------------------------------
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
    @Override
    protected void onResume() {
        super.onResume();
        presenter.invokeData(tableID,-3);
        onStopProcessBar();
    }

    @Override
    public void onRefresh() {
        presenter.invokeData(tableID,-3);
    }

    @Override
    public void ChangeFoodItem(int position, FoodModel foodModel) {
        int stt = foodModel.getStt();
        int quantity = foodModel.getCountFood();
        int status = foodModel.getStatusFood();
        if (stt != -1 && status == 0) {
            if(quantity == 0){
                AlertDialogFragment dialogFragment = new AlertDialogFragment(this, "Hủy chọn món đã đặt", "[ "+foodModel.getNameFood()+"]"+
                        "\nBạn có chắc không?", resultOk -> {
                    if (resultOk == Activity.RESULT_OK) {
                        onStartProcessBar("Đang xóa...");
                        presenter.UpdateOrderList(billID, tableID, position, foodModel);
                    } else {
                        getSupportFragmentManager().popBackStack();
                    }
                });
                dialogFragment.show(getSupportFragmentManager(), "Dialog");
            } else {
                presenter.UpdateOrderList(billID, tableID, position, foodModel);
            }
        } else {
            presenter.InsertOrderList(billID, tableID, position, foodModel);
        }
    }

//
//    private void CheckOut() {
//        if(arrayCart.size()>0) {
//            onBackPressed();
//        }
//        else {
//            MainActivity.database.QueryData("UPDATE TableFood SET Status = 1 WHERE idTable = " + ListTableActivity.idTableClick);
//            Toast.makeText(ListOrderedActivity.this, "VUI LÒNG CHỌN MÓN", Toast.LENGTH_SHORT).show();
//            onBackPressed();
//        }
//    }
//    //----------------------------------------------------------------------------------------------
//
//    private void GetListInfoBill() {
//        String sql =    "SELECT OD.Stt, F.idFood, F.nameFood, F.priceFood,  " +
//                "ifnull(OD.Quantity,0) as Quantity , OD.Status " +
//                "FROM Food F , OrderedDetails OD " +
//                "WHERE F.IdFood = OD.IdFood " +
//                "AND OD.IdBill = "+ListTableActivity.idBill+" AND Status >0";
//        Cursor cursor = MainActivity.database.GetData(sql);
//        arrayCart.clear();
//        while (cursor.moveToNext()) {
//            arrayCart.add(new Foods(
//                    cursor.getInt(0),//Stt
//                    cursor.getInt(1),//idFood
//                    cursor.getString(2),//nameFood
//                    cursor.getDouble(3),//priceFood
//                    null,//imageFood
//                    cursor.getInt(4),//quantity
//                    null,//comment
//                    cursor.getInt(5)//Status
//            ));
//        }
//        SetTotalPrice();
//        adapter.notifyDataSetChanged();
//    }
//
//    private void SetTotalPrice() {
//        int size = arrayCart.size();
//        Double sum = 0.0;
//        for(int i = 0; i< size; i++) {
//            sum += ((arrayCart.get(i).getCountFood()* arrayCart.get(i).getPriceFood()));
//        }
//        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
//        txtTotal.setText(decimalFormat.format(sum)+" vnđ");
//    }
//    //----------------------------------------------------------------------------------------------
//    private void ChangeOrdered(int position, int  quantity) {
//        String sql;
//        if(quantity == 0){
//            sql = "DELETE FROM OrderedDetails WHERE Stt = " + arrayCart.get(position).getStt() ;
//        }
//        else {
//            int idBill = OrderActivity.idBill, idFood = arrayCart.get(position).getIdFood();
//            sql = "UPDATE  OrderedDetails SET Quantity = " + quantity + " WHERE Stt = " + arrayCart.get(position).getStt() ;
//        }
//        MainActivity.database.QueryData(sql);
//        GetListInfoBill();
//    }
//    //----------------------------------------------------------------------------------------------
//    public void InputCountFood(String foodName, final int position) {
//        final Dialog dialog = new Dialog(ListOrderedActivity.this);
//        dialog.setContentView(R.layout.dialog_input_count_food);
//        final TextView txtNameFood = dialog.findViewById(R.id.txtNameFood_dial);//Title
//        final EditText edtInputCount = dialog.findViewById(R.id.edtCountFood_dial);//Input
//        Button btnSubmit = dialog.findViewById(R.id.buttonSubmit_dial);
//        final Button btnCancel = dialog.findViewById(R.id.buttonCancel_dial);
//        txtNameFood.setText(foodName);
//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if( edtInputCount.getText().toString().equals("")){}
//                else {
//                    int countTmp = Integer.parseInt(edtInputCount.getText().toString());
//                    if(countTmp>=0 && countTmp<=99) {
//                        ChangeOrdered(position , countTmp);
//                        dialog.dismiss();
//                    }
//                    else Toast.makeText(ListOrderedActivity.this, "Giá trị không hợp lệ", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.cancel();
//            }
//        });
//        dialog.show();
//    }
//    //----------------------------------------------------------------------------------------------
//    public void ClickButtonAdd(int position) {
//        View view = adapter.getView(position,null,null);
//        EditText edtCount = view.findViewById(R.id.editTextCount_SubCart);
//        int countTmp = Integer.parseInt(edtCount.getText().toString());
//        if(countTmp < 99) {
//            countTmp++;
//            ChangeOrdered(position , countTmp);
//        }
//        else
//            Toast.makeText(ListOrderedActivity.this, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
//    }
//    //----------------------------------------------------------------------------------------------
//    public void ClickButtonSub(int position) {
//        View view = adapter.getView(position,null,null);
//        EditText edtCount = view.findViewById(R.id.editTextCount_SubCart);
//        int countTmp = Integer.parseInt(edtCount.getText().toString());
//        if(countTmp > 0) {
//            countTmp--;
//            ChangeOrdered(position , countTmp);
//        }
//        else
//            Toast.makeText(ListOrderedActivity.this, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
//    }
//    //----------------------------------------------------------------------------------------------
//    public void ClickButtonDel(final int position) {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        dialog.setTitle("Thông báo");
//        dialog.setMessage("Bạn có thật sự muốn xoá không?");
//        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String sql = "DELETE FROM OrderedDetails WHERE Stt = " + arrayCart.get(position).getStt() + " AND Status = 1";
//                MainActivity.database.QueryData(sql);
//                GetListInfoBill();
//            }
//        });
//        dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {}
//        });
//        dialog.show();
//    }
//    //----------------------------------------------------------------------------------------------
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId())
//        {
//            case android.R.id.home:
//                CheckOut();
//                onBackPressed();
//                return true;
//            default:break;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
