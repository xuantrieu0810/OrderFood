//package com.lexuantrieu.orderfood.ui.adapter;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.lexuantrieu.orderfood.R;
//import com.lexuantrieu.orderfood.ui.fragment.FragmentPopular;
//
//public class PopularAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private static int TYPE_DEFAULT = 1;
//    private static int TYPE_CUSTOM = 2;
//    private ArrayList<Foods> arrListFood;
//    private static FragmentPopular mContext;
//
//    public PopularAdapter(FragmentPopular context, ArrayList<Foods> arrListFood) {
//        this.mContext = context;
//        this.arrListFood = arrListFood;
//    }
//
//    @Override
//    public int getItemCount() {
//        return arrListFood == null ? 0 : arrListFood.size();
//    }
//    @Override
//    public int getItemViewType(int position) {
//        if (arrListFood.get(position).getCountFood()>0) {
//            return TYPE_DEFAULT;
//        } else {
//            return TYPE_CUSTOM;
//        }
//    }
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView;
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        if(viewType == TYPE_DEFAULT ) {
//            itemView = layoutInflater.inflate(R.layout.item_food_type1,parent,false);
//            return new ViewHolder(itemView);
//        }
//        else {
//            itemView = layoutInflater.inflate(R.layout.item_food_type2,parent,false);
//            return new ViewHolderClone(itemView);
//        }
//    }
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        if (getItemViewType(position) == TYPE_DEFAULT) {
//            ((ViewHolder) holder).setHolderDefault(arrListFood.get(position),position);
//        } else {
//            ((ViewHolderClone) holder).setHolderCustom(arrListFood.get(position),position);
//        }
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        private ImageView imgFood;
//        private TextView nameFood;
//        private TextView unitPrice;
//        private ImageButton btnAdd, btnSub, btnCmt;
//        private EditText edtCount;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            imgFood =  itemView.findViewById(R.id.imgFood_itemOrder);
//            nameFood =  itemView.findViewById(R.id.txtFoodName_itemOrder);
//            unitPrice =  itemView.findViewById(R.id.txtPriceFood_itemOrder);
//            btnAdd =  itemView.findViewById(R.id.buttonAdd_itemOrder);
//            btnSub =  itemView.findViewById(R.id.buttonSub_itemOrder);
//            edtCount = itemView.findViewById(R.id.edtCountFood_itemOrder);
//            btnCmt =  itemView.findViewById(R.id.buttonNote_itemOrder);
//        }
//        void setHolderDefault(final Foods food, final int position) {
//            nameFood.setText(food.getNameFood());
//            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
//            unitPrice.setText(decimalFormat.format(food.getPriceFood()));
//            //byte[] -> bitmap
//            byte[] imageFood = food.getImageFood();
//            Bitmap bitmap = BitmapFactory.decodeByteArray(imageFood,0,imageFood.length);
//            //
//            int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
//            int x = (bitmap.getWidth() - size) / 2;
//            int y = (bitmap.getHeight() - size) / 2;
//            Bitmap bitmap_Resul = Bitmap.createBitmap(bitmap, x, y, size, size);
////            if(bitmap_Resul != bitmap) bitmap_Resul.recycle();
//            //
//            imgFood.setImageBitmap(bitmap);
//            edtCount.setText(food.getCountFood()+"");
//            //--------------------------------------------------------------------------------------
//            edtCount.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mContext.InputCountFood(food.getNameFood(), position);
//                }
//            });
//            btnAdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mContext.ClickButtonAdd(position);
//                }
//            });
//            btnSub.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mContext.ClickButtonSub(position);
//                }
//            });
//            btnCmt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mContext.InputCommentFood(food.getNameFood(), position, food.getCommentFood());
//                }
//            });
//        }
//    }
//    public static class ViewHolderClone extends RecyclerView.ViewHolder {
//
//        private ImageView imgFood;
//        private TextView nameFood;
//        private TextView unitPrice;
//        private ImageButton btnChange;
//
//        public ViewHolderClone (View itemView) {
//            super(itemView);
//            imgFood =  itemView.findViewById(R.id.imgFood_itemOrder);
//            nameFood =  itemView.findViewById(R.id.txtFoodName_itemOrder);
//            unitPrice = itemView.findViewById(R.id.txtPriceFood_itemOrder);
//            btnChange = itemView.findViewById(R.id.buttonChange_itemOrder);
//
//        }
//        void setHolderCustom(final Foods food, final int position) {
//            nameFood.setText(food.getNameFood());
//            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
//            unitPrice.setText(decimalFormat.format(food.getPriceFood()));
//            //byte[] -> bitmap
//            byte[] imageFood = food.getImageFood();
//            Bitmap bitmap = BitmapFactory.decodeByteArray(imageFood,0,imageFood.length);
//            imgFood.setImageBitmap(bitmap);
//            //--------------------------------------------------------------------------------------
//            btnChange.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mContext.ClickButtonChange(position);
//                }
//            });
//        }
//    }
//}