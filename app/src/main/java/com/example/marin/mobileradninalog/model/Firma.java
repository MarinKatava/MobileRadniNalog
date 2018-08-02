package com.example.marin.mobileradninalog.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Firma implements Parcelable {
    private int firmaId;
    private int idBroj;
    private String naziv;
    private String adresa;

    public int getFirmaId() {
        return firmaId;
    }

    public int getIdBroj() {
        return idBroj;
    }

    public String getNaziv() {
        return naziv;
    }

    public String getAdresa() {
        return adresa;
    }

    public Firma(int firmaId, int idBroj, String naziv, String adresa) {
        this.firmaId = firmaId;
        this.idBroj = idBroj;
        this.naziv = naziv;
        this.adresa = adresa;
    }

    protected Firma(Parcel in) {
        firmaId = in.readInt();
        idBroj = in.readInt();
        naziv = in.readString();
        adresa = in.readString();
    }

    public static final Creator<Firma> CREATOR = new Creator<Firma>() {
        @Override
        public Firma createFromParcel(Parcel in) {
            return new Firma(in);
        }

        @Override
        public Firma[] newArray(int size) {
            return new Firma[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(firmaId);
        parcel.writeInt(idBroj);
        parcel.writeString(naziv);
        parcel.writeString(adresa);
    }
}
