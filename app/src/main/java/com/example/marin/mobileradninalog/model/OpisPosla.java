package com.example.marin.mobileradninalog.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OpisPosla implements Parcelable {
    private int opisPoslaId;
    private String nazivPosla;
    private int aspNetFirmaId;

    public OpisPosla(int opisPoslaId, String nazivPosla, int aspNetFirmaId) {

        this.opisPoslaId = opisPoslaId;
        this.nazivPosla = nazivPosla;
        this.aspNetFirmaId = aspNetFirmaId;
    }

    public int getOpisPoslaId() {
        return opisPoslaId;
    }

    public String getNazivPosla() {
        return nazivPosla;
    }

    public int getAspNetFirmaId() {
        return aspNetFirmaId;
    }

    protected OpisPosla(Parcel in) {
        opisPoslaId = in.readInt();
        nazivPosla = in.readString();
        aspNetFirmaId = in.readInt();
    }

    public static final Creator<OpisPosla> CREATOR = new Creator<OpisPosla>() {
        @Override
        public OpisPosla createFromParcel(Parcel in) {
            return new OpisPosla(in);
        }

        @Override
        public OpisPosla[] newArray(int size) {
            return new OpisPosla[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(opisPoslaId);
        parcel.writeString(nazivPosla);
        parcel.writeInt(aspNetFirmaId);
    }
}
