package com.unidunav.student.dto;

import java.util.List;

import com.unidunav.predmet.dto.EvaluacijaZnanjaDTO;

public class StudentPredmetDTO {
    private Long id;
    private String ime;
    private String prezime;
    private String brojIndeksa;
    private int godinaUpisa;
    private double prosecnaOcena;
    private int ukupnoEcts;
    private String slikaPath;
    private String zavrsniRad;
    private String predmet; // samo jedan naziv
    private List<EvaluacijaZnanjaDTO> evaluacije;


    // Getteri i setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }

    public String getPrezime() { return prezime; }
    public void setPrezime(String prezime) { this.prezime = prezime; }

    public String getBrojIndeksa() { return brojIndeksa; }
    public void setBrojIndeksa(String brojIndeksa) { this.brojIndeksa = brojIndeksa; }

    public int getGodinaUpisa() { return godinaUpisa; }
    public void setGodinaUpisa(int godinaUpisa) { this.godinaUpisa = godinaUpisa; }

    public double getProsecnaOcena() { return prosecnaOcena; }
    public void setProsecnaOcena(double prosecnaOcena) { this.prosecnaOcena = prosecnaOcena; }

    public int getUkupnoEcts() { return ukupnoEcts; }
    public void setUkupnoEcts(int ukupnoEcts) { this.ukupnoEcts = ukupnoEcts; }

    public String getSlikaPath() { return slikaPath; }
    public void setSlikaPath(String slikaPath) { this.slikaPath = slikaPath; }

    public String getZavrsniRad() { return zavrsniRad; }
    public void setZavrsniRad(String zavrsniRad) { this.zavrsniRad = zavrsniRad; }

    public String getPredmet() { return predmet; }
    public void setPredmet(String predmet) { this.predmet = predmet; }
    
    public List<EvaluacijaZnanjaDTO> getEvaluacije() {
        return evaluacije;
    }

    public void setEvaluacije(List<EvaluacijaZnanjaDTO> evaluacije) {
        this.evaluacije = evaluacije;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentPredmetDTO that = (StudentPredmetDTO) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }

}
