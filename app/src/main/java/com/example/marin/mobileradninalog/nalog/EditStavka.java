package com.example.marin.mobileradninalog.nalog;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marin.mobileradninalog.Constants.URL;
import com.example.marin.mobileradninalog.R;
import com.example.marin.mobileradninalog.database.SearchRadniNalog;
import com.example.marin.mobileradninalog.database.SearchResultReceiver;
import com.example.marin.mobileradninalog.model.OpisPosla;
import com.example.marin.mobileradninalog.model.Stavka;
import com.example.marin.mobileradninalog.nalog.adapters.OpisPoslaSpinnerAdapter;

import java.util.ArrayList;

public class EditStavka extends AppCompatActivity implements SearchResultReceiver.Receiver {
    TextView nazivPoslaTekst;
    EditText opisPoslaTekst;
    Spinner spinnerNoviNazivPosla;
    Button spremiti;
    ArrayList<OpisPosla> opisPoslaList;
    int stavkaId;
    int radniNalogId;
    int position;
    int opisPoslaIdToSend;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stavka);

        nazivPoslaTekst = findViewById(R.id.nazivPoslaTekst);
        opisPoslaTekst = findViewById(R.id.opisPoslaTekst);
        spinnerNoviNazivPosla = findViewById(R.id.spinnerNoviNazivPosla);
        spremiti = findViewById(R.id.spremiti);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

//       back tipka na toolbaru
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditStavka.this.finish();
            }
        });


        Bundle extras = getIntent().getExtras();
        opisPoslaList = extras.getParcelableArrayList("opisPoslaList");
        stavkaId = extras.getInt("stavkaId");
        radniNalogId = extras.getInt("radniNalogId");
        position = extras.getInt("position");

        nazivPoslaTekst.setText(extras.getString("nazivPosla"));
        opisPoslaTekst.setText(extras.getString("stavkaTekst"));

        OpisPoslaSpinnerAdapter opisPoslaSpinnerAdapter = new OpisPoslaSpinnerAdapter(getApplicationContext(), R.layout.spinner_item, opisPoslaList);
        opisPoslaSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerNoviNazivPosla.setAdapter(opisPoslaSpinnerAdapter);

        spinnerNoviNazivPosla.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                opisPoslaIdToSend = opisPoslaList.get(spinnerNoviNazivPosla.getSelectedItemPosition()).getOpisPoslaId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spremiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Stavka stavka = new Stavka(stavkaId, radniNalogId, opisPoslaIdToSend, opisPoslaTekst.getText().toString());

                intent = new Intent(Intent.ACTION_SYNC, null, EditStavka.this, SearchRadniNalog.class);
                SearchResultReceiver mReceiver = new SearchResultReceiver(new Handler());
                mReceiver.setReceiver(EditStavka.this);
                intent.putExtra("receiver", mReceiver);
                intent.putExtra("urlUpdateRadniNalogStavka", URL.saveEditRadniNalogStavka + stavkaId);
                intent.putExtra("radniNalogStavka", stavka);
                intent.putExtra("category", "editStavka");
                startService(intent);
            }
        });
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case SearchRadniNalog.STATUS_RUNNING:
                getApplication().stopService(intent);
                Toast.makeText(this, "Spremljeno", Toast.LENGTH_SHORT).show();
                EditStavka.this.finish();
                break;
            case SearchRadniNalog.STATUS_FINISHED:
                break;
            case SearchRadniNalog.STATUS_ERROR:
                android.util.Log.d("ERROR", resultData.getString(Intent.EXTRA_TEXT));
                break;
        }
    }
}
