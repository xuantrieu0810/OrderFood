//package com.lexuantrieu.orderfood.ui.activity;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.DividerItemDecoration;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.Toast;
//
//import com.lexuantrieu.orderfood.R;
//import com.lexuantrieu.orderfood.model.OrderModel;
//import com.lexuantrieu.orderfood.ui.adapter.KitchenAdapter;
//
//import java.util.ArrayList;
//
//public class KitchenActivity extends AppCompatActivity {
//
//    RecyclerView rvFood;
//    ArrayList<OrderModel> arrayOrder;
//    KitchenAdapter adapter;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_kitchen);
//        //-----------------------------------------------
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("Nhà bếp");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Back
//        //----------------------------------------------------------
//        rvFood = (RecyclerView) findViewById(R.id.rcvKitchen);
//        arrayOrder = new ArrayList<>();
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        adapter = new KitchenAdapter(arrayOrder,this);
//        rvFood.setLayoutManager(layoutManager);
////        rvFood.setItemAnimator(null);
//        rvFood.setAdapter(adapter);
//        rvFood.addItemDecoration(new DividerItemDecoration(rvFood.getContext(), DividerItemDecoration.VERTICAL));
//
//        GetListFoodKitChen();
//    }
//
//    public void GetListFoodKitChen() {
//        String sql =    "SELECT OD.STT, OD.idBill, OD.idFood, F.nameFood, OD.quantity, TF.nameTable,  OD.Status, ifnull(OD.Comment,\"\") as Comment " +
//                " FROM Food F , OrderedDetails OD, Bill B, TableFood TF " +
//                " WHERE B.idBill = OD.idBill AND F.idFood = OD.idFood  AND B.idTable = TF.idTable AND B.statusBill = 0 AND ( OD.Status > 0  AND OD.Status < 4)";
//        Cursor cursor = MainActivity.database.GetData(sql);
//        arrayOrder.clear();
//        while (cursor.moveToNext()) {
//            arrayOrder.add(new OrderModel(
//                    cursor.getInt(0),//Stt
//                    cursor.getInt(1),//idBill
//                    cursor.getInt(2),//idFood
//                    cursor.getString(3),//nameFood
//                    cursor.getInt(4),//countFood
//                    cursor.getString(5),//nameTable
//                    cursor.getInt(6),//status
//                    cursor.getString(7)//comment
//            ));
//        }
//        adapter.notifyDataSetChanged();
//    }
//
//    public void LoadListKitChen(final int position) {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        dialog.setTitle("Thông báo hoàn tất");
//        dialog.setMessage("Thao tác này không thể khôi phục lại.\n\nXác nhận!");
//        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                MainActivity.database.QueryData("UPDATE OrderedDetails SET Status = 4 "+
//                        " WHERE STT = "+ arrayOrder.get(position).getStt());
//                MainActivity.database.QueryData("UPDATE TableFood SET status = 3 "+
//                        " WHERE idTable = (SELECT idTable FROM Bill WHERE idBill = "+ arrayOrder.get(position).getIdBill()+" AND statusBill = 0)");
//                GetListFoodKitChen();
//            }
//        });
//        dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {}
//        });
//        dialog.show();
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_in_kitchen, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//    //----------------------------------------------------------------------------------------------
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menuTutorial:
//                DialogTutorial();
//                break;
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//    private void DialogTutorial() {
//        androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(this);
//        dialog.setTitle("Hướng dẫn");
//        dialog.setMessage("\n- Các trạng thái Button"+
//                "\n\n+ Button màu đỏ: món ăn vừa được thực khách gọi." +
//                "\n+ Button màu vàng: món ăn đang được nhà bếp tiến hành chế biến."+
//                "\n+ Button màu xanh: món ăn đã được chế biến xong."+
//                "\n\n- Nhấn vào button đề chuyển các trạng thái."+
//                "\n- Nhấn và giữ để quay lại trạng thái trước đó.");
//        dialog.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        dialog.show();
//    }
//}