package com.unidunav.evaluacijaxml.dto;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "Evaluacije")
@XmlAccessorType(XmlAccessType.FIELD)
public class EvaluacijaXmlList {
    @XmlElement(name = "Evaluacija")
    private List<EvaluacijaXmlDTO> evaluacije;

    public List<EvaluacijaXmlDTO> getEvaluacije() { return evaluacije; }
    public void setEvaluacije(List<EvaluacijaXmlDTO> evaluacije) { this.evaluacije = evaluacije; }
}
