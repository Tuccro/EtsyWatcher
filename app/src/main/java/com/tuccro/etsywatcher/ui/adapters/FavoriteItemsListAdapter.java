package com.tuccro.etsywatcher.ui.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.tuccro.etsywatcher.db.DBObject;
import com.tuccro.etsywatcher.model.Item;

import java.util.List;

/**
 * Created by tuccro on 12/19/15.
 */
public class FavoriteItemsListAdapter extends ItemsListAdapter {

    public FavoriteItemsListAdapter(Context context, List<Item> itemList) {
        super(context, itemList);
    }

    @Override
    protected void onDeleteClick(final Item item) {
        AlertDialog.Builder deleteItemDialog = new AlertDialog.Builder(context);
        deleteItemDialog.setTitle("Please confirm an action");
        deleteItemDialog.setMessage("Are you really want to delete this item?");

        AlertDialog.OnClickListener onDeleteClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case AlertDialog.BUTTON_POSITIVE:

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                DBObject dbObject = new DBObject(context);
                                dbObject.deleteItemFromDB(item);
                            }
                        }).start();

                        FavoriteItemsListAdapter.super.itemList.remove(item);
                        FavoriteItemsListAdapter.super.notifyDataSetChanged();
                        break;
                }
            }
        };
        deleteItemDialog.setNegativeButton(android.R.string.no, onDeleteClickListener);
        deleteItemDialog.setPositiveButton(android.R.string.yes, onDeleteClickListener);
        deleteItemDialog.show();
    }
}
