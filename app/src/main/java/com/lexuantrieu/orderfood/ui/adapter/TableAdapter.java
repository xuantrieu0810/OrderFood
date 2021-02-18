package com.lexuantrieu.orderfood.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.TableModel;
import com.lexuantrieu.orderfood.ui.activity.ListTableActivity;

import java.util.List;

public class TableAdapter extends BaseAdapter {
    private ListTableActivity context;
    private LayoutInflater layoutInflater;
    private List<TableModel> listTable;

    public TableAdapter(ListTableActivity context, List<TableModel> listTable) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_table_cell, null);
            holder.imgTable = (ImageView) convertView.findViewById(R.id.image_table);
            holder.nameTable = (TextView) convertView.findViewById(R.id.name_table);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TableModel table = listTable.get(position);
        holder.imgTable.setImageResource(table.getImage());
        holder.nameTable.setText(table.getName());

        return convertView;
    }

    private class ViewHolder {
        ImageView imgTable;
        TextView nameTable;
    }
}

