package com.lexuantrieu.orderfood.ui.adapter.listener;

import com.lexuantrieu.orderfood.model.OrderedModel;

public interface KitchenAdapterListener {

    void ChangeStatusFoodItem(int pos, OrderedModel foodModel, boolean isClick);
}
