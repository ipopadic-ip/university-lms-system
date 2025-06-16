package com.unidunav.student.dto;

import java.util.List;

public class StudentDTO {

    private Long id;

    private String brojIndeksa;
    private int godinaUpisa;
    private double prosecnaOcena;
    private int ukupnoEcts;
    private String ime;
    private String prezime;
    
    private String slikaPath;
    
    private String zavrsniRad;
    private List<String> predmeti;

    public String getZavrsniRad() {
		return zavrsniRad;
	}

	public void setZavrsniRad(String zavrsniRad) {
		this.zavrsniRad = zavrsniRad;
	}

	public List<String> getPredmeti() {
		return predmeti;
	}

	public void setPredmeti(List<String> predmeti) {
		this.predmeti = predmeti;
	}

	public String getSlikaPath() {
        return slikaPath;
    }

    public void setSlikaPath(String slikaPath) {
        this.slikaPath = slikaPath;
    }

    
    public StudentDTO() {}

    // Getteri i setteri
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getBrojIndeksa() {
        return brojIndeksa;
    }

    public void setBrojIndeksa(String brojIndeksa) {
        this.brojIndeksa = brojIndeksa;
    }

    public int getGodinaUpisa() {
        return godinaUpisa;
    }

    public void setGodinaUpisa(int godinaUpisa) {
        this.godinaUpisa = godinaUpisa;
    }

    public double getProsecnaOcena() {
        return prosecnaOcena;
    }

    public void setProsecnaOcena(double prosecnaOcena) {
        this.prosecnaOcena = prosecnaOcena;
    }

    public int getUkupnoEcts() {
        return ukupnoEcts;
    }

    public void setUkupnoEcts(int ukupnoEcts) {
        this.ukupnoEcts = ukupnoEcts;
    }
}