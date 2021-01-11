package com.lexuantrieu.orderfood.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.CategoryModel;
import com.lexuantrieu.orderfood.ui.adapter.listener.ItemClickListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter {

    ArrayList<CategoryModel> arrayList;
    Context mContext;
    ItemClickListener itemClickListener; // Khai báo interface
//    public CategoryAdapter(Context mContext, ArrayList<CategoryModel> arrayList) {
//        this.arrayList = arrayList;
//        this.mContext = mContext;
//    }
    public CategoryAdapter(Context mContext, ArrayList<CategoryModel> arrayList, ItemClickListener itemClickListener) {
        this.arrayList = arrayList;
        this.mContext = mContext;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_category,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((ViewHolder)holder).SetViewHolder(arrayList.get(position),position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCategory;
        TextView nameCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategory = (ImageView) itemView.findViewById(R.id.imageViewCategory);
            nameCategory = (TextView) itemView.findViewById(R.id.txtCategoryName);
            itemView.setOnClickListener(v -> {
                initializeItemClickListener(v,getAdapterPosition(),false);
            });
            itemView.setOnLongClickListener(v -> {
                initializeItemClickListener(v,getAdapterPosition(),true);
                return false;
            });
        }
        void SetViewHolder(CategoryModel category, int position) {
            nameCategory.setText(category.getName());
            String urlImage = category.getImage();
            Picasso.get()
                    .load(urlImage)
                    .placeholder(R.drawable.imagepreview)
                    .error(R.drawable.ic_sync_error)
                    .into(imgCategory, new Callback() {
                        @Override
                        public void onSuccess() { }
                        @Override
                        public void onError(Exception e) {
                            Log.d("LXT_Error:", "LoadImage: " + category.getImage());
                        }
                    });



//            setItemClickListener(new ItemClickListener() {
//                @Override
//                public void onClick(View view, int position, boolean isLongClick) {
//                    if(isLongClick)
//                        Toast.makeText(mContext, category.getName(), Toast.LENGTH_SHORT).show();
//                    else{
//                        OrderActivity.nameCategory = category.getName();
//                        OrderActivity.idCategory = category.getId();
//                        Intent intent = new Intent(mContext, FoodByCatActivity.class);
//                        mContext.startActivity(intent);
//                    }
//
//                }
//            });
        }
//        public void setItemClickListener(ItemClickListener itemClickListener,View v, int pos, boolean isLongClick)
//        {
//            itemClickListener = itemClickListener;
//        }


//        @Override
//        public void onClick(View v) {
//            itemClickListener.onClick(v,getAdapterPosition(),false);
//        }
//
//        @Override
//        public boolean onLongClick(View v) {
//            itemClickListener.onClick(v,getAdapterPosition(),true);
//            return true;
//        }
    }
    public void initializeItemClickListener(View v, int pos, boolean isLongClick)
    {
        itemClickListener.onClick(v,pos,isLongClick);
    }
}

