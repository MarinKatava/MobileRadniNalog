package com.example.marin.mobileradninalog.tabs.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.marin.mobileradninalog.R;
import com.example.marin.mobileradninalog.model.Firma;

import java.util.ArrayList;

public class FirmaSpinnerAdapter extends ArrayAdapter<Firma> {
    ArrayList<Firma> firmaList;
    private final LayoutInflater mInflater;
    private final int mResource;

    public FirmaSpinnerAdapter(@NonNull Context context, int resource, ArrayList<Firma> firmaList) {
        super(context, resource, 0, firmaList);
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        this.firmaList = firmaList;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull
    View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        final View view = mInflater.inflate(mResource, parent, false);

        TextView textView = view.findViewById(R.id.spinnerItem);
        textView.setText(firmaList.get(position).getNaziv().toString());
        return view;
    }
}
