package com.lexuantrieu.orderfood.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.room.TableModel;
import com.lexuantrieu.orderfood.ui.activity.ListTableActivity;

import java.util.List;

public class TableAdapter extends BaseAdapter {
    private ListTableActivity context;
    private int layout;
    private List<TableModel> listTable;

    public TableAdapter(ListTableActivity context, int layout, List<TableModel> listTable) {
        this.context = context;
        this.layout = layout;
        this.listTable = listTable;
    }

    @Override
    public int getCount() {
        return listTable.size();
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
        ImageView imgTable;
        TextView nameTable;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.imgTable = (ImageView) convertView.findViewById(R.id.image_table);
            holder.nameTable = (TextView) convertView.findViewById(R.id.name_table);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TableModel table = listTable.get(position);
        holder.imgTable.setImageResource(table.getImageTable());
        holder.nameTable.setText(table.getNameTable());

        return convertView;
    }
}

