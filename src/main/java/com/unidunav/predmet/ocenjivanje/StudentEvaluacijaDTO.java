package com.unidunav.predmet.ocenjivanje;

import java.time.LocalDateTime;

public class StudentEvaluacijaDTO {
    private Long studentId;
    private String ime;
    private String prezime;
    private String brojIndeksa;

    private Long evaluacijaId;
    private String tipEvaluacije;
    private LocalDateTime datumEvaluacije;
    private Integer brojBodova;

    private Integer trenutnaOcena;
    
    private Integer ukupnoBodova;

    public StudentEvaluacijaDTO() {}

    public StudentEvaluacijaDTO(Long studentId, String ime, String prezime, String brojIndeksa,
                                Long evaluacijaId, String tipEvaluacije, LocalDateTime datumEvaluacije,
                                Integer brojBodova, Integer trenutnaOcena) {
        this.studentId = studentId;
        this.ime = ime;
        this.prezime = prezime;
        this.brojIndeksa = brojIndeksa;
        this.evaluacijaId = evaluacijaId;
        this.tipEvaluacije = tipEvaluacije;
        this.datumEvaluacije = datumEvaluacije;
        this.brojBodova = brojBodova;
        this.trenutnaOcena = trenutnaOcena;
    }

    // Getteri i setteri

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }

    public String getPrezime() { return prezime; }
    public void setPrezime(String prezime) { this.prezime = prezime; }

    public String getBrojIndeksa() { return brojIndeksa; }
    public void setBrojIndeksa(String brojIndeksa) { this.brojIndeksa = brojIndeksa; }

    public Long getEvaluacijaId() { return evaluacijaId; }
    public void setEvaluacijaId(Long evaluacijaId) { this.evaluacijaId = evaluacijaId; }

    public String getTipEvaluacije() { return tipEvaluacije; }
    public void setTipEvaluacije(String tipEvaluacije) { this.tipEvaluacije = tipEvaluacije; }

    public LocalDateTime getDatumEvaluacije() { return datumEvaluacije; }
    public void setDatumEvaluacije(LocalDateTime datumEvaluacije) { this.datumEvaluacije = datumEvaluacije; }

    public Integer getBrojBodova() { return brojBodova; }
    public void setBrojBodova(Integer brojBodova) { this.brojBodova = brojBodova; }

    public Integer getTrenutnaOcena() { return trenutnaOcena; }
    public void setTrenutnaOcena(Integer trenutnaOcena) { this.trenutnaOcena = trenutnaOcena; }
    
    public Integer getUkupnoBodova() {
        return ukupnoBodova;
    }

    public void setUkupnoBodova(Integer ukupnoBodova) {
        this.ukupnoBodova = ukupnoBodova;
    }
}
