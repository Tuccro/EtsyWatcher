package com.tuccro.etsywatcher.utils;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by tuccro on 12/20/15.
 */
public class NetUtils {

    public static String createRequestBodyString(Map<String, String> parameters) {

        StringBuilder sbResult = new StringBuilder("");
        String result;

        if (parameters != null) {

            Iterator it = parameters.entrySet().iterator();
            while (it.hasNext()) {

                if (sbResult.length() != 0) sbResult.append("&");
                else sbResult.append("?");

                Map.Entry pair = (Map.Entry) it.next();

                sbResult.append(pair.getKey() + "=" + pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }
            return sbResult.toString();
        } else return sbResult.toString();
    }

    public static String getRequest(String url) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
