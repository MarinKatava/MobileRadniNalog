package com.example.marin.mobileradninalog.nalog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import com.example.marin.mobileradninalog.Constants.URL;
import com.example.marin.mobileradninalog.R;
import com.example.marin.mobileradninalog.database.GetData;
import com.example.marin.mobileradninalog.database.SearchRadniNalog;
import com.example.marin.mobileradninalog.database.SearchResultReceiver;
import com.example.marin.mobileradninalog.main.MainActivity;
import com.example.marin.mobileradninalog.main.RadniNalogAdapter;
import com.example.marin.mobileradninalog.model.Covjek;
import com.example.marin.mobileradninalog.model.Firma;
import com.example.marin.mobileradninalog.model.OpisPosla;
import com.example.marin.mobileradninalog.model.RadniNalog;
import com.example.marin.mobileradninalog.model.Stavka;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Marin Katava on 22.5.2018..
 */

public class WriteToPdf {
    Context context;
    int position;
    RadniNalog radniNalog;
    ArrayList<RadniNalog> radniNalogList;
    ArrayList<Stavka> stavkaList;
    ArrayList<Firma> firmaList;
    ArrayList<Covjek> covjekList;
    ArrayList<OpisPosla> opisPoslaList;
    String odgovornaOsoba;
    String nazivFirme;
    int stavkaNumber = 1;

    GetData getData = new GetData();

    public Boolean writeToPdf(RadniNalog radniNalog, ArrayList<Stavka> stavkaList, ArrayList<Firma> firmaList, ArrayList<Covjek> covjekList, ArrayList<OpisPosla> opisPoslaList) throws ParseException {
        this.radniNalog = radniNalog;
        this.stavkaList = stavkaList;
        this.firmaList = firmaList;
        this.covjekList = covjekList;
        this.opisPoslaList = opisPoslaList;


//        dohvacanje naziva zaposlenika iz ID-ja
        for (int i = 0; i < covjekList.size(); i++) {
            if (radniNalog.getCovjekId() == covjekList.get(i).getCovjekId()) {
                odgovornaOsoba = covjekList.get(i).getIme() + " " + covjekList.get(i).getPrezime();
            }
        }
//        dohvacanje naziva firme iz ID-ja
        for (int i = 0; i < firmaList.size(); i++) {
            if (radniNalog.getFirmaId() == firmaList.get(i).getFirmaId()) {
                nazivFirme = firmaList.get(i).getNaziv();
            }
        }

        try {
//            kreiranje lokacije spremanja filea
            String fpath = "/sdcard/" + "RadniNalog" + String.valueOf(radniNalog.getBrojNaloga()) + ".pdf";
//            String fpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/RadniNalog";
            File file = new File(fpath);
            if (!file.exists()) {
                file.createNewFile();
            }

            Font title = new Font(Font.FontFamily.TIMES_ROMAN,
                    20, Font.BOLD, BaseColor.BLACK);
            Font textFont = new Font(Font.FontFamily.TIMES_ROMAN,
                    12);
            Document document = new Document();
            PdfWriter.getInstance(document,
                    new FileOutputStream(file.getAbsoluteFile()));

//            otvaranje dokumenta
            document.open();

//            upisivanje u dokument
            document.add(new Paragraph(Element.ALIGN_CENTER, "Radni nalog" + "\n", title));
            document.add(new Paragraph(" "));
//            broj naloga
            document.add(new Paragraph("Broj naloga: " + radniNalog.getBrojNaloga(), textFont));
//            odgovorna osoba
            document.add(new Paragraph("Odgovorna osoba: " + odgovornaOsoba, textFont));
//            naziv firme
            document.add(new Paragraph("Naziv firme: " + nazivFirme, textFont));
//            datum zahtjeva
            document.add(new Paragraph("Datum zahtjeva: " + radniNalog.getDatumZahtjeva(), textFont));
//            opis problema
            document.add(new Paragraph("Opis problema: " + radniNalog.getOpisProblema(), textFont));
//            datum obrade
            document.add(new Paragraph("Datum obrade: " + getData.jsonDateFormat(radniNalog.getDatumObrade()), textFont));
//            vrijeme pocetka
            document.add(new Paragraph("Vrijeme pocetka: " + radniNalog.getVrijemePocetka() + " h", textFont));
//            vrijeme kraja
            document.add(new Paragraph("Vrijeme kraja: " + radniNalog.getVrijemeKraja() + " h", textFont));
//            ugradjeni materijal
            document.add(new Paragraph("Ugradeni materijal: " + radniNalog.getUgradjeniMAterijal(), textFont));
//            primjedbe
            document.add(new Paragraph("Primjedbe: " + radniNalog.getPrimjedbe(), textFont));

//            po osnovu
            switch (radniNalog.getPoOsnovuId()) {
                case 1:
                    document.add(new Paragraph("Po osnovu: Ugovor", textFont));
                    break;
                case 2:
                    document.add(new Paragraph("Po osnovu: Garancija", textFont));
                    break;
                case 3:
                    document.add(new Paragraph("Po osnovu: Po pozivu", textFont));
                    break;
            }

//            status sistema
            switch (radniNalog.getStatusSistemaId()) {
                case 1:
                    document.add(new Paragraph("Status sistema: Potpuno u zastoju", textFont));
                    break;
                case 2:
                    document.add(new Paragraph("Status sistema: Djelimicno operativan", textFont));
                    break;
                case 3:
                    document.add(new Paragraph("Status sistema: Potpuno operativan", textFont));
                    break;
            }

//            isHitno
            if (radniNalog.getIsHitno() == true) {
                document.add(new Paragraph("Intervencija: Hitna", textFont));
            } else {
                document.add(new Paragraph("Intervencija: Normalna", textFont));
            }

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Stavke", title));
            document.add(new Paragraph(" "));

//            stavke
            for (int i = 0; i < stavkaList.size(); i++) {
                if (radniNalog.getRadniNalogId() == stavkaList.get(i).getRadniNalogId()) {
                    document.add(new Paragraph("Stavka " + String.valueOf(stavkaNumber) + ":"));
                    document.add(new Paragraph(stavkaList.get(i).getOpisTekst(), textFont));
                    document.add(new Paragraph(" "));
                }
                ++stavkaNumber;
            }

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

//            potpis
            document.add(new Paragraph("____________________"));
            document.add(new Paragraph("          Potpis ", textFont));


//            zatvaranje dokumenta
            document.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (DocumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}


