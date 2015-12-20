package com.tuccro.etsywatcher.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.Loader;

import com.tuccro.etsywatcher.R;
import com.tuccro.etsywatcher.async.GetRequestAsyncTask;
import com.tuccro.etsywatcher.async.IGetRequestResult;
import com.tuccro.etsywatcher.model.Item;
import com.tuccro.etsywatcher.utils.NetUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by tuccro on 12/20/15.
 */
public class ItemsLoader extends Loader<List<Item>> implements IGetRequestResult {

    public static final String ARG_QUERY = "query";
    public static final String ARG_CATEGORY = "category";

    private String queryString;
    private String categoryString;

    //    Documentation:
    //    https://www.etsy.com/developers/documentation/reference/listing#method_findalllistingactive
    static final String requestAddressString = "https://openapi.etsy.com/v2/listings/active";

    GetRequestAsyncTask getRequestAsyncTask;
    String requestUrlString;

    int page = 0;

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
    public ItemsLoader(Context context, Bundle args) {
        super(context);

        if (args != null) {
            queryString = args.getString(ARG_QUERY);
            categoryString = args.getString(ARG_CATEGORY);
        }
    }

    @Override
    protected boolean onCancelLoad() {
        initAsyncTask();
        return super.onCancelLoad();
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();

        initAsyncTask();
        makeRequestString();

        getRequestAsyncTask.execute(requestUrlString);
    }

    private void initAsyncTask() {
        if (getRequestAsyncTask != null)
            getRequestAsyncTask.cancel(true);

        getRequestAsyncTask = new GetRequestAsyncTask(this);
    }

    private void makeRequestString() {
        HashMap<String, String> parameters = new HashMap<>();

        parameters.put("api_key", getContext().getString(R.string.api_key));
        parameters.put("keywords", queryString);
        parameters.put("category", categoryString);

        if (page != 0) parameters.put("page", String.valueOf(page));
        page++;

        requestUrlString = requestAddressString
                .concat(NetUtils.createRequestBodyString(parameters));
    }

    @Override
    public void onGetRequestResult(List<String> result) {
        try {
            deliverResult(Item.fromFullJson(new JSONObject(result.get(0))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetRequestError(List<String> result) {

    }
}
