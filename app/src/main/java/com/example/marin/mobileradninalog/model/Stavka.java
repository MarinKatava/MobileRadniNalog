package com.example.marin.mobileradninalog.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Stavka implements Parcelable {
    private int stavkaId;
    private int radniNalogId;
    private int opisPoslaId;
    private String opisTekst;

    public Stavka(int stavkaId, int radniNalogId, int opisPoslaId, String opisTekst) {
        this.stavkaId = stavkaId;
        this.radniNalogId = radniNalogId;
        this.opisPoslaId = opisPoslaId;
        this.opisTekst = opisTekst;
    }

    public Stavka(int radniNalogId, int opisPoslaId, String opisTekst) {
        this.radniNalogId = radniNalogId;
        this.opisPoslaId = opisPoslaId;
        this.opisTekst = opisTekst;
    }

    public int getStavkaId() {
        return stavkaId;
    }

    public int getRadniNalogId() {
        return radniNalogId;
    }

    public int getOpisPoslaId() {
        return opisPoslaId;
    }

    public String getOpisTekst() {
        return opisTekst;
    }

    protected Stavka(Parcel in) {

        stavkaId = in.readInt();
        radniNalogId = in.readInt();
        opisPoslaId = in.readInt();
        opisTekst = in.readString();
    }

    public static final Creator<Stavka> CREATOR = new Creator<Stavka>() {
        @Override
        public Stavka createFromParcel(Parcel in) {
            return new Stavka(in);
        }

        @Override
        public Stavka[] newArray(int size) {
            return new Stavka[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(stavkaId);
        parcel.writeInt(radniNalogId);
        parcel.writeInt(opisPoslaId);
        parcel.writeString(opisTekst);
    }
}
