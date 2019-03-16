package com.ae.apps.pnrstatus.service;

import android.util.Pair;

import com.ae.apps.pnrstatus.exceptions.StatusException;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class NetworkService {

    private OkHttpClient client;

    private static NetworkService sService;

    private NetworkService() {
        client = new OkHttpClient();
    }

    public static NetworkService getInstance() {
        if (null == sService) {
            sService = new NetworkService();
        }
        return sService;
    }

    public String doGetRequest(final String httpUrl) throws Exception {
        return doGetRequest(httpUrl, null);
    }

    public String doGetRequest(final String httpUrl, List<Pair<String, String>> params)
            throws Exception {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(httpUrl).newBuilder();

        // Add Query Params if present
        if (null != params && !params.isEmpty()) {
            for (Pair<String, String> param : params) {
                urlBuilder.addQueryParameter(param.first, param.second);
            }
        }

        String url = urlBuilder.build().toString();

        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();

            return response.body().string();
        } catch (IOException ex) {
            throw new StatusException(ex.getMessage(), StatusException.ErrorCodes.URL_ERROR);
        }
    }

    public static final MediaType WEB_FORM = MediaType.parse("application/x-www-form-urlencoded");

    public String doPostRequest(final String targetUrl,
                                final Map<String, String> headers,
                                final Map<String, String> params) throws StatusException {
        RequestBody requestBody = RequestBody.create(WEB_FORM, "");
        try {
            Request.Builder requestBuilder = new Request.Builder()
                    .url(targetUrl);
            if (null != headers) {
                for (String key : headers.keySet()) {
                    requestBuilder.addHeader(key, String.valueOf(headers.get(key)));
                }
            }
            //--
            FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
            if (null != params) {
                for (String key : params.keySet()) {
                    formEncodingBuilder.add(key, String.valueOf(params.get(key)));
                }
            }

            RequestBody formBody = formEncodingBuilder.build();
            Request request = requestBuilder
                    .url(targetUrl)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException ex) {
            throw new StatusException(ex.getMessage(), StatusException.ErrorCodes.URL_ERROR);
        }
    }
}
