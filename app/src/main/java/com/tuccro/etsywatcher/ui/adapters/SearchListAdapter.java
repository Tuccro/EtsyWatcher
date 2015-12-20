package com.tuccro.etsywatcher.ui.adapters;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tuccro.etsywatcher.R;
import com.tuccro.etsywatcher.model.Item;

import java.util.List;

/**
 * Created by tuccro on 12/19/15.
 */
public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    List<Item> itemList;
    Context context;

    public SearchListAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item item = itemList.get(position);

        holder.textDescribe.setText(item.getTitle());

        if (!item.getImagesUrls().isEmpty()) {
            Picasso.with(context).load(item.getImagesUrls().get(0)).into(holder.imageThumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageThumbnail;
        TextView textDescribe;

        public ViewHolder(View itemView) {
            super(itemView);

            imageThumbnail = (ImageView) itemView.findViewById(R.id.imageThumbnail);
            textDescribe = (TextView) itemView.findViewById(R.id.textDescribe);
        }
    }
}
