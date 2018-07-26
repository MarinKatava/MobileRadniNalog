package com.example.marin.mobileradninalog.database;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

public class DeleteData {
    ConnectAndReceiveData connectAndReceiveData;

    protected void deleteData(String url) throws IOException {
        Log.d("url", url);

        connectAndReceiveData = new ConnectAndReceiveData();

        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(url);
        HttpResponse httpResponse = httpclient.execute(httpPost);
    }
}