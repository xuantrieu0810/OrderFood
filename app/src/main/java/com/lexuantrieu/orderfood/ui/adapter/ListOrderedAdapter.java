package com.lexuantrieu.orderfood.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.FoodModel;
import com.lexuantrieu.orderfood.ui.adapter.listener.FoodAdapterListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListOrderedAdapter extends BaseAdapter {
    private Context mContext;
    private int layout;
    private ArrayList<FoodModel> arrListFood;
    private FoodAdapterListener listener;

    public ListOrderedAdapter(Context context, int layout, ArrayList<FoodModel> arrListFood, FoodAdapterListener listener) {
        this.mContext = context;
        this.layout = layout;
        this.arrListFood = arrListFood;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return arrListFood == null ? 0 : arrListFood.size();
    }
    @Override
    public int getItemViewType(int position) {
        return (arrListFood.get(position).getCountFood()>0)?1:0;
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
        ViewHolder holder;
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
        FoodModel foodModel = arrListFood.get(position);
        switch (foodModel.getStatusFood()) {
            case 1:
                convertView.setBackgroundColor(Color.YELLOW);// bep dang tien hanh xu li
                break;
            case 2:
                convertView.setBackgroundColor(Color.GREEN);// da hoan thanh
                break;
        }
        holder.nameFood.setText(foodModel.getNameFood());
        holder.edtCount.setText(""+ foodModel.getCountFood());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.priceFood.setText(decimalFormat.format(foodModel.getPriceFood()*foodModel.getCountFood()));
        //------------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------------

        holder.edtCount.setOnClickListener(v -> InputCountFood(foodModel, position));
        holder.btnAdd.setOnClickListener(v -> ClickButtonAdd(foodModel, position));
        holder.btnSub.setOnClickListener(v -> ClickButtonSub(foodModel, position));
        holder.btnDel.setOnClickListener(v -> ClickButtonDel(foodModel, position));

        if(foodModel.getStatusFood()>1) {
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



    //----------------------------------------------------------------------------------------------
    private void InputCountFood(FoodModel foodModel, int position) {
        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_input_count_food);
        TextView txtNameFood = dialog.findViewById(R.id.txtNameFood_dial);//Title
        EditText edtInputCount = dialog.findViewById(R.id.edtCountFood_dial);//Input
        Button btnSubmit = dialog.findViewById(R.id.buttonSubmit_dial);
        Button btnCancel = dialog.findViewById(R.id.buttonCancel_dial);
        txtNameFood.setText(foodModel.getNameFood());
        btnSubmit.setOnClickListener(v -> {
            if (edtInputCount.getText().toString().equals("")) {
                edtInputCount.setError("Vui lòng nhập số lượng");
            } else {
                int countTmp = Integer.parseInt(edtInputCount.getText().toString());
                if (countTmp >= 0 && countTmp <= 99) {
//                        FoodModel model = new FoodModel(foodModel);
                    SetCountFood(position, foodModel , countTmp, "");
                    dialog.dismiss();
                } else edtInputCount.setError("Số lượng không hợp lệ.");
            }
        });
        btnCancel.setOnClickListener(v -> dialog.cancel());
        dialog.show();
    }
    //----------------------------------------------------------------------------------------------
    private void InputCommentFood(FoodModel foodModel, int position) {
        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_input_comment);
        TextView txtNameFood = dialog.findViewById(R.id.txtNameFood_dial);//Title
        EditText edtInputComment = dialog.findViewById(R.id.editCommentFood_dial);//Input
        Button btnSubmit = dialog.findViewById(R.id.buttonSubmit_dial);
        Button btnCancel = dialog.findViewById(R.id.buttonCancel_dial);
        txtNameFood.setText(foodModel.getNameFood());
        edtInputComment.setText(foodModel.getCommentFood());
        btnSubmit.setOnClickListener(v -> {
            String cmtFood = edtInputComment.getText().toString().trim();
            if(foodModel.getStt() !=0){
                SetCountFood(position,foodModel, foodModel.getCountFood() ,cmtFood);
                dialog.dismiss();
            }
            else Toast.makeText(mContext, "Không thể cập nhật", Toast.LENGTH_SHORT).show();
        });
        btnCancel.setOnClickListener(v -> dialog.cancel());
        dialog.show();
    }
    //----------------------------------------------------------------------------------------------
    private void ClickButtonAdd(FoodModel foodModel, int position) {
        int countTmp = foodModel.getCountFood();
        if(countTmp < 99) {
            SetCountFood(position, foodModel, countTmp+1, "");
        }
        else
            Toast.makeText(mContext, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
    }
    //----------------------------------------------------------------------------------------------
    private void ClickButtonSub(FoodModel foodModel, int position) {
        int countTmp = foodModel.getCountFood();
        if(countTmp > 0) {
            SetCountFood(position , foodModel, countTmp-1, "");
        }
        else
            Toast.makeText(mContext, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
    }
    private void ClickButtonDel(FoodModel foodModel, int position) {
        SetCountFood(position, foodModel, 0,"");
    }
    //Fucntion of adapter
    private void SetCountFood(int position, FoodModel foodModel, int countTmp ,String cmt) {
        FoodModel model = new FoodModel(foodModel);
        model.setCountFood(countTmp);
        model.setCommentFood(cmt);
        listener.ChangeFoodItem(position, model);
    }
}

