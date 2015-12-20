package com.tuccro.etsywatcher.async;

import android.os.AsyncTask;

import com.tuccro.etsywatcher.utils.NetUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuccro on 12/20/15.
 */
public class GetRequestAsyncTask extends AsyncTask<String, Void, List<String>> {

    IGetRequestResult iGetRequestResult;

    public GetRequestAsyncTask(Object listener) {
        iGetRequestResult = (IGetRequestResult) listener;
    }

    @Override
    protected List<String> doInBackground(String... params) {

        if (params != null
                && params.length > 0)
            try {

                ArrayList<String> arrayList = new ArrayList<>(params.length);

                for (String url : params) {
                    arrayList.add(NetUtils.getRequest(url));
                }

                return arrayList;
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }

    @Override
    protected void onPostExecute(List<String> s) {
        super.onPostExecute(s);
        if (s != null) iGetRequestResult.onGetRequestResult(s);
        else iGetRequestResult.onGetRequestError(s);
    }
}
