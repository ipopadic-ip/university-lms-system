package com.unidunav.predmet.dto;

import java.time.LocalDateTime;

public class IstorijaStudiranjaDTO {

    private String nazivPredmeta;
    private int brojPolaganja;
    private Integer ocena;
    private int brojECTS;
    private LocalDateTime datum;
    private String opis;
    private int brojBodova;

    public IstorijaStudiranjaDTO(String nazivPredmeta, int brojPolaganja, Integer ocena, int brojECTS, int brojBodova) {
        this.nazivPredmeta = nazivPredmeta;
        this.brojPolaganja = brojPolaganja;
        this.ocena = ocena;
        this.brojECTS = brojECTS;
        this.brojBodova = brojBodova;
    }

    public IstorijaStudiranjaDTO(String nazivPredmeta, int brojPolaganja, Integer ocena, int brojECTS) {
        this(nazivPredmeta, brojPolaganja, ocena, brojECTS, 0);
    }

    public IstorijaStudiranjaDTO(String nazivPredmeta, int brojPolaganja, Integer ocena, int brojECTS, LocalDateTime datum) {
        this(nazivPredmeta, brojPolaganja, ocena, brojECTS, 0);
        this.datum = datum;
    }

    public IstorijaStudiranjaDTO(String nazivPredmeta, int brojPolaganja, Integer ocena, int brojECTS, LocalDateTime datum, String opis) {
        this(nazivPredmeta, brojPolaganja, ocena, brojECTS, 0);
        this.datum = datum;
        this.opis = opis;
    }

    public String getNazivPredmeta() {
        return nazivPredmeta;
    }

    public void setNazivPredmeta(String nazivPredmeta) {
        this.nazivPredmeta = nazivPredmeta;
    }

    public int getBrojPolaganja() {
        return brojPolaganja;
    }

    public void setBrojPolaganja(int brojPolaganja) {
        this.brojPolaganja = brojPolaganja;
    }

    public Integer getOcena() {
        return ocena;
    }

    public void setOcena(Integer ocena) {
        this.ocena = ocena;
    }

    public int getBrojECTS() {
        return brojECTS;
    }

    public void setBrojECTS(int brojECTS) {
        this.brojECTS = brojECTS;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public int getBrojBodova() {
        return brojBodova;
    }

    public void setBrojBodova(int brojBodova) {
        this.brojBodova = brojBodova;
    }
}