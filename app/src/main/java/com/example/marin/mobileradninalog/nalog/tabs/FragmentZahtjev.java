package com.example.marin.mobileradninalog.nalog.tabs;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marin.mobileradninalog.Constants.URL;
import com.example.marin.mobileradninalog.R;
import com.example.marin.mobileradninalog.database.CheckInternetConnection;
import com.example.marin.mobileradninalog.database.GetData;
import com.example.marin.mobileradninalog.database.SearchRadniNalog;
import com.example.marin.mobileradninalog.database.SearchResultReceiver;
import com.example.marin.mobileradninalog.main.MainActivity;
import com.example.marin.mobileradninalog.model.Covjek;
import com.example.marin.mobileradninalog.model.Firma;
import com.example.marin.mobileradninalog.model.RadniNalog;
import com.example.marin.mobileradninalog.nalog.adapters.FirmaSpinnerAdapter;
import com.example.marin.mobileradninalog.nalog.zahtjev.PeopleSpinnerAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class FragmentZahtjev extends Fragment implements SearchResultReceiver.Receiver {
    Calendar mCurrentDate, mCurrentTime;
    int day, month, year, hours, minutes;

    ArrayList<RadniNalog> rn;
    ArrayList<Covjek> covjekList;
    ArrayList<Firma> firmaList;
    Intent intent;

    int radniNalogPosition;
    int rnCovjekId;
    int rnFirmaId;
    int selectedCovjek = 0;
    int selectedFirma = 0;
    boolean isHitno;

    String imeFromId = "";
    String prezimeFromId = "";
    String firmaFromId = "";

    Spinner spinnerPoslovniPartner;
    TextView date, odgovornaOsobaTextView, poslovnipartnerTextView;
    Spinner spinnerOdgovornaOsoba;
    EditText unesiteOpis;
    CheckBox hitna;
    CheckBox normalna;
    Button spremiti;

    GetData getData = new GetData();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_zahtjev, container, false);

        spinnerPoslovniPartner = view.findViewById(R.id.spinnerPoslovniPartner);
        date = view.findViewById(R.id.date);
        spinnerOdgovornaOsoba = view.findViewById(R.id.spinnerOdgovornaOsoba);
        unesiteOpis = view.findViewById(R.id.unesiteOpis);
        hitna = view.findViewById(R.id.hitna);
        normalna = view.findViewById(R.id.normalna);
        spremiti = view.findViewById(R.id.spremiti);
        odgovornaOsobaTextView = view.findViewById(R.id.odgovornaOsobaTextView);
        poslovnipartnerTextView = view.findViewById(R.id.poslovniPartnerTextView);

        mCurrentDate = Calendar.getInstance();
        mCurrentTime = Calendar.getInstance();
        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);
        hours = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        minutes = mCurrentTime.get(Calendar.MINUTE);


//      dohvacanje liste naloga
        rn = getArguments().getParcelableArrayList("radniNalog");
        radniNalogPosition = getArguments().getInt("position");
        covjekList = getArguments().getParcelableArrayList("covjekList");
        firmaList = getArguments().getParcelableArrayList("firmaList");

//    postavljanje prethodno unesenih vrijednosti spinnera u textview
        try {
            getCovjekFirma();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        popunjavanje spinnera listom zaposlenika
        PeopleSpinnerAdapter covjekListAdapter = new PeopleSpinnerAdapter(getContext(), R.layout.spinner_item, covjekList);
        covjekListAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerOdgovornaOsoba.setAdapter(covjekListAdapter);

        for (int i = 0; i < covjekList.size(); i++) {
            if (covjekList.get(i).getCovjekId() == rnCovjekId) {
                selectedCovjek = covjekList.indexOf(covjekList.get(i));
            }
        }
        spinnerOdgovornaOsoba.setSelection(selectedCovjek);

//        popunjavanje spinnera listom firmi
        FirmaSpinnerAdapter firmaListAdapter = new FirmaSpinnerAdapter(getContext(), R.layout.spinner_item, firmaList);
        firmaListAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerPoslovniPartner.setAdapter(firmaListAdapter);

        for (int i = 0; i < firmaList.size(); i++) {
            if (firmaList.get(i).getFirmaId() == rnFirmaId) {
                selectedFirma = firmaList.indexOf(firmaList.get(i));
            }
        }
        spinnerPoslovniPartner.setSelection(selectedFirma);


//        dohvacanje datuma i opisa problema
        date.setText(rn.get(radniNalogPosition).getDatumZahtjeva());
        unesiteOpis.setText(rn.get(radniNalogPosition).getOpisProblema());


//        postavljanje datuma
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear + 1;
                        date.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


//         dohvacanje intervencije (hitna ili normalna)
        if (rn.get(radniNalogPosition).getIsHitno()) {
            hitna.setChecked(true);
            normalna.setChecked(false);
        } else if (!rn.get(radniNalogPosition).getIsHitno()) {
            hitna.setChecked(false);
            normalna.setChecked(true);
        }


//        spremanje promjena
        spremiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unesiteOpis.getText().toString() == "") {
                    Toast.makeText(getContext(), "Unesite opis problema", Toast.LENGTH_SHORT).show();
                } else {
//        provjera koji je checkbox oznacen
                    if (hitna.isChecked()) {
                        isHitno = true;
                    } else {
                        isHitno = false;
                    }

                    if ((hitna.isChecked() != true && normalna.isChecked() != true) || (hitna.isChecked() == true && normalna.isChecked()==true)) {
                        Toast.makeText(getContext(), "Označite jednu vrstu intervencije", Toast.LENGTH_SHORT).show();
                    } else {
                        RadniNalog radniNalog = new RadniNalog(rn.get(radniNalogPosition).getRadniNalogId(),
                                rn.get(radniNalogPosition).getBrojNaloga(),
                                covjekList.get(spinnerOdgovornaOsoba.getSelectedItemPosition()).getCovjekId(),
                                firmaList.get(spinnerPoslovniPartner.getSelectedItemPosition()).getFirmaId(),
                                date.getText().toString(),
                                unesiteOpis.getText().toString(),
                                getData.jsonDateFormat(rn.get(radniNalogPosition).getDatumObrade()),
                                rn.get(radniNalogPosition).getVrijemePocetka(),
                                rn.get(radniNalogPosition).getVrijemeKraja(),
                                rn.get(radniNalogPosition).getPoOsnovuId(),
                                rn.get(radniNalogPosition).getStatusSistemaId(),
                                rn.get(radniNalogPosition).getUgradjeniMAterijal(),
                                rn.get(radniNalogPosition).getPrimjedbe(),
                                isHitno);

                        CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
                        if (checkInternetConnection.checkConnection(getContext())) {
                            intent = new Intent(Intent.ACTION_SYNC, null, getContext(), SearchRadniNalog.class);
                            SearchResultReceiver mReceiver = new SearchResultReceiver(new Handler());
                            mReceiver.setReceiver(FragmentZahtjev.this);
                            intent.putExtra("receiver", mReceiver);
                            intent.putExtra("category", "postZahtjev");
                            intent.putExtra("radniNalog", radniNalog);
                            intent.putExtra("urlPostRadniNalog", URL.saveEditRadniNalog + rn.get(radniNalogPosition).getRadniNalogId());
//                    intent.putExtra("urlUpdateRadniNalog", URL.saveEditRadniNalog + rn.get(position).getRadniNalogId());
                            getContext().startService(intent);
                            Toast.makeText(getContext(), "Spremljeno", Toast.LENGTH_SHORT).show();

                            new Handler().post(new Runnable() {

                                @Override
                                public void run()
                                {
                                    Intent intent = getActivity().getIntent();
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    getActivity().overridePendingTransition(0, 0);
                                    getActivity().finish();

                                    getActivity().overridePendingTransition(0, 0);
                                    startActivity(intent);
                                }
                            });


                        } else {
                            Toast.makeText(getContext(), "Provjerite internetsku vezu", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //    postavljanje prethodno unesenih vrijednosti spinnera u textview
    public void getCovjekFirma() {
        rnCovjekId = rn.get(radniNalogPosition).getCovjekId();
        rnFirmaId = rn.get(radniNalogPosition).getFirmaId();

        for (int i = 0; i < covjekList.size(); i++) {
            if (covjekList.get(i).getCovjekId() == rnCovjekId) {
                imeFromId = covjekList.get(i).getIme().toString();
                prezimeFromId = covjekList.get(i).getPrezime().toString();
            }
        }
        odgovornaOsobaTextView.setText(imeFromId + " " + prezimeFromId);

        for (int i = 0; i < firmaList.size(); i++) {
            if (firmaList.get(i).getFirmaId() == rnFirmaId) {
                firmaFromId = firmaList.get(i).getNaziv().toString();
            }
        }
        poslovnipartnerTextView.setText(firmaFromId);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case SearchRadniNalog.STATUS_RUNNING:
                getActivity().stopService(intent);
                Toast.makeText(getContext(), "Spremljeno", Toast.LENGTH_SHORT).show();
                break;
            case SearchRadniNalog.STATUS_FINISHED:
                break;
            case SearchRadniNalog.STATUS_ERROR:
                android.util.Log.d("ERROR", resultData.getString(Intent.EXTRA_TEXT));
                break;
        }
    }
}

