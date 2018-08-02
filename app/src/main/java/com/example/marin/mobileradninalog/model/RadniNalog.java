package com.example.marin.mobileradninalog.model;


import android.os.Parcel;
import android.os.Parcelable;

public class RadniNalog implements Parcelable {
    private int radniNalogId;
    private String brojNaloga;
    private int covjekId;
    private int firmaId;
    private String datumZahtjeva;
    private String opisProblema;
    private String datumObrade;
    private String vrijemePocetka;
    private String vrijemeKraja;
    private int poOsnovuId;
    private int statusSistemaId;
    private String ugradjeniMAterijal;
    private String primjedbe;
    private boolean isHitno;

    public RadniNalog(int radniNalogId, String brojNaloga, int covjekId, int firmaId, String datumZahtjeva,
                      String opisProblema, String datumObrade, String vrijemePocetka, String vrijemeKraja,
                      int poOsnovuId, int statusSistemaId, String ugradjeniMAterijal, String primjedbe, boolean isHitno) {

        this.radniNalogId = radniNalogId;
        this.brojNaloga = brojNaloga;
        this.covjekId = covjekId;
        this.firmaId = firmaId;
        this.datumZahtjeva = datumZahtjeva;
        this.opisProblema = opisProblema;
        this.datumObrade = datumObrade;
        this.vrijemePocetka = vrijemePocetka;
        this.vrijemeKraja = vrijemeKraja;
        this.poOsnovuId = poOsnovuId;
        this.statusSistemaId = statusSistemaId;
        this.ugradjeniMAterijal = ugradjeniMAterijal;
        this.primjedbe = primjedbe;
        this.isHitno = isHitno;
    }

    public RadniNalog(String brojNaloga, int covjekId, int firmaId, String datumZahtjeva,
                      String opisProblema, String datumObrade, String vrijemePocetka, String vrijemeKraja,
                      int poOsnovuId, int statusSistemaId, String ugradjeniMAterijal, String primjedbe, boolean isHitno) {

        this.brojNaloga = brojNaloga;
        this.covjekId = covjekId;
        this.firmaId = firmaId;
        this.datumZahtjeva = datumZahtjeva;
        this.opisProblema = opisProblema;
        this.datumObrade = datumObrade;
        this.vrijemePocetka = vrijemePocetka;
        this.vrijemeKraja = vrijemeKraja;
        this.poOsnovuId = poOsnovuId;
        this.statusSistemaId = statusSistemaId;
        this.ugradjeniMAterijal = ugradjeniMAterijal;
        this.primjedbe = primjedbe;
        this.isHitno = isHitno;
    }

    public RadniNalog (int covjekId, int firmaId, String datumZahtjeva, String opisProblema, boolean isHitno) {
        this.covjekId = covjekId;
        this.firmaId = firmaId;
        this.datumZahtjeva = datumZahtjeva;
        this.opisProblema = opisProblema;
        this.isHitno = isHitno;
    }

    public int getRadniNalogId() {
        return radniNalogId;
    }

    public String getBrojNaloga() {
        return brojNaloga;
    }

    public void setBrojNaloga(String brojNaloga) {
        this.brojNaloga = brojNaloga;
    }

    public int getCovjekId() {
        return covjekId;
    }

    public int getFirmaId() {
        return firmaId;
    }

    public String getDatumZahtjeva() {
        return datumZahtjeva;
    }

    public String getOpisProblema() {
        return opisProblema;
    }

    public String getDatumObrade() {
        return datumObrade;
    }

    public String getVrijemePocetka() {
        return vrijemePocetka;
    }

    public String getVrijemeKraja() {
        return vrijemeKraja;
    }

    public int getPoOsnovuId() {
        return poOsnovuId;
    }

    public int getStatusSistemaId() {return statusSistemaId;}

    public String getUgradjeniMAterijal() {
        return ugradjeniMAterijal;
    }

    public String getPrimjedbe() {
        return primjedbe;
    }

    public boolean getIsHitno() {return isHitno;}

    public RadniNalog(Parcel in) {

        radniNalogId = in.readInt();
        brojNaloga = in.readString();
        covjekId = in.readInt();
        firmaId = in.readInt();
        datumZahtjeva = in.readString();
        opisProblema = in.readString();
        datumObrade = in.readString();
        vrijemePocetka = in.readString();
        vrijemeKraja = in.readString();
        poOsnovuId = in.readInt();
        statusSistemaId = in.readInt();
        ugradjeniMAterijal = in.readString();
        primjedbe = in.readString();
        isHitno = (Boolean) in.readValue( null );
    }

    public static final Creator<RadniNalog> CREATOR = new Creator<RadniNalog>() {
        @Override
        public RadniNalog createFromParcel(Parcel in) {
            return new RadniNalog(in);
        }

        @Override
        public RadniNalog[] newArray(int size) {
            return new RadniNalog[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(radniNalogId);
        dest.writeString(brojNaloga);
        dest.writeInt(covjekId);
        dest.writeInt(firmaId);
        dest.writeString(datumZahtjeva);
        dest.writeString(opisProblema);
        dest.writeString(datumObrade);
        dest.writeString(vrijemePocetka);
        dest.writeString(vrijemeKraja);
        dest.writeInt(poOsnovuId);
        dest.writeInt(statusSistemaId);
        dest.writeString(ugradjeniMAterijal);
        dest.writeString(primjedbe);
        dest.writeValue(isHitno);
    }
}
