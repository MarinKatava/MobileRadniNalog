package com.example.marin.mobileradninalog.database;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class UpdateData {
    ConnectAndReceiveData connectAndReceiveData;

    protected String updateColumn(String url, Object p) throws IOException, JSONException {
        Log.d("url", url);

        connectAndReceiveData = new ConnectAndReceiveData();

        String result = "";
        InputStream inputStream = null;
        HttpClient httpclient = new DefaultHttpClient();

        HttpPut httpPut  = new HttpPut(url);

        Gson gson = new Gson();
        String json = gson.toJson(p);
        StringEntity se = new StringEntity(json);
        httpPut.setEntity(se);

        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");

        HttpResponse httpResponse = httpclient.execute(httpPut);

        inputStream = httpResponse.getEntity().getContent();

        if (inputStream != null)
            result = connectAndReceiveData.convertStreamToString(inputStream);

        return result;
    }
}
