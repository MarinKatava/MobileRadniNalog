package com.example.marin.mobileradninalog.nalog.zahtjev;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.marin.mobileradninalog.Constants.URL;
import com.example.marin.mobileradninalog.R;
import com.example.marin.mobileradninalog.database.SearchRadniNalog;
import com.example.marin.mobileradninalog.database.SearchResultReceiver;
import com.example.marin.mobileradninalog.model.Covjek;
import com.example.marin.mobileradninalog.model.Firma;
import com.example.marin.mobileradninalog.model.RadniNalog;
import com.example.marin.mobileradninalog.nalog.adapters.FirmaSpinnerAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class ZahtjevActivity extends AppCompatActivity implements SearchResultReceiver.Receiver {
    ProgressDialog mProgressDialog;

    Calendar mCurrentDate, mCurrentTime;
    int day, month, year, hours, minutes;
    int unos, unosCovjek;
    boolean isHitno;

    Spinner spinnerPoslovniPartner;
    TextView date;
    Spinner spinnerOdgovornaOsoba;
    EditText unesiteOpis;
    CheckBox hitna;
    CheckBox normalna;
    Button spremiti;

    ArrayList<Covjek> covjekLista;
    ArrayList<Firma> firmaLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zahtjev);

        Toolbar noviZahtjevToolbar = findViewById(R.id.noviZahtjevToolbar);
        setSupportActionBar(noviZahtjevToolbar);
        noviZahtjevToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZahtjevActivity.this.finish();
            }
        });
        spinnerPoslovniPartner = findViewById(R.id.spinnerPoslovniPartner);
        date = findViewById(R.id.date);
        spinnerOdgovornaOsoba = findViewById(R.id.spinnerOdgovornaOsoba);
        unesiteOpis = findViewById(R.id.unesiteOpis);
        hitna = findViewById(R.id.hitna);
        normalna = findViewById(R.id.normalna);
        spremiti = findViewById(R.id.spremiti);

        mCurrentDate = Calendar.getInstance();
        mCurrentTime = Calendar.getInstance();
        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);
        hours = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        minutes = mCurrentTime.get(Calendar.MINUTE);

        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, SearchRadniNalog.class);
        SearchResultReceiver mReceiver = new SearchResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("urlFirma", URL.getFirma);
        intent.putExtra("urlCovjek", URL.getCovjek);
        intent.putExtra("category", "getCovjekFirma");
        startService(intent);


//        postavljanje datuma
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ZahtjevActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear + 1;
                        date.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


//        uzimanje podataka firme koju smo odabrali
        spinnerPoslovniPartner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unos = spinnerPoslovniPartner.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ////////// uzimanje podataka osobe koju smo odabrali
        spinnerOdgovornaOsoba.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unosCovjek = spinnerOdgovornaOsoba.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        spremiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (hitna.isChecked() && normalna.isChecked()) {
                        Toast.makeText(ZahtjevActivity.this, "Odznačite jednu vrstu intervencije", Toast.LENGTH_SHORT).show();
                    } else if (hitna.isChecked()) {
                        isHitno = true;
                    } else if (normalna.isChecked()) {
                        isHitno = false;
                    } else {
                        Toast.makeText(ZahtjevActivity.this, "Označite vrstu intervencije! ", Toast.LENGTH_SHORT).show();
                    }


                    RadniNalog radniNalog = new RadniNalog("0",
                            covjekLista.get(spinnerOdgovornaOsoba.getSelectedItemPosition()).getCovjekId(),
                            firmaLista.get(spinnerOdgovornaOsoba.getSelectedItemPosition()).getFirmaId(),
                            date.getText().toString(),
                            unesiteOpis.getText().toString(),null ,
                            "0", "0", 1,
                            1, null, null, isHitno);

//                    RadniNalog radniNalog = new RadniNalog(unosCovjek, unos, date.getText().toString(), unesiteOpis.getText().toString(), isHitno);

                    Intent intent = new Intent(Intent.ACTION_SYNC, null, getApplicationContext(), SearchRadniNalog.class);
                    SearchResultReceiver mReceiver = new SearchResultReceiver(new Handler());
                    mReceiver.setReceiver(ZahtjevActivity.this);
                    intent.putExtra("receiver", mReceiver);
                    intent.putExtra("category", "postZahtjev");
                    intent.putExtra("radniNalog", radniNalog);
                    intent.putExtra("urlPostRadniNalog", URL.postRadniNalog);
                    getApplicationContext().startService(intent);

                    Toast.makeText(getApplicationContext(), "Spremljeno", Toast.LENGTH_SHORT).show();


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        });


    }////////////OnCreate


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultData.getSerializable("category") == "getCovjekFirma") {
            switch (resultCode) {
                case SearchRadniNalog.STATUS_RUNNING:
                    showProgressDialog();
                    break;
                case SearchRadniNalog.STATUS_FINISHED:
                    hideProgressDialog();

                    covjekLista = (ArrayList<Covjek>) resultData.getSerializable("zaposlenici");
                    firmaLista = (ArrayList<Firma>) resultData.getSerializable("firme");

//        postavljanje liste zaposlenika
                    PeopleSpinnerAdapter covjekListAdapter = new PeopleSpinnerAdapter(getApplicationContext(), R.layout.spinner_item, covjekLista);
                    covjekListAdapter.setDropDownViewResource(R.layout.spinner_item);
                    spinnerOdgovornaOsoba.setAdapter(covjekListAdapter);

//        postavljanje liste ljudi
                    FirmaSpinnerAdapter firmaListAdapter = new FirmaSpinnerAdapter(getApplicationContext(), R.layout.spinner_item, firmaLista);
                    firmaListAdapter.setDropDownViewResource(R.layout.spinner_item);
                    spinnerPoslovniPartner.setAdapter(firmaListAdapter);

                    break;
                case SearchRadniNalog.STATUS_ERROR:
                    android.util.Log.d("ERROR", resultData.getString(Intent.EXTRA_TEXT));
                    break;
            }
        } else {
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

