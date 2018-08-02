package com.example.marin.mobileradninalog.network;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class DeleteData {
    ConnectAndReceiveData connectAndReceiveData;

    protected void deleteData(String url) throws IOException {
        Log.d("url", url);

        connectAndReceiveData = new ConnectAndReceiveData();

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpclient.execute(httpPost);
    }
}