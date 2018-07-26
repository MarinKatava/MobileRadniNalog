package com.example.marin.mobileradninalog.nalog.tabs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.marin.mobileradninalog.Constants.URL;
import com.example.marin.mobileradninalog.R;
import com.example.marin.mobileradninalog.database.SearchRadniNalog;
import com.example.marin.mobileradninalog.database.SearchResultReceiver;
import com.example.marin.mobileradninalog.model.Covjek;
import com.example.marin.mobileradninalog.model.Firma;
import com.example.marin.mobileradninalog.model.OpisPosla;
import com.example.marin.mobileradninalog.model.RadniNalog;
import com.example.marin.mobileradninalog.model.Stavka;
import com.example.marin.mobileradninalog.nalog.WriteToPdf;
import com.example.marin.mobileradninalog.nalog.adapters.PageAdapter;

import java.util.ArrayList;

public class TabActivity extends AppCompatActivity implements SearchResultReceiver.Receiver {
    private ProgressDialog mProgressDialog;

    TabLayout tabs;
    ViewPager viewPager;
    PageAdapter pageAdapter;
    TabItem tabNalog;
    TabItem tabStavke;
    Button preuzmi;

    ArrayList<RadniNalog> radniNalog;
    ArrayList<Covjek> covjekList;
    ArrayList<Firma> firmaList;
    ArrayList<Stavka> stavkaList;
    ArrayList<OpisPosla> opisPoslaList;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tabs = findViewById(R.id.tabs);
        tabNalog = findViewById(R.id.tabNalog);
        tabStavke = findViewById(R.id.tabStavke);
        viewPager = findViewById(R.id.viewPager);
        preuzmi = findViewById(R.id.download);

        Bundle extras = getIntent().getExtras();
        position = extras.getInt("position");
        radniNalog = getIntent().getParcelableArrayListExtra("radniNalogList");

        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, SearchRadniNalog.class);
        SearchResultReceiver mReceiver = new SearchResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("urlFirma", URL.getFirma);
        intent.putExtra("urlCovjek", URL.getCovjek);
        intent.putExtra("urlGetOpisPosla", URL.getOpisPosla);
//       url za dohvacanje stavki samo za taj radniNalogId
        intent.putExtra("urlGetStavka", URL.getStavka + String.valueOf(radniNalog.get(position).getRadniNalogId()));
        intent.putExtra("category", "getData");
        startService(intent);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);

//       back tipka na toolbaru
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabActivity.this.finish();
            }
        });

//        skidanje pdf-a
        preuzmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    WriteToPdf writeToPdf = new WriteToPdf();
                    writeToPdf.writeToPdf(radniNalog.get(position), stavkaList, firmaList, covjekList, opisPoslaList);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(TabActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case SearchRadniNalog.STATUS_RUNNING:
                showProgressDialog();
                break;
            case SearchRadniNalog.STATUS_FINISHED:
                hideProgressDialog();

                covjekList = (ArrayList<Covjek>) resultData.getSerializable("zaposlenici");
                firmaList = (ArrayList<Firma>) resultData.getSerializable("firme");
                stavkaList = (ArrayList<Stavka>) resultData.getSerializable("stavke");
                opisPoslaList = (ArrayList<OpisPosla>) resultData.getSerializable("opisPosla");

                pageAdapter = new PageAdapter(getSupportFragmentManager(), tabs.getTabCount(), radniNalog, covjekList, firmaList, stavkaList, opisPoslaList, position);
                viewPager.setAdapter(pageAdapter);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
                break;
            case SearchRadniNalog.STATUS_ERROR:
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

}

