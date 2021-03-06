package com.lexuantrieu.orderfood.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.lexuantrieu.orderfood.ui.fragment.FragmentAllFood;
import com.lexuantrieu.orderfood.ui.fragment.FragmentCategory;
import com.lexuantrieu.orderfood.ui.fragment.FragmentPopular;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private static final String[] TAB_TITLES = {"Phổ biến", "Danh mục", "Tất cả"};

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fr;
        switch (position) {
            case 0:
                fr = new FragmentPopular();
                break;
            case 1:
                fr = new FragmentCategory();
                break;
            case 2:
                fr = new FragmentAllFood();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }
        return fr;
//        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    //    private static final String[] TAB_TITLES = {"Phổ biến", "Danh mục", "Tất cả"};
//    private final Context mContext;
//
//    public ViewPagerAdapter(Context context, FragmentManager fm) {
//        super(fm);
//        mContext = context;
//    }
//
//    // This determines the fragment for each tab
//    @Override
//    public Fragment getItem(int position) {
//        Fragment fr;
//        switch (position) {
//            case 0:
//                fr = new FragmentPopular();
//                break;
//            case 1:
//                fr = new FragmentCategory();
//                break;
//            case 2:
//                fr = new FragmentAllFood();
//                break;
//            default:
//                throw new IllegalStateException("Unexpected value: " + position);
//        }
//        return fr;
//    }
//
//    @Override
//    public int getCount() {
//        return 3;
//    }
//
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }


}