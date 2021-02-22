package com.lexuantrieu.orderfood.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.OrderedModel;
import com.lexuantrieu.orderfood.ui.adapter.listener.KitchenAdapterListener;

import java.util.ArrayList;

public class KitchenAdapter extends RecyclerView.Adapter<KitchenAdapter.ViewHolder> {

    ArrayList<OrderedModel> arrayList;
    Context mContext;
    private KitchenAdapterListener listener;

    public KitchenAdapter(Context mContext, ArrayList<OrderedModel> arrayList, KitchenAdapterListener listener) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_activity_kitchen,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderedModel mFood = arrayList.get(position);
        holder.SetViewHolder(mFood,position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        ImageView imgStatus;
        TextView txtFood, txtCount, txtTable, txtComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgStatus = (ImageView) itemView.findViewById(R.id.imageView_kc);
            txtFood = itemView.findViewById(R.id.txtFood_kc);
            txtCount = itemView.findViewById(R.id.txtCount_kc);
            txtTable = itemView.findViewById(R.id.txtTable_kc);
            txtComment = itemView.findViewById(R.id.txtComment_kc);
        }
        void SetViewHolder(final OrderedModel mFood, final int position) {
            txtFood.setText(mFood.getFoodName());
            txtCount.setText("SL: "+mFood.getQuantity());
            txtTable.setText(mFood.getTableName());
            txtComment.setText("Ghi chú: "+mFood.getComment());
            switch (mFood.getStatus()) {
                case 2:
                    imgStatus.setColorFilter(Color.YELLOW);// bep dang tien hanh xu li
                    break;
                case 3:
                    imgStatus.setColorFilter(Color.GREEN);// da hoan thanh
                    break;
                default:
                    imgStatus.setColorFilter(Color.RED);
            }

            imgStatus.setOnClickListener(v -> SetStatusFood(position,mFood,false));
            imgStatus.setOnLongClickListener(v -> {
                SetStatusFood(position,mFood,true);
                return false;
            });
        }
    }

    private void SetStatusFood(int position, OrderedModel foodModel, boolean isLongClick) {
        listener.ChangeStatusFoodItem(position, foodModel, isLongClick);
    }
}
