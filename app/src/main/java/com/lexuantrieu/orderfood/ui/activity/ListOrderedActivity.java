package com.lexuantrieu.orderfood.ui.activity;
//
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.lexuantrieu.orderfood.R;
//import com.lexuantrieu.orderfood.model.FoodModel;
//import com.lexuantrieu.orderfood.ui.adapter.ListOrderedAdapter;
//
//import java.util.ArrayList;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.FoodModel;
import com.lexuantrieu.orderfood.ui.adapter.ListOrderedAdapter;

import java.util.ArrayList;

public class ListOrderedActivity extends AppCompatActivity {

    String tableName;
    int tableId=-1, billId =-1;
    TextView txtTotal;
    ListView lvCart;
    ArrayList<FoodModel> arrayCart;
    ListOrderedAdapter cartAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //-----------------------------------------------------------
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tableName = bundle.getString("tableName");
            tableId= bundle.getInt("tableId");
            billId = bundle.getInt("billId");
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
        lvCart = findViewById(R.id.listViewCart);
        txtTotal = findViewById(R.id.txtTotalCart);
        arrayCart = new ArrayList<>();
        cartAdapter = new ListOrderedAdapter(this,R.layout.item_list_ordered,arrayCart);
        lvCart.setAdapter(cartAdapter);
//        GetListInfoBill();
    }//end of onCreate
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
//        cartAdapter.notifyDataSetChanged();
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
//        View view = cartAdapter.getView(position,null,null);
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
//        View view = cartAdapter.getView(position,null,null);
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
