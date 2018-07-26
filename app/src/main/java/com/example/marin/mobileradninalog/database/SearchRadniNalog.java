package com.example.marin.mobileradninalog.database;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.example.marin.mobileradninalog.Constants.URL;
import com.example.marin.mobileradninalog.model.Covjek;
import com.example.marin.mobileradninalog.model.Firma;
import com.example.marin.mobileradninalog.model.OpisPosla;
import com.example.marin.mobileradninalog.model.RadniNalog;
import com.example.marin.mobileradninalog.model.Stavka;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchRadniNalog extends IntentService {
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    public SearchRadniNalog() {
        super(null);
    }

    public SearchRadniNalog(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("RestrictedApi")
    @Override
    protected void onHandleIntent(Intent intent) {
        GetData getData = new GetData();
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Bundle bundle = new Bundle();
        receiver.send(STATUS_RUNNING, Bundle.EMPTY);

        String urlRadniNalog = intent.getExtras().getString("urlRadniNalog");
        String urlCovjek = intent.getExtras().getString("urlCovjek");
        String urlFirma = intent.getExtras().getString("urlFirma");
        String urlPostRadniNalog = intent.getExtras().getString("urlPostRadniNalog");
        String urlGetStavka = intent.getExtras().getString("urlGetStavka");
        String urlGetOpisPosla = intent.getExtras().getString("urlGetOpisPosla");
        String urlUpdateRadniNalog = intent.getExtras().getString("urlUpdateRadniNalog");
        String urlDeleteRadniNalog = intent.getExtras().getString("urlDeleteRadniNalog");
        String urlSaveRadniNalogStavka = intent.getExtras().getString("urlSaveRadniNalogStavka");
        String urlDeleteRadniNalogStavka = intent.getExtras().getString("urlDeleteRadniNalogStavka");
        String urlUpdateRadniNalogStavka = intent.getExtras().getString("urlUpdateRadniNalogStavka");

        RadniNalog radniNalog = intent.getExtras().getParcelable("radniNalog");
        Stavka radniNalogStavka = intent.getExtras().getParcelable("radniNalogStavka");

        String category = "";

        if (intent.getExtras().getString("category") != null)
            category = intent.getExtras().getString("category");

        try {

            switch (category) {
                //dohvacanje liste zaposlenika i firmi
                case "getRadniNalog":
                    ArrayList<RadniNalog> radniNalogList = getData.getRadniNalogList(urlRadniNalog);
                    bundle.putSerializable("radninalozi", radniNalogList);
                    bundle.putSerializable("category", category);
                    break;

                //dohvacanje liste radnih naloga
                case "postZahtjev":
                    SendData sendData = new SendData();
                    sendData.sendJson(urlPostRadniNalog, radniNalog);
                    bundle.putSerializable("category", "postZahtjev");
                    break;

                case "getData":
                    ArrayList<Covjek> covjek = getData.getCovjek(urlCovjek);
                    ArrayList<Firma> firma = getData.getFirma(urlFirma);
                    ArrayList<Stavka> stavka = getData.getStavka(urlGetStavka);
                    ArrayList<OpisPosla> opisPosla = getData.getOpisPoslaList(urlGetOpisPosla);

                    bundle.putSerializable("firme", firma);
                    bundle.putSerializable("zaposlenici", covjek);
                    bundle.putSerializable("stavke", stavka);
                    bundle.putSerializable("opisPosla", opisPosla);
                    bundle.putSerializable("category", "getData");
                    break;

                case "getCovjekFirma":
                    ArrayList<Covjek> covjek1 = getData.getCovjek(urlCovjek);
                    ArrayList<Firma> firma1 = getData.getFirma(urlFirma);

                    bundle.putSerializable("firme", firma1);
                    bundle.putSerializable("zaposlenici", covjek1);
                    bundle.putSerializable("category", "getCovjekFirma");
                    break;

                case "updateRadniNalog":
                    UpdateData updateData = new UpdateData();
                    updateData.updateColumn(urlUpdateRadniNalog, radniNalog);
                    break;

                case "deleteRadniNalogStavka":
                    DeleteData deleteData = new DeleteData();
                    deleteData.deleteData(urlDeleteRadniNalogStavka);
                    break;

                case "saveRadniNalogStavka":
                    SendData sendData1 = new SendData();
                    sendData1.sendJson(urlSaveRadniNalogStavka, radniNalogStavka);
                    break;

                case "editStavka":
                    SendData sendData2 = new SendData();
                    sendData2.sendJson(urlUpdateRadniNalogStavka, radniNalogStavka);
                    break;
            }

            Log.w("TU SAM RODIJAK", "EVO MENE MOJI LJUDI");

            receiver.send(STATUS_FINISHED, bundle);
        } catch (IOException | JSONException e) {
            bundle.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(STATUS_ERROR, bundle);
        }

    }

}
