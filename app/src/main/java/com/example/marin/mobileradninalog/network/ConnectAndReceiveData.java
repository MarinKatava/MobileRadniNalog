package com.example.marin.mobileradninalog.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectAndReceiveData {

    public JSONObject connectAndReceiveData(String url) throws IOException, JSONException {
        Log.d("url", url);
        URL m_ulr = new URL(url);
        HttpURLConnection urlConnection = (HttpURLConnection) m_ulr.openConnection();
        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        String rezultat = convertStreamToString(in);
        return new JSONObject(rezultat);
    }
    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null)
                sb.append(line + "\n");
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }
}
