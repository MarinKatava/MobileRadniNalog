package com.example.marin.mobileradninalog.tabs.stavka;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.marin.mobileradninalog.constants.URL;
import com.example.marin.mobileradninalog.R;
import com.example.marin.mobileradninalog.network.Service;
import com.example.marin.mobileradninalog.network.SearchResultReceiver;
import com.example.marin.mobileradninalog.model.OpisPosla;
import com.example.marin.mobileradninalog.model.RadniNalog;
import com.example.marin.mobileradninalog.model.Stavka;
import com.example.marin.mobileradninalog.tabs.adapters.OpisPoslaSpinnerAdapter;
import com.example.marin.mobileradninalog.tabs.adapters.RadniNalogStavkaAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentStavka extends Fragment implements SearchResultReceiver.Receiver {

    ArrayList<Stavka> stavkaList;
    Stavka stavka;
    ArrayList<RadniNalog> radniNalogList;
    ArrayList<OpisPosla> opisPoslaList;
    ArrayList<Stavka> stavkaListToSend;
    int position;
    int radniNalogId;

    ListView stavkaListView;
    EditText insertText;
    Spinner nazivPoslaSpinner;
    RadniNalogStavkaAdapter radniNalogStavkaAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_stavka, container, false);
        setHasOptionsMenu(true);

        Button addStavka = view.findViewById(R.id.add);
        insertText = view.findViewById(R.id.insertTekst);
        nazivPoslaSpinner = view.findViewById(R.id.nazivPosla);
        stavkaListView = view.findViewById(R.id.stavkaListView);

//        dohvacanje liste radnih naloga i liste stavki
        stavkaList = getArguments().getParcelableArrayList("stavkaList");
        position = getArguments().getInt("position");
        radniNalogList = getArguments().getParcelableArrayList("radniNalog");
        opisPoslaList = getArguments().getParcelableArrayList("opisPosla");

//        uzimanje radniNalogId iz radnog naloga u kojem smo trenutno
        radniNalogId = radniNalogList.get(position).getRadniNalogId();

//        dodavanje u listu samo onih stavki koje sadrze u sebi radniNalogId trenutnog radnog naloga
        stavkaListToSend = new ArrayList<>();
        for (int i = 0; i < stavkaList.size(); i++) {
            if (stavkaList.get(i).getRadniNalogId() == radniNalogId) {
                stavkaListToSend.add(stavkaList.get(i));
            } else {
                stavkaListToSend.isEmpty();
            }
        }

//        postavljanje naziva poslova u spinner
        final OpisPoslaSpinnerAdapter opisPoslaSpinnerAdapter = new OpisPoslaSpinnerAdapter(getContext(), R.layout.spinner_item, opisPoslaList);
        opisPoslaSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        nazivPoslaSpinner.setAdapter(opisPoslaSpinnerAdapter);

//        postavljanje podataka u listu (upakovanu u adapter)
        radniNalogStavkaAdapter = new RadniNalogStavkaAdapter(getContext(), R.layout.radni_nalog, stavkaListToSend, opisPoslaList, radniNalogId, getResources());
        stavkaListView.setAdapter(radniNalogStavkaAdapter);

//        dodavanje stavke u listu
        addStavka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                uzimanje indexa oznacenog itema u spinneru i ID-ja iz opisPoslaList na tom indexu
                stavka = new Stavka(radniNalogId, opisPoslaList.get(nazivPoslaSpinner.getSelectedItemPosition()).getOpisPoslaId(), insertText.getText().toString());
//                upisivanje nove stavke u bazu
                Intent intent = new Intent(Intent.ACTION_SYNC, null, getContext(), Service.class);
                SearchResultReceiver mReceiver = new SearchResultReceiver(new Handler());
                mReceiver.setReceiver(FragmentStavka.this);
                intent.putExtra("receiver", mReceiver);
                intent.putExtra("category", "saveRadniNalogStavka");
                intent.putExtra("radniNalogStavka", stavka);
                intent.putExtra("urlSaveRadniNalogStavka", URL.postRadniNalogStavka);
                getContext().startService(intent);
                radniNalogStavkaAdapter.notifyDataSetChanged();


//                refresh liste stavki nakon dodavanja nove stavke
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
            }
        });
        return view;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
    }
}
