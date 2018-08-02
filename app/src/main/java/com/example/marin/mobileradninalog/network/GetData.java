package com.example.marin.mobileradninalog.network;

import com.example.marin.mobileradninalog.model.Covjek;
import com.example.marin.mobileradninalog.model.Firma;
import com.example.marin.mobileradninalog.model.OpisPosla;
import com.example.marin.mobileradninalog.model.RadniNalog;
import com.example.marin.mobileradninalog.model.Stavka;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GetData {
    ArrayList<RadniNalog> radniNalogList = new ArrayList<>();
    ArrayList<Firma> firmaList = new ArrayList<>();
    ArrayList<Covjek> covjekList = new ArrayList<>();
    ArrayList<Stavka> stavkaList = new ArrayList<>();
    ArrayList<OpisPosla> opisPoslaList = new ArrayList<>();
    JSONArray results;
    ConnectAndReceiveData connectAndReceiveData = new ConnectAndReceiveData();

    public ArrayList<RadniNalog> getRadniNalogList(String url) throws IOException, JSONException {
        results = connectAndReceiveData.connectAndReceiveData(url).getJSONArray("rn");
        for (int i = 0; i < results.length(); i++) {
            String datumZahtjeva = results.getJSONObject(i).get("datumZahtjeva").toString();
            String datumObrade = results.getJSONObject(i).get("datumObrade").toString();
            JSONObject vrijemePocetka = results.getJSONObject(i).getJSONObject("vrijemePocetka");
            JSONObject vrijemeKraja = results.getJSONObject(i).getJSONObject("vrijemeKraja");

//            if(results.getJSONObject(i).getString("brojNaloga")==""){
//
//            }

            radniNalogList.add(new RadniNalog(results.getJSONObject(i).getInt("radniNalogId"), results.getJSONObject(i).getString("brojNaloga"),
                    results.getJSONObject(i).getInt("covjekId"), results.getJSONObject(i).getInt("firmaId"),
                    jsonDateFormat(datumZahtjeva), results.getJSONObject(i).getString("opisProblema"),
                    datumObrade, jsonTimeFormat(vrijemePocetka), jsonTimeFormat(vrijemeKraja),
                    results.getJSONObject(i).getInt("poOsnovuId"), results.getJSONObject(i).getInt("statusSistemaId"),
                    results.getJSONObject(i).getString("ugradjeniMAterijal"), results.getJSONObject(i).getString("primjedbe"),
                    results.getJSONObject(i).getBoolean("isHitno"))
            );
        }
        return radniNalogList;
    }

    public ArrayList<Firma> getFirma(String url) throws IOException, JSONException {
        results = connectAndReceiveData.connectAndReceiveData(url).getJSONArray("rn");
        for (int i = 0; i < results.length(); i++) {
            firmaList.add(new Firma(results.getJSONObject(i).getInt("firmaId"), results.getJSONObject(i).getInt("idBroj"),
                    results.getJSONObject(i).getString("naziv"), results.getJSONObject(i).getString("adresa")));
        }
        return firmaList;
    }

    public ArrayList<Covjek> getCovjek(String url) throws IOException, JSONException {
        results = connectAndReceiveData.connectAndReceiveData(url).getJSONArray("rn");
        for (int i = 0; i < results.length(); i++) {
            covjekList.add(new Covjek(results.getJSONObject(i).getInt("covjekId"), results.getJSONObject(i).getString("prezime"),
                    results.getJSONObject(i).getString("ime")));
        }
        return covjekList;
    }

    public ArrayList<Stavka> getStavka(String url) throws IOException, JSONException {
        results = connectAndReceiveData.connectAndReceiveData(url).getJSONArray("rn");
        for (int i = 0; i < results.length(); i++) {
            stavkaList.add(new Stavka(results.getJSONObject(i).getInt("radniNalogStavkaId"), results.getJSONObject(i).getInt("radniNalogId"),
                    results.getJSONObject(i).getInt("opisPoslaId"), results.getJSONObject(i).getString("opisTekst")));
        }
        return stavkaList;
    }

    public ArrayList<OpisPosla> getOpisPoslaList(String url) throws IOException, JSONException {
        results = connectAndReceiveData.connectAndReceiveData(url).getJSONArray("rn");
        for (int i = 0; i < results.length(); i++) {
            opisPoslaList.add(new OpisPosla(results.getJSONObject(i).getInt("opisPoslaId"), results.getJSONObject(i).getString("nazivPosla"),
                    results.getJSONObject(i).getInt("aspNetFirmaId")));
        }
        return opisPoslaList;
    }

    public String jsonDateFormat(String jsonDate) {
        jsonDate = jsonDate.replace("/Date(", "").replace(")/", "");
        long time = Long.parseLong(jsonDate);
        Date d = new Date(time + 86400);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(d).toString();
    }

    public String jsonTimeFormat(JSONObject jsonTime) {
        String sHours, sMinutes;
        int hours, minutes;
        String time = "";
        try {
            hours = jsonTime.getInt("Hours");
            minutes = jsonTime.getInt("Minutes");
            if (hours < 10) {
                sHours = "0" + String.valueOf(hours);
            } else {
                sHours = String.valueOf(hours);
            }
            if (minutes < 10) {
                sMinutes = "0" + String.valueOf(minutes);
            } else {
                sMinutes = String.valueOf(minutes);
            }
            time = sHours + ":" + sMinutes;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return time;
    }
}
