package com.unidunav.predmet.ocenjivanje;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ocenjivanje")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class OcenjivanjeController {

    @Autowired
    private OcenjivanjeService ocenjivanjeService;

    @GetMapping("/predmet/{predmetId}")
    public List<StudentEvaluacijaDTO> getEvaluacijeZaPredmet(@PathVariable Long predmetId) {
        return ocenjivanjeService.getEvaluacijeZaPredmet(predmetId);
    }

    @PutMapping("/{evaluacijaId}/bodovi")
    public void updateBodovi(@PathVariable Long evaluacijaId, @RequestParam int brojBodova) {
        ocenjivanjeService.azurirajBodove(evaluacijaId, brojBodova);
    }
}
