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
import com.example.marin.mobileradninalog.model.Covjek;

import java.util.ArrayList;

public class PeopleSpinnerAdapter extends ArrayAdapter<Covjek> {

    ArrayList<Covjek> covjekList;
    private final LayoutInflater mInflater;
    private final int mResource;

    public PeopleSpinnerAdapter(@NonNull Context context, int resource, ArrayList<Covjek> covjekList) {
        super(context, resource, 0, covjekList);
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        this.covjekList = covjekList;
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
        textView.setText(covjekList.get(position).getIme().toString() + " " + covjekList.get(position).getPrezime().toString());
        return view;
    }
}