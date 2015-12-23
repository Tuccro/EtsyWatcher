package com.tuccro.etsywatcher.ui.adapters;

import android.content.Context;

import com.tuccro.etsywatcher.model.Item;

import java.util.List;

/**
 * Created by tuccro on 12/19/15.
 */
public class SearchItemsListAdapter extends ItemsListAdapter {

    public SearchItemsListAdapter(Context context, List<Item> itemList) {
        super(context, itemList);
    }

    @Override
    protected void onDeleteClick(Item item) {
        super.itemList.remove(item);
        super.notifyDataSetChanged();
    }
}