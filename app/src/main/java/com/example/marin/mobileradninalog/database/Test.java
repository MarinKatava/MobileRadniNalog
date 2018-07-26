package com.example.marin.mobileradninalog.database;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.marin.mobileradninalog.R;
import com.example.marin.mobileradninalog.main.MainActivity;
import com.example.marin.mobileradninalog.main.RadniNalogAdapter;
import com.example.marin.mobileradninalog.model.Covjek;
import com.example.marin.mobileradninalog.model.RadniNalog;

import java.util.ArrayList;

public class Test extends AppCompatActivity implements SearchResultReceiver.Receiver {
    ArrayList<Covjek> covjekList = new ArrayList<>();
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        editText = findViewById(R.id.textView);
        Button button = findViewById(R.id.button);

//        final String url = "http://rnalog.dyndns.biz/RadniNalog/SaveRadniNalog";
        final String url = "http://rnalog.dyndns.biz/Covjek/GetCovjek";


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                RadniNalog radniNalog = new RadniNalog("55",
////                        1, 1, "2018-06-25",
////                        editText.getText().toString(), "2018-06-26",
////                        String.valueOf(0), String.valueOf(0), 1,
////                        1, "mmmmmmm", "ppppppp", false);
////
////                Intent intent = new Intent(Intent.ACTION_SYNC, null, Test.this, SearchRadniNalog.class);
////                SearchResultReceiver mReceiver = new SearchResultReceiver(new Handler());
////                mReceiver.setReceiver(Test.this);
////                intent.putExtra("receiver", mReceiver);
////                intent.putExtra("category", "sendJson");
////                intent.putExtra("radniNalog", radniNalog);
////                intent.putExtra("url", url);
////                startService(intent);


                Intent intent = new Intent(Intent.ACTION_SYNC, null, Test.this, SearchRadniNalog.class);
                SearchResultReceiver mReceiver = new SearchResultReceiver(new Handler());
                mReceiver.setReceiver(Test.this);
                intent.putExtra("receiver", mReceiver);
                intent.putExtra("category", "getCovjek");
                intent.putExtra("url", url);
                startService(intent);

            }
        });
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case SearchRadniNalog.STATUS_RUNNING:
                break;
            case SearchRadniNalog.STATUS_FINISHED:
                covjekList = (ArrayList<Covjek>) resultData.getSerializable("zaposlenici");
                editText.setText(covjekList.get(0).getPrezime().toString() + " je prezime, a ime je : " + covjekList.get(0).getIme().toString());
                break;
            case SearchRadniNalog.STATUS_ERROR:
                android.util.Log.d("ERROR", resultData.getString(Intent.EXTRA_TEXT));
                break;
        }

    }
}
