package com.unidunav.evaluacijaxml.dto;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "Evaluacija")
@XmlAccessorType(XmlAccessType.FIELD)
public class EvaluacijaXmlDTO {
    private int brojBodova;
    private int ocena;
    private Long pohadjanjeId;
    private Long evaluacijaId;

    public int getBrojBodova() { return brojBodova; }
    public void setBrojBodova(int brojBodova) { this.brojBodova = brojBodova; }

    public int getOcena() { return ocena; }
    public void setOcena(int ocena) { this.ocena = ocena; }

    public Long getPohadjanjeId() { return pohadjanjeId; }
    public void setPohadjanjeId(Long pohadjanjeId) { this.pohadjanjeId = pohadjanjeId; }

    public Long getEvaluacijaId() { return evaluacijaId; }
    public void setEvaluacijaId(Long evaluacijaId) { this.evaluacijaId = evaluacijaId; }
}