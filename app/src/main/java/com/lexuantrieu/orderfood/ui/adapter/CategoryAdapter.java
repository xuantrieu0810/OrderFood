//package com.lexuantrieu.orderfood.ui.adapter;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.lexuantrieu.orderfood.R;
//import com.lexuantrieu.orderfood.ui.activity.FoodByCatActivity;
//import com.lexuantrieu.orderfood.ui.activity.OrderActivity;
//import com.lexuantrieu.orderfood.ui.fragment.FragmentCategory;
//
//public class CategoryAdapter extends RecyclerView.Adapter {
//
//    ArrayList<Category> arrayList;
//    FragmentCategory mContext;
//    public CategoryAdapter(FragmentCategory mContext, ArrayList<Category> arrayList) {
//        this.arrayList = arrayList;
//        this.mContext = mContext;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
//        ImageView imgCategory;
//        TextView nameCategory;
//        private ItemClickListener itemClickListener; // Khai báo interface
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imgCategory = (ImageView) itemView.findViewById(R.id.imageViewCategory);
//            nameCategory = (TextView) itemView.findViewById(R.id.txtCategoryName);
//            itemView.setOnClickListener(this); // Mấu chốt ở đây , set sự kiên onClick cho View
//            itemView.setOnLongClickListener(this); // Mấu chốt ở đây , set sự kiên onLongClick cho View
//        }
//        void SetViewHolder(final Category category, int position) {
//            setItemClickListener(new ItemClickListener() {
//                @Override
//                public void onClick(View view, int position, boolean isLongClick) {
//                    if(isLongClick)
//                        Toast.makeText(mContext.getContext(), category.getNameCategory(), Toast.LENGTH_SHORT).show();
//                    else{
//                        OrderActivity.nameCategory = category.getNameCategory();
//                        OrderActivity.idCategory = category.getIdCategory();
//                        Intent intent = new Intent(mContext.getContext(), FoodByCatActivity.class);
//                        mContext.startActivity(intent);
//                    }
//
//                }
//            });
//            nameCategory.setText(category.getNameCategory());
//            //byte[] -> bitmap
//            byte[] imageCategory = category.getImageCategory();
//            Bitmap bitmap = BitmapFactory.decodeByteArray(imageCategory,0,imageCategory.length);
//            //
//            int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
//            int x = (bitmap.getWidth() - size) / 2;
//            int y = (bitmap.getHeight() - size) / 2;
//            Bitmap bitmap_Resul = Bitmap.createBitmap(bitmap, x, y, size, size);
////            if(bitmap_Resul != bitmap) bitmap_Resul.recycle();
//            //
//            imgCategory.setImageBitmap(bitmap);
//        }
//
//        public void setItemClickListener(ItemClickListener itemClickListener)
//        {
//            this.itemClickListener = itemClickListener;
//        }
//
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
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View itemView = layoutInflater.inflate(R.layout.item_category,parent,false);
//        return new ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//        ((ViewHolder)holder).SetViewHolder(arrayList.get(position),position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return arrayList.size();
//    }
//}
//
