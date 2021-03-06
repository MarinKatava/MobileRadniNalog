package com.example.marin.mobileradninalog.network;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

public class SendData {
    ConnectAndReceiveData connectAndReceiveData;

    protected void sendJson(String url, Object p) throws IOException {
        Log.d("url", url);

        connectAndReceiveData = new ConnectAndReceiveData();

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        Gson gson = new Gson();
        String json = gson.toJson(p);
        HttpEntity se = new StringEntity(json,"UTF-8");
        httpPost.setEntity(se);

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        httpclient.execute(httpPost);

    }
}
