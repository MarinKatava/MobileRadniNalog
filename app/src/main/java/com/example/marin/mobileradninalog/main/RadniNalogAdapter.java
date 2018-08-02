package com.example.marin.mobileradninalog.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marin.mobileradninalog.R;
import com.example.marin.mobileradninalog.network.SearchResultReceiver;
import com.example.marin.mobileradninalog.model.RadniNalog;
import com.example.marin.mobileradninalog.tabs.TabActivity;

import java.io.File;
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
                File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "RadniNalog" + radniNalog.get(position).getBrojNaloga() + ".pdf");
                Uri path = Uri.fromFile(filelocation);
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("vnd.android.cursor.dir/email");
                if (path != null) {
                    emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                } else {
                    Toast.makeText(context, "File ne postoji. Prvo preuzmite radni nalog na uređaj, zatim ponovno izvršite slanje.", Toast.LENGTH_SHORT).show();
                }
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Radni nalog");
                context.startActivity(Intent.createChooser(emailIntent, "Pošaljite radni nalog broj " + radniNalog.get(position).getBrojNaloga()));
            }
        });

        return convertView;
    }

}

