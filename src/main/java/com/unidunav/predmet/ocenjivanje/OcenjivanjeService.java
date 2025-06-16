package com.unidunav.predmet.ocenjivanje;

import java.util.List;

public interface OcenjivanjeService {
    List<StudentEvaluacijaDTO> getEvaluacijeZaPredmet(Long predmetId);
    void azurirajBodove(Long evaluacijaId, int brojBodova);
}
