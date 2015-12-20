package com.tuccro.etsywatcher.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuccro on 12/19/15.
 */
public class Item {

    long id;
    String title;
    String description;
    double price;

    List<String> imagesUrls;

    public Item(long id, String title, String description, double price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        imagesUrls = new ArrayList<>();
    }

    public static Item fromJson(final JSONObject object) {
        final long mId = object.optLong("listing_id", 1);
        final String mTitle = object.optString("title", "");
        final String mDescription = object.optString("description", "");
        final double mPrice = object.optDouble("price", 0.0);
        return new Item(mId, mTitle, mDescription, mPrice);
    }

    public static ArrayList<Item> fromFullJson(final JSONObject jsonObject) {
        final ArrayList<Item> items = new ArrayList<>();
        final JSONArray array = jsonObject.optJSONArray("results");

        for (int index = 0; index < array.length(); ++index) {
            try {
                final Item item = fromJson(array.getJSONObject(index));
                if (null != item) items.add(item);
            } catch (final JSONException ignored) {
            }
        }
        return items;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getImagesUrls() {
        return imagesUrls;
    }

    public void addImageUrl(String url) {
        imagesUrls.add(url);
    }

    public void addImageUrls(List<String> urls) {
        imagesUrls.addAll(urls);
    }
}
