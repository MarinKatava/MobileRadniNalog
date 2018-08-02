package com.example.marin.mobileradninalog.network;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;


public class SearchResultReceiver extends ResultReceiver {
    private Receiver mReceiver;

    @SuppressLint("RestrictedApi")
    public SearchResultReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null)
            mReceiver.onReceiveResult(resultCode, resultData);
    }
}