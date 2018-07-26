package com.example.marin.mobileradninalog.main;

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
import com.example.marin.mobileradninalog.model.Covjek;
import com.example.marin.mobileradninalog.model.Firma;
import com.example.marin.mobileradninalog.model.RadniNalog;
import com.example.marin.mobileradninalog.model.Stavka;
import com.example.marin.mobileradninalog.nalog.tabs.TabActivity;

import java.util.ArrayList;

public class RadniNalogAdapter extends ArrayAdapter implements SearchResultReceiver.Receiver {
    private ArrayList<RadniNalog> radniNalog;
    private Context context;
    private Resources res;

    public RadniNalogAdapter(Context context, int resource, ArrayList<RadniNalog> radniNalog, Resources res) {
        super(context, resource, radniNalog);
        this.radniNalog = radniNalog;
        this.context = context;
        this.res = res;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

    }

    public class Holder {
        TextView id, datumZahtjeva, redniBroj;
        Button open, send;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder h = new Holder();
//        View rowView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_item, null);

        h.redniBroj = convertView.findViewById(R.id.redniBroj);
        h.id = convertView.findViewById(R.id.kupac);
        h.datumZahtjeva = convertView.findViewById(R.id.datum);
        h.open = convertView.findViewById(R.id.open);
        h.send = convertView.findViewById(R.id.posalji);
        h.redniBroj.setText(String.valueOf(position + 1));

        if (radniNalog.get(position).getBrojNaloga() != null) {
            h.id.setText("Broj naloga: " + String.valueOf(radniNalog.get(position).getBrojNaloga()));
        }
        h.datumZahtjeva.setText("Datum zahtjeva: " + radniNalog.get(position).getDatumZahtjeva().toString());

        h.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TabActivity.class);
                intent.putExtra("position", position);
                intent.putParcelableArrayListExtra("radniNalogList", radniNalog);
                context.startActivity(intent);
            }
        });

        h.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                builder.setMessage("Obrisati stavku?").setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_SYNC, null, getContext(), SearchRadniNalog.class);
                        SearchResultReceiver mReceiver = new SearchResultReceiver(new Handler());
                        mReceiver.setReceiver(RadniNalogAdapter.this);
                        intent.putExtra("receiver", mReceiver);
                        intent.putExtra("category", "deleteRadniNalog");
                        intent.putExtra("radniNalog", radniNalog);
                        intent.putExtra("urlDeleteRadniNalog", URL.deleteRadniNalog + radniNalog.get(position).getRadniNalogId());
                        getContext().startService(intent);
                    }
                }).setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
                return true;
            }
        });


        return convertView;
    }

}
