package com.tuccro.etsywatcher.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tuccro.etsywatcher.R;
import com.tuccro.etsywatcher.db.DBObject;
import com.tuccro.etsywatcher.model.Item;

/**
 * Created by tuccro on 12/22/15.
 */
public class DetailsActivity extends AppCompatActivity {

    public static final String ATTR_ITEM = "Item";

    Item item;

    LinearLayout layoutAddToFavorites;
    LinearLayout layoutDeleteFromFavorites;
    LinearLayout layoutImages;

    TextView textTitle;
    TextView textPrice;
    TextView textDescribe;

    Handler dbHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        initToolbar();

        item = (Item) getIntent().getSerializableExtra(ATTR_ITEM);

        initViews();
        fillViews();

        dbHandler = new Handler();
    }

    private void initViews() {

        layoutImages = (LinearLayout) findViewById(R.id.layoutImages);
        layoutAddToFavorites = (LinearLayout) findViewById(R.id.layoutAddToFavorites);
        layoutDeleteFromFavorites = (LinearLayout) findViewById(R.id.layoutDeleteFromFavorites);

        textTitle = (TextView) findViewById(R.id.textTitle);
        textDescribe = (TextView) findViewById(R.id.textDescribe);
        textPrice = (TextView) findViewById(R.id.textPrice);

        layoutAddToFavorites.setOnClickListener(onFavoritesClickListener);
        layoutDeleteFromFavorites.setOnClickListener(onFavoritesClickListener);
    }

    private void fillViews() {

        for (String s : item.getImagesUrls()) {
            layoutImages.addView(insertPhoto(s));
        }

        textTitle.setText(item.getTitle());
        textPrice.setText(String.valueOf(item.getPrice()));
        textDescribe.setText(item.getDescription());

        setIsInFavorites(isItemInDb(item));
    }

    private boolean isItemInDb(Item item) {
        DBObject dbObject = new DBObject(this);
        return dbObject.isItemExistsInDB(item);
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setIsInFavorites(boolean isIn) {
        if (isIn) {
            layoutAddToFavorites.setVisibility(View.GONE);
            layoutDeleteFromFavorites.setVisibility(View.VISIBLE);
        } else {
            layoutAddToFavorites.setVisibility(View.VISIBLE);
            layoutDeleteFromFavorites.setVisibility(View.GONE);
        }
    }

    View.OnClickListener onFavoritesClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layoutAddToFavorites:

                    setIsInFavorites(true);
                    dbHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            DBObject dbObject = new DBObject(DetailsActivity.this);
                            dbObject.addItemToDB(item);
                        }
                    });
                    break;
                case R.id.layoutDeleteFromFavorites:

                    setIsInFavorites(false);
                    dbHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            DBObject dbObject = new DBObject(DetailsActivity.this);
                            dbObject.deleteItemFromDB(item);
                        }
                    });
                    break;
            }
        }
    };

    View insertPhoto(String path) {

        LinearLayout layout = new LinearLayout(getApplicationContext());
        ImageView imageView = new ImageView(getApplicationContext());
        Picasso.with(this).load(path).centerCrop().resize(300, 300).into(imageView);

        layout.addView(imageView);
        return layout;
    }
}
