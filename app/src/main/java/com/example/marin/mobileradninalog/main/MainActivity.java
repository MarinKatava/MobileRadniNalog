package com.example.marin.mobileradninalog.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.marin.mobileradninalog.constants.CheckForPermission;
import com.example.marin.mobileradninalog.constants.URL;
import com.example.marin.mobileradninalog.R;
import com.example.marin.mobileradninalog.network.CheckInternetConnection;
import com.example.marin.mobileradninalog.network.Service;
import com.example.marin.mobileradninalog.network.SearchResultReceiver;
import com.example.marin.mobileradninalog.model.RadniNalog;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SearchResultReceiver.Receiver, CheckForPermission {
    ListView listView;
    ArrayList<RadniNalog> radniNalog;
    RadniNalogAdapter adapter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();

        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission(this)) {
                requestForSpecificPermission();
            }
        }

        CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
        if (checkInternetConnection.checkConnection(this)) {

            Button novizahtjev = findViewById(R.id.noviZahtjev);
            novizahtjev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, NoviZahtjevActivity.class);
                    startActivity(intent);
                }
            });

            listView = findViewById(R.id.listView);

            Intent intent = new Intent(Intent.ACTION_SYNC, null, this, Service.class);
            SearchResultReceiver mReceiver = new SearchResultReceiver(new Handler());
            mReceiver.setReceiver(this);
            intent.putExtra("receiver", mReceiver);
            intent.putExtra("urlRadniNalog", URL.getRadniNalog);
            intent.putExtra("category", "getRadniNalog");
            startService(intent);
        } else {
            Toast.makeText(this, "Provjerite internetsku vezu", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case Service.STATUS_RUNNING:
                showProgressDialog();
                break;
            case Service.STATUS_FINISHED:
                hideProgressDialog();
                mProgressDialog.dismiss();

                radniNalog = (ArrayList<RadniNalog>) resultData.getSerializable("radninalozi");

                adapter = new RadniNalogAdapter(MainActivity.this, R.layout.radni_nalog, radniNalog, getResources());
                listView.setAdapter(adapter);
                break;
            case Service.STATUS_ERROR:
                android.util.Log.d("ERROR", resultData.getString(Intent.EXTRA_TEXT));
                break;
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
            mProgressDialog.setMessage("Učitavanje...");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public boolean checkIfAlreadyhavePermission(Context context) {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                } else {
                    //not granted
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {

    }
}
