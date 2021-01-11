package com.lexuantrieu.orderfood.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.FoodModel;
import com.lexuantrieu.orderfood.ui.adapter.listener.FoodAdapterListener;
import com.lexuantrieu.orderfood.utils.LibraryString;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

public class FoodSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    static String charConstraint = "";

    private final int TYPE_DEFAULT = 1;
    private final int TYPE_CUSTOM = 2;

    private Context mContext;
    private static ArrayList<FoodModel> arrListFoodModel;
    private ArrayList<FoodModel> arrListFoodModelFull;
    private FoodAdapterListener listener;

    //    public FoodAdapter(Context context, ArrayList<FoodModel> arrListFoodModel) {
//        this.mContext = context;
//        this.arrListFoodModel = arrListFoodModel;
//    }
    public FoodSearchAdapter(Context context, ArrayList<FoodModel> arrListFoodModel, FoodAdapterListener listener) {
        this.mContext = context;
        this.arrListFoodModel = arrListFoodModel;
        this.arrListFoodModelFull = new ArrayList<>(arrListFoodModel);
        this.listener = listener;
    }
    @Override
    public int getItemCount() {
        return arrListFoodModel == null ? 0 : arrListFoodModel.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (arrListFoodModel.get(position).getCountFood()>0)?TYPE_DEFAULT:TYPE_CUSTOM;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if(viewType == TYPE_DEFAULT ) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_type1, parent, false);
            return new ViewHolder(itemView);
        }
        else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_type2, parent, false);
            return new ViewHolderClone(itemView);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_DEFAULT) {
            ((ViewHolder) holder).setHolderDefault(arrListFoodModel.get(position),position);
        } else {
            ((ViewHolderClone) holder).setHolderCustom(arrListFoodModel.get(position),position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgFood;
        private TextView nameFood;
        private TextView unitPrice;
        private ImageButton btnAdd, btnSub, btnCmt;
        private EditText edtCount;

        public ViewHolder(View itemView) {
            super(itemView);
            imgFood =  itemView.findViewById(R.id.imgFood_itemOrder);
            nameFood =  itemView.findViewById(R.id.txtFoodName_itemOrder);
            unitPrice =  itemView.findViewById(R.id.txtPriceFood_itemOrder);
            btnAdd =  itemView.findViewById(R.id.buttonAdd_itemOrder);
            btnSub =  itemView.findViewById(R.id.buttonSub_itemOrder);
            edtCount =  itemView.findViewById(R.id.edtCountFood_itemOrder);
            btnCmt =  itemView.findViewById(R.id.buttonNote_itemOrder);
        }
        void setHolderDefault(FoodModel foodModel, int position) {
            nameFood.setText(foodModel.getNameFood());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            unitPrice.setText(decimalFormat.format(foodModel.getPriceFood()));
            edtCount.setText(String.valueOf(foodModel.getCountFood()));
            String urlImage = foodModel.getImageFood();
            Picasso.get()
                    .load(urlImage)
                    .placeholder(R.drawable.imagepreview)
                    .error(R.drawable.ic_sync_error)
                    .into(imgFood, new Callback() {
                        @Override
                        public void onSuccess() { }
                        @Override
                        public void onError(Exception e) {
                            Log.d("LXT_Error:", "LoadImage: " + foodModel.getNameFood());
                        }
                    });
            //Button Click event
            edtCount.setOnClickListener(v -> InputCountFood(foodModel, position));
            btnAdd.setOnClickListener(v -> ClickButtonAdd(foodModel, position));
            btnSub.setOnClickListener(v -> ClickButtonSub(foodModel, position));
            btnCmt.setOnClickListener(v -> InputCommentFood(foodModel, position));
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
                        foodModel.setCountFood(countTmp);
                        SetCountFood(position, foodModel);
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
                    foodModel.setCommentFood(cmtFood);
                    SetCountFood(position,foodModel);
                    dialog.dismiss();
                }
                else Toast.makeText(mContext, "Không thể cập nhật", Toast.LENGTH_SHORT).show();
            });
            btnCancel.setOnClickListener(v -> dialog.cancel());
            dialog.show();
        }
        //----------------------------------------------------------------------------------------------
        private void ClickButtonAdd(FoodModel foodModel, int position) {
            int countTmp = arrListFoodModel.get(position).getCountFood();
            if(countTmp < 99) {
                foodModel.setCountFood(countTmp+1);
                SetCountFood(position, foodModel);
            }
            else
                Toast.makeText(mContext, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
        }
        //----------------------------------------------------------------------------------------------
        private void ClickButtonSub(FoodModel foodModel, int position) {
            int countTmp = arrListFoodModel.get(position).getCountFood();
            if(countTmp > 0) {
                foodModel.setCountFood(countTmp-1);
                SetCountFood(position , foodModel);
            }
            else
                Toast.makeText(mContext, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    public class ViewHolderClone extends RecyclerView.ViewHolder {

        private ImageView imgFood;
        private TextView nameFood;
        private TextView unitPrice;
        private ImageButton btnChange;

        public ViewHolderClone (View itemView) {
            super(itemView);
            imgFood =  itemView.findViewById(R.id.imgFood_itemOrder);
            nameFood = itemView.findViewById(R.id.txtFoodName_itemOrder);
            unitPrice = itemView.findViewById(R.id.txtPriceFood_itemOrder);
            btnChange = itemView.findViewById(R.id.buttonChange_itemOrder);
        }
        void setHolderCustom(FoodModel foodModel, int position) {
            nameFood.setText(foodModel.getNameFood());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            unitPrice.setText(decimalFormat.format(foodModel.getPriceFood()));
            String urlImage = foodModel.getImageFood();
            Picasso.get()
                    .load(urlImage)
                    .placeholder(R.drawable.imagepreview)
                    .error(R.drawable.ic_sync_error)
                    .into(imgFood, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
            //--------------------------------------------------------------------------------------

            btnChange.setOnClickListener(v -> {

                        ClickCreate(position, foodModel);
                    }
            );
        }

        private void ClickCreate(int position, FoodModel foodModel) {
            foodModel.setCountFood(1);
            SetCountFood(position,foodModel);
        }
    }
    
    //Fucntion of adapter
    private void SetCountFood(int position, FoodModel foodModel) {
        listener.ChangeFoodQuantity(position, foodModel);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            charConstraint = constraint.toString().toLowerCase().trim();
            charConstraint = LibraryString.covertStringToVN(charConstraint);
            ArrayList<FoodModel> filterList = new ArrayList<>();
            if(charConstraint.isEmpty()) {
                filterList.addAll(arrListFoodModelFull);
            } else {
                for (FoodModel foods : arrListFoodModelFull) {
                    if (foods.getNameFoodNonVN().toLowerCase().contains(charConstraint)) {
                        filterList.add(foods);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrListFoodModel.clear();
            arrListFoodModel.addAll((Collection<? extends FoodModel>) results.values);
            Log.d("LXT_Log", "Searched item: " + arrListFoodModel.size());
            notifyDataSetChanged();
        }
    };
}
