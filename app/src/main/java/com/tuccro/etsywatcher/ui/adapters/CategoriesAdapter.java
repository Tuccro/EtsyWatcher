package com.tuccro.etsywatcher.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tuccro.etsywatcher.model.Category;

import java.util.List;

/**
 * Created by tuccro on 12/20/15.
 */
public class CategoriesAdapter extends ArrayAdapter<Category> {

    public CategoriesAdapter(Context context, int resource, List<Category> objects) {
        super(context, resource, objects);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView label = new TextView(super.getContext());
        label.setText(super.getItem(position).getTitle());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(super.getContext());
        label.setText(super.getItem(position).getTitle());

        return label;
    }
}
