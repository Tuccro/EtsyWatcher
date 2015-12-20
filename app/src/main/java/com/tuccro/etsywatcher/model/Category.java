package com.tuccro.etsywatcher.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tuccro on 12/19/15.
 */
public class Category {

    String title;
    String name;

    public Category(String title, String name) {
        this.title = title;
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Category fromJson(final JSONObject object) {
        final String mTitle = object.optString("page_title", "");
        final String mName = object.optString("name", "");
        return new Category(mTitle, mName);
    }

    public static ArrayList<Category> fromFullJson(final JSONObject jsonObject) {
        final ArrayList<Category> categories = new ArrayList<>();
        final JSONArray array = jsonObject.optJSONArray("results");

        for (int index = 0; index < array.length(); ++index) {
            try {
                final Category category = fromJson(array.getJSONObject(index));
                if (null != category) categories.add(category);
            } catch (final JSONException ignored) {
            }
        }
        return categories;
    }
}
