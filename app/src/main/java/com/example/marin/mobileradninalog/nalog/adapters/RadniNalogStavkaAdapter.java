package com.example.marin.mobileradninalog.nalog.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.marin.mobileradninalog.Constants.URL;
import com.example.marin.mobileradninalog.R;
import com.example.marin.mobileradninalog.database.SearchRadniNalog;
import com.example.marin.mobileradninalog.database.SearchResultReceiver;
import com.example.marin.mobileradninalog.model.OpisPosla;
import com.example.marin.mobileradninalog.model.Stavka;
import com.example.marin.mobileradninalog.nalog.EditStavka;
import com.example.marin.mobileradninalog.nalog.ItemTextActivity;
import com.example.marin.mobileradninalog.nalog.tabs.TabActivity;

import java.util.ArrayList;

public class RadniNalogStavkaAdapter extends ArrayAdapter implements SearchResultReceiver.Receiver {
    private ArrayList<Stavka> radniNalogStavkaList;
    private ArrayList<OpisPosla> opisPoslaList;
    private String nazivPosla;
    private int radniNalogId;
    private Context context;
    private Resources res;
    int indexOfObject;
    int opisPoslaId;


    public RadniNalogStavkaAdapter(Context context, int resource, ArrayList<Stavka> radniNalogStavkaList, ArrayList<OpisPosla> opisPoslaList, int radniNalogId, Resources res) {
        super(context, resource, radniNalogStavkaList);
        this.radniNalogStavkaList = radniNalogStavkaList;
        this.opisPoslaList = opisPoslaList;
        this.radniNalogId = radniNalogId;
        this.context = context;
        this.res = res;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        notifyDataSetChanged();

    }

    public class Holder {
        TextView redniBrojStavke;
        TextView stavkaId;
        TextView nazivPosla;
        TextView stavkaTekst;
        Button deleteStavka;
        Button editStavka;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder h = new Holder();

        View rowView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.listview_stavka, null);

        h.redniBrojStavke = rowView.findViewById(R.id.redniBrojStavke);
        h.stavkaId = rowView.findViewById(R.id.stavkaId);
        h.nazivPosla = rowView.findViewById(R.id.nazivPosla);
        h.stavkaTekst = rowView.findViewById(R.id.stavkaTekst);
        h.deleteStavka = rowView.findViewById(R.id.deleteItem);
        h.editStavka = rowView.findViewById(R.id.editStavka);

        h.redniBrojStavke.setText(String.valueOf(position + 1));
        h.stavkaId.setText("ID stavke: " + String.valueOf(radniNalogStavkaList.get(position).getRadniNalogStavkaId()));
        h.stavkaTekst.setText(radniNalogStavkaList.get(position).getOpisTekst().toString());

//        dohvacanje naziva posla za odredenu stavku
        int opisPoslaId = radniNalogStavkaList.get(position).getOpisPoslaId();

        for (int i = 0; i < opisPoslaList.size(); i++) {
            if (opisPoslaList.get(i).getOpisPoslaId() == opisPoslaId) {
                indexOfObject = opisPoslaList.indexOf(opisPoslaList.get(i));
            }
        }
        h.nazivPosla.setText("Naziv posla: " + opisPoslaList.get(indexOfObject).getNazivPosla().toString());

//        otvaranje teksta opisa posla
        h.stavkaTekst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ItemTextActivity.class);
                i.putExtra("stavkaTekst", radniNalogStavkaList.get(position).getOpisTekst());
                getContext().startActivity(i);
            }
        });

//        brisanje stavke
        h.deleteStavka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                builder.setTitle("Obrisati stavku?").setMessage("Promjene vidljive pri sljedećem otvaranju naloga.").setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_SYNC, null, getContext(), SearchRadniNalog.class);
                        SearchResultReceiver mReceiver = new SearchResultReceiver(new Handler());
                        mReceiver.setReceiver(RadniNalogStavkaAdapter.this);
                        intent.putExtra("receiver", mReceiver);
                        intent.putExtra("category", "postZahtjev");
                        intent.putExtra("radniNalog", radniNalogStavkaList);
                        intent.putExtra("urlPostRadniNalog", URL.deleteRadniNalogStavka + radniNalogStavkaList.get(position).getRadniNalogStavkaId());
                        getContext().startService(intent);
                        getContext().stopService(intent);

                    }
                }).setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
        });


//        uredjivanje stavke
        h.editStavka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int opisPoslaId = radniNalogStavkaList.get(position).getOpisPoslaId();
                for (int i = 0; i < opisPoslaList.size(); i++) {
                    if (opisPoslaList.get(i).getOpisPoslaId() == opisPoslaId) {
                        indexOfObject = opisPoslaList.indexOf(opisPoslaList.get(i));
                    }
                }
                Intent intent = new Intent(getContext(), EditStavka.class);
                intent.putExtra("opisPoslaList", opisPoslaList);
                intent.putExtra("radniNalogStavkaList", radniNalogStavkaList);
                intent.putExtra("radniNalogId", radniNalogId);
                intent.putExtra("nazivPosla", opisPoslaList.get(indexOfObject).getNazivPosla().toString());
                intent.putExtra("stavkaTekst", radniNalogStavkaList.get(position).getOpisTekst());
                intent.putExtra("position", position);
                getContext().startActivity(intent);
            }
        });

        return rowView;
    }
}
