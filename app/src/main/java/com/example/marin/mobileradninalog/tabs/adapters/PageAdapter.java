package com.example.marin.mobileradninalog.tabs.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.marin.mobileradninalog.model.Covjek;
import com.example.marin.mobileradninalog.model.Firma;
import com.example.marin.mobileradninalog.model.OpisPosla;
import com.example.marin.mobileradninalog.model.RadniNalog;
import com.example.marin.mobileradninalog.model.Stavka;
import com.example.marin.mobileradninalog.tabs.nalog.FragmentRadniNalog;
import com.example.marin.mobileradninalog.tabs.stavka.FragmentStavka;
import com.example.marin.mobileradninalog.tabs.zahtjev.FragmentZahtjev;

import java.util.ArrayList;

public class
PageAdapter extends FragmentStatePagerAdapter {
    private int numOfTabs;
    ArrayList<RadniNalog> radniNalog;
    ArrayList<Covjek> covjekList;
    ArrayList<Firma> firmaList;
    ArrayList<Stavka> stavkaList;
    ArrayList<OpisPosla> opisPoslaList;
    Bundle bundle;
    int itemPosition;

    public PageAdapter(FragmentManager fm, int numOfTabs, ArrayList<RadniNalog> radniNalog,
                       ArrayList<Covjek> covjekList, ArrayList<Firma> firmaList,
                       ArrayList<Stavka> stavkaList, ArrayList<OpisPosla> opisPoslaList,
                       int itemPosition) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.radniNalog = radniNalog;
        this.covjekList = covjekList;
        this.firmaList = firmaList;
        this.stavkaList = stavkaList;
        this.opisPoslaList = opisPoslaList;
        this.itemPosition = itemPosition;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentStavka fragmentStavka = new FragmentStavka();
                bundle = new Bundle();
                bundle.putParcelableArrayList("stavkaList", stavkaList);
                bundle.putParcelableArrayList("radniNalog", radniNalog);
                bundle.putParcelableArrayList("opisPosla", opisPoslaList);
                bundle.putInt("position", itemPosition);
                fragmentStavka.setArguments(bundle);
                return fragmentStavka;
            case 1:
                FragmentZahtjev fragmentZahtjev = new FragmentZahtjev();
                bundle = new Bundle();
                bundle.putParcelableArrayList("radniNalog", radniNalog);
                bundle.putParcelableArrayList("covjekList", covjekList);
                bundle.putParcelableArrayList("firmaList", firmaList);
                bundle.putInt("position", itemPosition);
                fragmentZahtjev.setArguments(bundle);
                return fragmentZahtjev;
            case 2:
                FragmentRadniNalog fragmentRadniNalog = new FragmentRadniNalog();
                bundle = new Bundle();
                bundle.putParcelableArrayList("radniNalog", radniNalog);
                bundle.putInt("position", itemPosition);
                fragmentRadniNalog.setArguments(bundle);
                return fragmentRadniNalog;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
