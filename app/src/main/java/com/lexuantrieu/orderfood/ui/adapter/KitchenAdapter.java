//package com.lexuantrieu.orderfood.ui.adapter;
//
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.lexuantrieu.orderfood.R;
//import com.lexuantrieu.orderfood.model.OrderModel;
//import com.lexuantrieu.orderfood.ui.activity.KitchenActivity;
//import com.lexuantrieu.orderfood.ui.activity.MainActivity;
//
//import java.util.ArrayList;
//
//public class KitchenAdapter extends RecyclerView.Adapter<KitchenAdapter.ViewHolder> {
//
//    ArrayList<OrderModel> arrayList;
//    KitchenActivity mContext;
//
//    public KitchenAdapter(ArrayList<OrderModel> arrayList, KitchenActivity mContext) {
//        this.arrayList = arrayList;
//        this.mContext = mContext;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View view = layoutInflater.inflate(R.layout.item_activity_kitchen,parent,false);
//        ViewHolder viewHolder = new ViewHolder(view);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        OrderModel foodKC = arrayList.get(position);
//        holder.SetViewHolder(foodKC,position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return arrayList.size();
//    }
//
//    public class ViewHolder  extends RecyclerView.ViewHolder{
//        ImageView imgStatus;
//        TextView txtFood, txtCount, txtTable, txtComment;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imgStatus = (ImageView) itemView.findViewById(R.id.imageView_kc);
//            txtFood = itemView.findViewById(R.id.txtFood_kc);
//            txtCount = itemView.findViewById(R.id.txtCount_kc);
//            txtTable = itemView.findViewById(R.id.txtTable_kc);
//            txtComment = itemView.findViewById(R.id.txtComment_kc);
//        }
//        void SetViewHolder(final OrderModel OrderModel, final int position) {
//            txtFood.setText(OrderModel.getNameFood());
//            txtCount.setText("SL: "+OrderModel.getQuantity());
//            txtTable.setText(OrderModel.getNameTable());
//            txtComment.setText("Ghi chú: "+OrderModel.getCommentFood());
//            switch (OrderModel.getStatusFood()) {
//                case 2:
//                    imgStatus.setColorFilter(Color.YELLOW);// bep dang tien hanh xu li
//                    break;
//                case 3:
//                    imgStatus.setColorFilter(Color.GREEN);// da hoan thanh
//                    break;
//                default:
//                    imgStatus.setColorFilter(Color.RED);
//            }
//
//            imgStatus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int sttF = OrderModel.getStatusFood();
//                    switch (sttF) {
//                        case 1://default
//                            OrderModel.setStatusFood(2);
////                            MainActivity.database.QueryData("UPDATE OrderedDetails SET Status = 2 " +
////                                    " WHERE STT = "+ OrderModel.getStt());
//                            notifyItemChanged(position);
//                            break;
//                        case 2:
//                            OrderModel.setStatusFood(3);
////                            MainActivity.database.QueryData("UPDATE OrderedDetails SET Status = 3 "+
////                                    " WHERE STT = "+ OrderModel.getStt());
//                            notifyItemChanged(position);
//                            break;
//                        case 3:
//                            mContext.LoadListKitChen(position);
//                            break;
//                    }
//                }
//            });
//
//            imgStatus.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    int sttF = OrderModel.getStatusFood();
//                    switch (sttF) {
//                        case 2://default
//                            OrderModel.setStatusFood(1);
////                            MainActivity.database.QueryData("UPDATE OrderedDetails SET Status = 1 " +
////                                    " WHERE STT = "+ OrderModel.getStt());
//                            notifyItemChanged(position);
//                            break;
//                        case 3:
//                            OrderModel.setStatusFood(2);
////                            MainActivity.database.QueryData("UPDATE OrderedDetails SET Status = 3 " +
////                                    " WHERE STT = "+ OrderModel.getStt());
//                            notifyItemChanged(position);
//                            break;
//                        case 4:
//                            OrderModel.setStatusFood(3);
////                            MainActivity.database.QueryData("UPDATE OrderedDetails SET Status = 4 " +
////                                    " WHERE STT = "+ OrderModel.getStt());
//                            mContext.GetListFoodKitChen();
//                            break;
//                        default:
//                            Toast.makeText(mContext, "KHÔNG THỂ HOÀN TÁC", Toast.LENGTH_SHORT).show();
//                    }
//                    return true;// khong tiep tuc bat su kien onClick
//                }
//            });
//        }
//    }
//}
