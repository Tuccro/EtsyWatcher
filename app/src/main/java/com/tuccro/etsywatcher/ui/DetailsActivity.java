package com.tuccro.etsywatcher.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tuccro.etsywatcher.R;
import com.tuccro.etsywatcher.model.Item;

/**
 * Created by tuccro on 12/22/15.
 */
public class DetailsActivity extends AppCompatActivity {

    public static final String ATTR_ITEM = "Item";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        initToolbar();

        TextView textView = (TextView) findViewById(R.id.text);
        LinearLayout layoutImages = (LinearLayout) findViewById(R.id.layoutImages);

        Item item = (Item) getIntent().getSerializableExtra(ATTR_ITEM);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(item.getId() + "\n");
        stringBuilder.append(item.getTitle() + "\n");
        stringBuilder.append(item.getDescription() + "\n");
        stringBuilder.append(item.getPrice() + "\n");

        for (String s : item.getImagesUrls()) {

            layoutImages.addView(insertPhoto(s));
            stringBuilder.append(s + "\n");
        }


        textView.setText(stringBuilder.toString());
    }


    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    View insertPhoto(String path) {

        LinearLayout layout = new LinearLayout(getApplicationContext());
        ImageView imageView = new ImageView(getApplicationContext());
        Picasso.with(this).load(path).centerCrop().resize(300, 300).into(imageView);

        layout.addView(imageView);
        return layout;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);

        return true;
    }
}
