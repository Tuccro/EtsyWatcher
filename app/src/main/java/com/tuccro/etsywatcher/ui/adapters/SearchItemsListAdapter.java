package com.tuccro.etsywatcher.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.tuccro.etsywatcher.model.Item;
import com.tuccro.etsywatcher.ui.DetailsActivity;

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

    @Override
    protected void onCardClick(Item item) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(DetailsActivity.ATTR_ITEM, item);
        context.startActivity(intent);
    }
}