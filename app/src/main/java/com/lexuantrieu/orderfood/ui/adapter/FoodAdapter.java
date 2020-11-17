package com.lexuantrieu.orderfood.ui.adapter;

import android.app.Dialog;
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
import com.lexuantrieu.orderfood.ui.activity.ListFoodCustom;
import com.lexuantrieu.orderfood.utils.LibraryString;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
@SuppressWarnings("unchecked")
public class FoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    static String charConstraint = "";
    private static final int TYPE_DEFAULT = 1;
    private static final int TYPE_CUSTOM = 2;
    private static ArrayList<FoodModel> arrListFoodModel;
    private ArrayList<FoodModel> arrListFoodModelFull;
    private ListFoodCustom mContext;


    public FoodAdapter(ListFoodCustom context, ArrayList<FoodModel> arrListFoodModel) {
        this.mContext = context;
        this.arrListFoodModel = arrListFoodModel;
        this.arrListFoodModelFull = new ArrayList<>(arrListFoodModel);
    }

    @Override
    public int getItemViewType(int position) {
        if (arrListFoodModel.get(position).getCountFood() > 0) {
            return TYPE_DEFAULT;
        } else {
            return TYPE_CUSTOM;
        }
    }
    @NonNull
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
            ((FoodAdapter.ViewHolder) holder).setHolderDefault(arrListFoodModel.get(position), position);
        } else {
            ((FoodAdapter.ViewHolderClone) holder).setHolderCustom(arrListFoodModel.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return arrListFoodModel.size();
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



    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgFood;
        private TextView nameFood;
        private TextView unitPrice;
        private ImageButton btnAdd, btnSub, btnCmt;
        private EditText edtCount;

        public ViewHolder(View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood_itemOrder);
            nameFood = itemView.findViewById(R.id.txtFoodName_itemOrder);
            unitPrice = itemView.findViewById(R.id.txtPriceFood_itemOrder);
            btnAdd = itemView.findViewById(R.id.buttonAdd_itemOrder);
            btnSub = itemView.findViewById(R.id.buttonSub_itemOrder);
            edtCount = itemView.findViewById(R.id.edtCountFood_itemOrder);
            btnCmt = itemView.findViewById(R.id.buttonNote_itemOrder);
        }

        void setHolderDefault(final FoodModel foodModel, final int position) {
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
                            Log.d("LXT_Error:", "LoadImage: " + foodModel.getNameFood());
                        }
                    });
            edtCount.setText(String.valueOf(foodModel.getCountFood()));
            //--------------------------------------------------------------------------------------
            edtCount.setOnClickListener(v -> InputCountFood(foodModel.getNameFood(), position));
            btnAdd.setOnClickListener(v -> ClickButtonAdd(position));
            btnSub.setOnClickListener(v -> ClickButtonSub(position));


        }
        //----------------------------------------------------------------------------------------------
        private void InputCountFood(String foodName, final int position) {

            final Dialog dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.dialog_input_count_food);
            final TextView txtNameFood = dialog.findViewById(R.id.txtNameFood_dial);//Title
            final EditText edtInputCount = dialog.findViewById(R.id.edtCountFood_dial);//Input
            Button btnSubmit = dialog.findViewById(R.id.buttonSubmit_dial);
            Button btnCancel = dialog.findViewById(R.id.buttonCancel_dial);
            txtNameFood.setText(foodName);
            btnSubmit.setOnClickListener(v -> {
                if (edtInputCount.getText().toString().equals("")) {
                    edtInputCount.setError("Vui lòng nhập số lượng");
                } else {
                    int countTmp = Integer.parseInt(edtInputCount.getText().toString());
                    if (countTmp >= 0 && countTmp <= 99) {
                        SetCountFood(position, countTmp);
                        dialog.dismiss();
                    } else edtInputCount.setError("Số lượng không hợp lệ.");
                }
            });
            btnCancel.setOnClickListener(v -> dialog.cancel());
            dialog.show();
        }
        //----------------------------------------------------------------------------------------------
        private void ClickButtonAdd(int position) {
            int countTmp = arrListFoodModel.get(position).getCountFood();
            if(countTmp < 99) {
                countTmp++;
                SetCountFood(position , countTmp);
            }
            else
                Toast.makeText(mContext, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
        }
        //----------------------------------------------------------------------------------------------
        private void ClickButtonSub(int position) {
            int countTmp = arrListFoodModel.get(position).getCountFood();
            if(countTmp > 0) {
                countTmp--;
                SetCountFood(position , countTmp);
            }
            else
                Toast.makeText(mContext, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }// end Class ViewHolderDefault

    //----------------------------------------------------------------------------------------------
    public class ViewHolderClone extends RecyclerView.ViewHolder {

        private ImageView imgFood;
        private TextView nameFood;
        private TextView unitPrice;
        private ImageButton btnChange;

        public ViewHolderClone(View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood_itemOrder);
            nameFood = itemView.findViewById(R.id.txtFoodName_itemOrder);
            unitPrice = itemView.findViewById(R.id.txtPriceFood_itemOrder);
            btnChange = itemView.findViewById(R.id.buttonChange_itemOrder);
        }

        void setHolderCustom(final FoodModel foodModel, final int position) {
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
            btnChange.setOnClickListener(v -> SetCountFood(position, 1));
        }
    }//end Class ViewHolderClone


    private void SetCountFood(int position, int  quantity) {
        mContext.SetCountFood(position, quantity, arrListFoodModel.get(position));
    }
}
