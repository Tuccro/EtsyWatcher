package com.tuccro.etsywatcher.loaders;

import android.content.Context;
import android.support.v4.content.Loader;

import com.tuccro.etsywatcher.R;
import com.tuccro.etsywatcher.async.GetRequestAsyncTask;
import com.tuccro.etsywatcher.async.IGetRequestResult;
import com.tuccro.etsywatcher.model.Item;
import com.tuccro.etsywatcher.utils.ItemsImagesJSONParser;
import com.tuccro.etsywatcher.utils.NetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tuccro on 12/20/15.
 */
public class ImagesUrlsLoader extends Loader<List<Item>> implements IGetRequestResult {

    List<Item> items;

    static final String requestAddressString = "https://openapi.etsy.com/v2/listings/";
    static final String requestAddressStringParameter = "/images";

    GetRequestAsyncTask getRequestAsyncTask;
    String requestUrlString;

    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     */
    public ImagesUrlsLoader(Context context, List<Item> items) {
        super(context);
        this.items = items;
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();

        initAsyncTask();

        List<String> urlStingsList = new ArrayList<>(items.size());

        for (Item item : items) {
            makeRequestString(item.getId());
            urlStingsList.add(requestUrlString);
        }
        getRequestAsyncTask.execute(urlStingsList
                .toArray(new String[urlStingsList.size()]));
    }

    private void initAsyncTask() {
        if (getRequestAsyncTask != null)
            getRequestAsyncTask.cancel(true);

        getRequestAsyncTask = new GetRequestAsyncTask(this);
    }

    private void makeRequestString(long id) {
        HashMap<String, String> parameters = new HashMap<>();

        parameters.put("api_key", getContext().getString(R.string.api_key));

        requestUrlString = requestAddressString
                .concat(String.valueOf(id))
                .concat(requestAddressStringParameter)
                .concat(NetUtils.createRequestBodyString(parameters));
    }

    private void parseResultAndDeliver(List<String> result) {
        ItemsImagesJSONParser itemsImagesJSONParser = new ItemsImagesJSONParser(items, result);
        items = itemsImagesJSONParser.parseUrls();
        deliverResult(items);
    }

    @Override
    public void onGetRequestResult(List<String> result) {
        parseResultAndDeliver(result);
    }

    @Override
    public void onGetRequestError(List<String> result) {

    }
}
