package com.lexuantrieu.orderfood.ui.adapter.listener;

import com.lexuantrieu.orderfood.model.FoodModel;

public interface FoodAdapterListener {

    void ChangeFoodQuantity(int pos, int quantity, FoodModel foodModel);
}
