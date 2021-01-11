package com.lexuantrieu.orderfood.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.FoodModel;
import com.lexuantrieu.orderfood.ui.activity.ListOrderedActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ListOrderedAdapter extends BaseAdapter {
    private ListOrderedActivity mContext;
    private int layout;
    private List<FoodModel> listFood;

    public ListOrderedAdapter(ListOrderedActivity context, int layout, ArrayList<FoodModel> listFood) {
        this.mContext = context;
        this.layout = layout;
        this.listFood = listFood;
    }

    @Override
    public int getCount() {
        return listFood.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {

        TextView nameFood;
        TextView priceFood;
        ImageButton btnAdd, btnSub, btnDel;
        EditText edtCount;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ListOrderedAdapter.ViewHolder holder;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);

            holder = new ListOrderedAdapter.ViewHolder();
            holder.nameFood =  convertView.findViewById(R.id.txtFoodName_SubCart);
            holder.priceFood =  convertView.findViewById(R.id.txtPrice_SubCart);
            holder.btnAdd =  convertView.findViewById(R.id.buttonAdd_SubCart);
            holder.btnSub = convertView.findViewById(R.id.buttonSub_SubCart);
            holder.btnDel = convertView.findViewById(R.id.buttonDel_SubCart);
            holder.edtCount =  convertView.findViewById(R.id.editTextCount_SubCart);
            convertView.setTag(holder);
        } else {
            holder = (ListOrderedAdapter.ViewHolder) convertView.getTag();
        }
        final FoodModel food = listFood.get(position);
        switch (food.getStatusFood()) {
            case 2:
                convertView.setBackgroundColor(Color.YELLOW);// bep dang tien hanh xu li
                break;
            case 3:
                convertView.setBackgroundColor(Color.GREEN);// da hoan thanh
                break;
        }
        holder.nameFood.setText(food.getNameFood());
        holder.edtCount.setText(""+ food.getCountFood());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.priceFood.setText(decimalFormat.format(food.getPriceFood()*food.getCountFood()));
        //------------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------------

//        holder.edtCount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mContext.InputCountFood(food.getNameFood(), position);
//            }
//        });
//        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mContext.ClickButtonAdd(position);
//            }
//        });
//        holder.btnSub.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mContext.ClickButtonSub(position);
//            }
//        });
//        holder.btnDel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mContext.ClickButtonDel(position);
//            }
//        });
        if(food.getStatusFood()>1) {
            holder.edtCount.setFocusable(false);
            holder.edtCount.setFocusableInTouchMode(true);
            holder.edtCount.setEnabled(false);
            holder.edtCount.setCursorVisible(false);
            holder.edtCount.setKeyListener(null);
//            holder.edtCount.setBackgroundColor(Color.TRANSPARENT);

            holder.btnAdd.setVisibility(View.INVISIBLE);
            holder.btnSub.setVisibility(View.INVISIBLE);
            holder.btnDel.setVisibility(View.INVISIBLE);
        }
        //------------------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------
        return convertView;
    }
}

