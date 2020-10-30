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
import com.lexuantrieu.orderfood.model.Food;
import com.lexuantrieu.orderfood.ui.activity.ListFoodCustom;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

public class FoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    static String charConstraint = "";
    private static int TYPE_DEFAULT = 1;
    private static int TYPE_CUSTOM = 2;
    private ArrayList<Food> arrListFood;
    private ArrayList<Food> arrListFoodFull;
    private static ListFoodCustom mContext;

    public FoodAdapter(ListFoodCustom context,ArrayList<Food> arrListFood) {
        mContext = context;
        this.arrListFood = arrListFood;
        arrListFoodFull = new ArrayList<>(arrListFood);
    }

    @Override
    public int getItemViewType(int position) {
        if (arrListFood.get(position).getCountFood()>0) {
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
            ((FoodAdapter.ViewHolder) holder).setHolderDefault(arrListFood.get(position),position);
//            Food foods = arrListFood.get(position);
//            holder.setHolderDefault(foods,position);
        } else {
            ((FoodAdapter.ViewHolderClone) holder).setHolderCustom(arrListFood.get(position),position);
        }
    }

    @Override
    public int getItemCount() {
        return arrListFood.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            charConstraint = constraint.toString().toLowerCase().trim();
            charConstraint = ListFoodCustom.covertToString(charConstraint);
            ArrayList<Food>  filterList = new ArrayList<>();
            if(charConstraint.isEmpty()) {
                filterList.addAll(arrListFoodFull);
            } else {
                for (Food foods : arrListFoodFull) {
                    if(foods.getNameFoodNonVN().toLowerCase().contains(charConstraint)) {
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
            arrListFood.clear();
            arrListFood.addAll((Collection<? extends Food>) results.values);
            Log.d("LXT_Log","Searched item: "+arrListFood.size());
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
            btnSub =  itemView.findViewById(R.id.buttonSub_itemOrder);
            edtCount = itemView.findViewById(R.id.edtCountFood_itemOrder);
            btnCmt = itemView.findViewById(R.id.buttonNote_itemOrder);
        }
        void setHolderDefault(final Food food, final int position) {
            nameFood.setText(food.getNameFood());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            unitPrice.setText(decimalFormat.format(food.getPriceFood()));
            String urlImage = food.getImageFood();
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
                            Log.d("LXT_Error:","LoadImage: "+food.getNameFood());
                        }
                    });
            edtCount.setText(String.valueOf(food.getCountFood()));
            //--------------------------------------------------------------------------------------
            edtCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputCountFood(food.getNameFood(), position);
                }
            });
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClickButtonAdd(position);
                }
            });
            btnSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClickButtonSub(position);
                }
            });

        }
        //----------------------------------------------------------------------------------------------
        //----------------------------------------------------------------------------------------------
        private void InputCountFood(String foodName, final int position) {

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
                            arrListFood.get(position).setCountFood(countTmp);
                            SetCountFood(position , countTmp);
                            notifyItemChanged(position);
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
        private void ClickButtonAdd(int position) {
            int countTmp = arrListFood.get(position).getCountFood();
            if(countTmp < 99) {
                countTmp++;
                arrListFood.get(position).setCountFood(countTmp);
                SetCountFood(position , countTmp);
                notifyItemChanged(position);
            }
            else
                Toast.makeText(mContext, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
        }
        //----------------------------------------------------------------------------------------------
        private void ClickButtonSub(int position) {
            int countTmp = arrListFood.get(position).getCountFood();
            if(countTmp > 0) {
                countTmp--;
                arrListFood.get(position).setCountFood(countTmp);
                SetCountFood(position , countTmp);
                notifyItemChanged(position);
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

        public ViewHolderClone (View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood_itemOrder);
            nameFood = itemView.findViewById(R.id.txtFoodName_itemOrder);
            unitPrice = itemView.findViewById(R.id.txtPriceFood_itemOrder);
            btnChange = itemView.findViewById(R.id.buttonChange_itemOrder);
        }
        void setHolderCustom(final Food food, final int position) {
            nameFood.setText(food.getNameFood());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            unitPrice.setText(decimalFormat.format(food.getPriceFood()));
            String urlImage = food.getImageFood();
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
            btnChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClickButtonChange(position);
                }
            });
        }
        private void ClickButtonChange(int position) {
            arrListFood.get(position).setCountFood(1);
            SetCountFood(position , 1);
            notifyItemChanged(position);
        }
    }//end Class ViewHolderClone
    private void SetCountFood(int position, int  quantity) {
        mContext.SetCountFood(position,quantity,arrListFood.get(position));
    }
}
