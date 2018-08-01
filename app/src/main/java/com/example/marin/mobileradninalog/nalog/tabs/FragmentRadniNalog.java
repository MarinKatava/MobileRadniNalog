package com.example.marin.mobileradninalog.nalog.tabs;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.marin.mobileradninalog.Constants.URL;
import com.example.marin.mobileradninalog.R;
import com.example.marin.mobileradninalog.database.CheckInternetConnection;
import com.example.marin.mobileradninalog.database.GetData;
import com.example.marin.mobileradninalog.database.SearchRadniNalog;
import com.example.marin.mobileradninalog.database.SearchResultReceiver;
import com.example.marin.mobileradninalog.main.MainActivity;
import com.example.marin.mobileradninalog.main.RadniNalogAdapter;
import com.example.marin.mobileradninalog.model.RadniNalog;
import com.example.marin.mobileradninalog.nalog.adapters.PageAdapter;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRadniNalog extends Fragment implements SearchResultReceiver.Receiver {
//    url za create

    ArrayList<RadniNalog> rn;
    GetData getData = new GetData();
    Calendar mCurrentDate, mCurrentTime;
    int day, month, year, hours, minutes, minOd, minDo, satOd, satDo;
    int radniNalogId;
    int setPoOsnovuId, setStatusSistemaId;
    int position;

    Intent intent;
    TextView dateInput, odUnos, doUnos;
    EditText brojNaloga, unosMaterijala, unosPrimjedbe;
    CheckBox ugovor, garancija, poPozivu, potpunoOperativan, potpunoUZastoju, djelimicnoOperativan;
    Button save;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nalog, container, false);

        dateInput = view.findViewById(R.id.formatDatuma);
        odUnos = view.findViewById(R.id.odUnos);
        doUnos = view.findViewById(R.id.doUnos);

        brojNaloga = view.findViewById(R.id.brojNaloga);
        unosMaterijala = view.findViewById(R.id.unosMaterijala);
        unosPrimjedbe = view.findViewById(R.id.unosPrimjedbe);

        ugovor = view.findViewById(R.id.ugovor);
        garancija = view.findViewById(R.id.garancija);
        poPozivu = view.findViewById(R.id.poPozivu);
        potpunoOperativan = view.findViewById(R.id.potpunoOperativan);
        potpunoUZastoju = view.findViewById(R.id.potpunoUZastoju);
        djelimicnoOperativan = view.findViewById(R.id.djelimicniZastoj);

        save = view.findViewById(R.id.spremiti);

        mCurrentDate = Calendar.getInstance();
        mCurrentTime = Calendar.getInstance();

        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);


        rn = getArguments().getParcelableArrayList("radniNalog");
        position = getArguments().getInt("position");

        try {

            //postavljanje checkboxa "Na osnovu" na vec unesene vrijednosti
            switch (rn.get(position).getPoOsnovuId()) {
                case 1:
                    ugovor.setChecked(true);
                    garancija.setChecked(false);
                    poPozivu.setChecked(false);
                    break;
                case 2:
                    garancija.setChecked(true);
                    ugovor.setChecked(false);
                    poPozivu.setChecked(false);
                    break;
                case 3:
                    poPozivu.setChecked(true);
                    ugovor.setChecked(false);
                    garancija.setChecked(false);
            }
            //postavljanje checkboxa Status sistema" na vec unesene vrijednosti
            switch (rn.get(position).getStatusSistemaId()) {
                case 1:
                    potpunoUZastoju.setChecked(true);
                    djelimicnoOperativan.setChecked(false);
                    potpunoOperativan.setChecked(false);
                    break;
                case 2:
                    djelimicnoOperativan.setChecked(true);
                    potpunoOperativan.setChecked(false);
                    potpunoUZastoju.setChecked(false);
                    break;
                case 3:
                    potpunoOperativan.setChecked(true);
                    djelimicnoOperativan.setChecked(false);
                    potpunoUZastoju.setChecked(false);
            }

//            postavljanje polja na vec unesene vrijednosti
            brojNaloga.setText(rn.get(position).getBrojNaloga());
            dateInput.setText(getData.jsonDateFormat(rn.get(position).getDatumObrade()));
            odUnos.setText(rn.get(position).getVrijemePocetka());
            doUnos.setText(rn.get(position).getVrijemeKraja());
            unosMaterijala.setText(rn.get(position).getUgradjeniMAterijal());
            unosPrimjedbe.setText(rn.get(position).getPrimjedbe());

        } catch (Exception e) {
            e.printStackTrace();
        }

//          unos datuma
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear + 1;
                        dateInput.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

//          vrijeme pocetka
        odUnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        minOd = minute;
                        satOd = hourOfDay;
                        if ((hourOfDay >= 10) & (minute >= 10)) {
                            odUnos.setText(hourOfDay + ":" + minute);
                        } else if ((hourOfDay < 10) & (minute >= 10)) {
                            odUnos.setText("0" + hourOfDay + ":" + minute);
                        } else if ((hourOfDay >= 10) & (minute < 10)) {
                            odUnos.setText(hourOfDay + ":" + "0" + minute);
                        } else if ((hourOfDay < 10) & (minute < 10)) {
                            odUnos.setText("0" + hourOfDay + ":" + "0" + minute);
                        }
                    }
                }, hours, minutes, true);
                timePickerDialog.show();

            }
        });

//          vrijeme kraja
        doUnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        minDo = minute;
                        satDo = hourOfDay;
                        if ((hourOfDay >= 10) & (minute >= 10)) {
                            doUnos.setText(hourOfDay + ":" + minute);
                        } else if ((hourOfDay < 10) & (minute >= 10)) {
                            doUnos.setText("0" + hourOfDay + ":" + minute);
                        } else if ((hourOfDay >= 10) & (minute < 10)) {
                            doUnos.setText(hourOfDay + ":" + "0" + minute);
                        } else if ((hourOfDay < 10) & (minute < 10)) {
                            doUnos.setText("0" + hourOfDay + ":" + "0" + minute);
                        }
                    }
                }, hours, minutes, true);
                timePickerDialog.show();
            }
        });


//        spremanje
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ugovor.isChecked()) {
                        setPoOsnovuId = 1;
                    } else if (garancija.isChecked()) {
                        setPoOsnovuId = 2;
                    } else if (poPozivu.isChecked()) {
                        setPoOsnovuId = 3;
                    }

                    if (potpunoOperativan.isChecked()) {
                        setStatusSistemaId = 3;
                    } else if (djelimicnoOperativan.isChecked()) {
                        setStatusSistemaId = 2;
                    } else if (potpunoUZastoju.isChecked()) {
                        setStatusSistemaId = 1;
                    }

                    RadniNalog radniNalog = new RadniNalog(rn.get(position).getRadniNalogId(), brojNaloga.getText().toString(),
                            rn.get(position).getCovjekId(), rn.get(position).getFirmaId(), rn.get(position).getDatumZahtjeva(),
                            rn.get(position).getOpisProblema(), dateInput.getText().toString(),
                            odUnos.getText().toString(), doUnos.getText().toString(), setPoOsnovuId,
                            setStatusSistemaId, unosMaterijala.getText().toString(), unosPrimjedbe.getText().toString(), rn.get(position).getIsHitno());

                    CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
                    if (checkInternetConnection.checkConnection(getContext())) {
                        intent = new Intent(Intent.ACTION_SYNC, null, getContext(), SearchRadniNalog.class);
                        SearchResultReceiver mReceiver = new SearchResultReceiver(new Handler());
                        mReceiver.setReceiver(FragmentRadniNalog.this);
                        intent.putExtra("receiver", mReceiver);
                        intent.putExtra("category", "postZahtjev");
                        intent.putExtra("radniNalog", radniNalog);
                        intent.putExtra("urlPostRadniNalog", URL.saveEditRadniNalog + rn.get(position).getRadniNalogId());
                        getContext().startService(intent);


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



                    }else{
                        Toast.makeText(getContext(), "Provjerite internetsku vezu", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return view;
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


