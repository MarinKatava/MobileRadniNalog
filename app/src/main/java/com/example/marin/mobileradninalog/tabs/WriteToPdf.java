package com.example.marin.mobileradninalog.tabs;

import android.content.Context;

import com.example.marin.mobileradninalog.network.GetData;
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
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import static com.itextpdf.text.pdf.PdfContentByte.ALIGN_CENTER;

/**
 * Created by Marin Katava on 22.5.2018..
 */

public class WriteToPdf {
    RadniNalog radniNalog;
    ArrayList<Stavka> stavkaList;
    ArrayList<Firma> firmaList;
    ArrayList<Covjek> covjekList;
    ArrayList<OpisPosla> opisPoslaList;
    String odgovornaOsoba;
    String nazivFirme;
    int stavkaNumber = 1;

    GetData getData = new GetData();

    public Boolean writeToPdf(RadniNalog radniNalog, ArrayList<Stavka> stavkaList, ArrayList<Firma> firmaList, ArrayList<Covjek> covjekList, ArrayList<OpisPosla> opisPoslaList) {
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
            Font items = new Font(Font.FontFamily.TIMES_ROMAN,
                    15, Font.BOLD, BaseColor.BLACK);
            Font textFont = new Font(Font.FontFamily.TIMES_ROMAN,
                    12);
            Document document = new Document();
            PdfWriter.getInstance(document,
                    new FileOutputStream(file.getAbsoluteFile()));

//            otvaranje dokumenta
            document.open();

//            dodavanje naslova Radni nalog
            Paragraph para = new Paragraph("Radni nalog", title);
            para.setAlignment(ALIGN_CENTER);
            para.setLeading(0, 1);
            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell();
            cell.setMinimumHeight(35);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.addElement(para);
            table.addCell(cell);
            document.add(table);


//            upisivanje u dokument
            document.add(new Paragraph(" "));
//            broj naloga
            document.add(new Paragraph("Broj naloga: ", items));
            document.add(new Paragraph(radniNalog.getBrojNaloga(), textFont));
//            odgovorna osoba
            document.add(new Paragraph("Odgovorna osoba: ", items));
            document.add(new Paragraph(odgovornaOsoba, textFont));
//            naziv firme
            document.add(new Paragraph("Naziv firme: ", items));
            document.add(new Paragraph(nazivFirme, textFont));
//            datum zahtjeva
            document.add(new Paragraph("Datum zahtjeva: ", items));
            document.add(new Paragraph(radniNalog.getDatumZahtjeva(), textFont));
//            opis problema
            document.add(new Paragraph("Opis problema: ", items));
            document.add(new Paragraph(radniNalog.getOpisProblema(), textFont));
//            datum obrade
            document.add(new Paragraph("Datum obrade: ", items));
            document.add(new Paragraph(getData.jsonDateFormat(radniNalog.getDatumObrade()), textFont));
//            vrijeme pocetka
            document.add(new Paragraph("Vrijeme pocetka: ", items));
            document.add(new Paragraph(radniNalog.getVrijemePocetka() + " h", textFont));
//            vrijeme kraja
            document.add(new Paragraph("Vrijeme kraja: ", items));
            document.add(new Paragraph(radniNalog.getVrijemeKraja() + " h", textFont));
//            ugradjeni materijal
            document.add(new Paragraph("Ugradeni materijal: ", items));
            document.add(new Paragraph(radniNalog.getUgradjeniMAterijal(), textFont));
//            primjedbe
            document.add(new Paragraph("Primjedbe: ", items));
            document.add(new Paragraph(radniNalog.getPrimjedbe(), textFont));

//            po osnovu
            switch (radniNalog.getPoOsnovuId()) {
                case 1:
                    document.add(new Paragraph("Po osnovu: ", items));
                    document.add(new Paragraph("Ugovor", textFont));
                    break;
                case 2:
                    document.add(new Paragraph("Po osnovu: a", items));
                    document.add(new Paragraph("Garancija", textFont));
                    break;
                case 3:
                    document.add(new Paragraph("Po osnovu: ", items));
                    document.add(new Paragraph("Po pozivu", textFont));
                    break;
            }

//            status sistema
            switch (radniNalog.getStatusSistemaId()) {
                case 1:
                    document.add(new Paragraph("Status sistema: ", items));
                    document.add(new Paragraph("Potpuno u zastoju", textFont));
                    break;
                case 2:
                    document.add(new Paragraph("Status sistema: ", items));
                    document.add(new Paragraph("Djelimicno operativan", textFont));
                    break;
                case 3:
                    document.add(new Paragraph("Status sistema: ", items));
                    document.add(new Paragraph("Potpuno operativan", textFont));
                    break;
            }

//            isHitno
            if (radniNalog.getIsHitno() == true) {
                document.add(new Paragraph("Intervencija:", items));
                document.add(new Paragraph("Hitna", textFont));

            } else {
                document.add(new Paragraph("Intervencija: ", items));
                document.add(new Paragraph("Normalna", textFont));
            }

//            naslov Stavke
            document.add(new Paragraph(" "));
            Paragraph para1 = new Paragraph("Stavke", title);
            para1.setAlignment(ALIGN_CENTER);
            para1.setLeading(0, 1);
            PdfPTable table1 = new PdfPTable(1);
            table1.setWidthPercentage(100);
            PdfPCell cell1 = new PdfPCell();
            cell1.setMinimumHeight(35);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell1.addElement(para1);
            table1.addCell(cell1);
            document.add(table1);

            document.add(new Paragraph(" "));

//            stavke
            for (int i = 0; i < stavkaList.size(); i++) {
                if (radniNalog.getRadniNalogId() == stavkaList.get(i).getRadniNalogId()) {
                    document.add(new Paragraph("Stavka " + String.valueOf(stavkaNumber) + ":", items));
                    document.add(new Paragraph(stavkaList.get(i).getOpisTekst(), textFont));
                    document.add(new Paragraph(" "));
                }
                ++stavkaNumber;
            }

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

//            potpis
            document.add(new Paragraph("____________________                                                            ____________________ "));
            document.add(new Paragraph("      Potpis izvrsitelja                                                                                           Pecat firme", textFont));

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


