package com.lexuantrieu.orderfood.ui.fragment;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private final int mSpace;

    public SpacesItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect,
                               View view,
                               RecyclerView parent, RecyclerView.State state) {

        outRect.left = mSpace;
        outRect.bottom = mSpace;
        // Add top margin only for the first item to avoid double space between items
//        if (parent.getChildAdapterPosition(view) <= 2)
        outRect.top = mSpace;
//        if (parent.getChildAdapterPosition(view) %2 != 0)
        outRect.right = mSpace;
    }
}
