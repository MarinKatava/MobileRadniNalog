package com.example.marin.mobileradninalog.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Covjek implements Parcelable {
    private int covjekId;
    private String prezime;
    private String ime;

    public Covjek(int covjekId, String prezime, String ime) {
        this.covjekId = covjekId;
        this.prezime = prezime;
        this.ime = ime;
    }

    public int getCovjekId() {
        return covjekId;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    protected Covjek(Parcel in) {
        covjekId = in.readInt();
        prezime = in.readString();
        ime = in.readString();
    }

    public static final Creator<Covjek> CREATOR = new Creator<Covjek>() {
        @Override
        public Covjek createFromParcel(Parcel in) {
            return new Covjek(in);
        }

        @Override
        public Covjek[] newArray(int size) {
            return new Covjek[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(covjekId);
        parcel.writeString(prezime);
        parcel.writeString(ime);
    }
}
