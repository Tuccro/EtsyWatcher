package com.tuccro.etsywatcher.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tuccro.etsywatcher.R;
import com.tuccro.etsywatcher.model.Item;
import com.tuccro.etsywatcher.ui.DetailsActivity;

import java.util.List;

/**
 * Created by tuccro on 12/19/15.
 */
public class SavedListAdapter extends RecyclerView.Adapter<SavedListAdapter.ViewHolder> {

    List<Item> itemList;
    Context context;

    public SavedListAdapter(Context context, List<Item> itemList) {
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
            Picasso.with(context).load(item.getImagesUrls().get(0))
                    .centerCrop().resize(300, 300).into(holder.imageThumbnail);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(DetailsActivity.ATTR_ITEM, item);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView imageThumbnail;
        TextView textDescribe;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardView);
            imageThumbnail = (ImageView) itemView.findViewById(R.id.imageThumbnail);
            textDescribe = (TextView) itemView.findViewById(R.id.textDescribe);
        }
    }
}
