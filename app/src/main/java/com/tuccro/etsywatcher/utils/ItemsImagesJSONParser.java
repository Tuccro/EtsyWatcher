package com.tuccro.etsywatcher.utils;

import com.tuccro.etsywatcher.model.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuccro on 12/20/15.
 */
public class ItemsImagesJSONParser {

    List<Item> itemsList;
    List<String> imagesImagesUrlsJSONs;

    public ItemsImagesJSONParser(List<Item> itemsList, List<String> imagesImagesUrlsJSONs) {
        this.itemsList = itemsList;
        this.imagesImagesUrlsJSONs = imagesImagesUrlsJSONs;
    }

    public List<Item> parseUrls() {

        for (int n = 0; n < itemsList.size(); n++) {

            for (String url : parseUrlsJSON(imagesImagesUrlsJSONs.get(n))) {
                itemsList.get(n).addImageUrl(url);
            }
        }

        return itemsList;
    }

    private List<String> parseUrlsJSON(String jsonString) {
        List<String> urls = new ArrayList();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for (int index = 0; index < jsonArray.length(); ++index) {

                JSONObject jsonImageDescriptor = jsonArray.getJSONObject(index);
                final String url570xN = jsonImageDescriptor.optString("url_570xN", "");
                if (null != url570xN) urls.add(url570xN);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return urls;
    }
}
