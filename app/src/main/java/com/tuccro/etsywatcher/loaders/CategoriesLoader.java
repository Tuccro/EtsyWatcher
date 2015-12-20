package com.tuccro.etsywatcher.loaders;

import android.content.Context;
import android.support.v4.content.Loader;

import com.tuccro.etsywatcher.R;
import com.tuccro.etsywatcher.async.GetRequestAsyncTask;
import com.tuccro.etsywatcher.async.IGetRequestResult;
import com.tuccro.etsywatcher.model.Category;
import com.tuccro.etsywatcher.utils.NetUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by tuccro on 12/19/15.
 */
public class CategoriesLoader extends Loader<List<Category>> implements IGetRequestResult {

    static final String requestAddressString = "https://openapi.etsy.com/v2/taxonomy/categories";

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
    public CategoriesLoader(Context context) {
        super(context);
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

        requestUrlString = requestAddressString
                .concat(NetUtils.createRequestBodyString(parameters));
    }

    @Override
    public void onGetRequestResult(List<String> result) {
        try {
            deliverResult(Category.fromFullJson(new JSONObject(result.get(0))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetRequestError(List<String> result) {

    }
}
