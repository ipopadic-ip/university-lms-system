package com.unidunav.profesorPredmet.controller;

import com.unidunav.profesorPredmet.dto.TerminNastaveDTO;
import com.unidunav.profesorPredmet.dto.TerminNastaveResponseDTO;
import com.unidunav.profesorPredmet.service.TerminNastaveService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/termin-nastave")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyRole('SLUZBENIK', 'PROFESOR')")
public class TerminNastaveController {

    private final TerminNastaveService service;

    public TerminNastaveController(TerminNastaveService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TerminNastaveDTO> kreiraj(@RequestBody TerminNastaveDTO dto) {
        return ResponseEntity.ok(service.create(dto));
//    @PreAuthorize("hasRole('SLUZBENIK')")
//    public ResponseEntity<TerminNastaveResponseDTO> kreiraj(@RequestBody TerminNastaveDTO dto) {
//        return ResponseEntity.ok(service.kreiraj(dto));
    }
    
    
    @GetMapping("/profesor/{profesorId}")
    @PreAuthorize("hasAnyRole('PROFESOR')")
    public ResponseEntity<List<TerminNastaveDTO>> sviZaProfesora(@PathVariable Long profesorId) {
        return ResponseEntity.ok(service.findAllByProfesor(profesorId));

//    @GetMapping("/profesor-predmet/{id}")
//    public ResponseEntity<List<TerminNastaveResponseDTO>> sviZaProfesorPredmet(@PathVariable Long id) {
//        return ResponseEntity.ok(service.sviZaProfesorPredmet(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SLUZBENIK', 'PROFESOR')")
    public ResponseEntity<TerminNastaveDTO> azuriraj(@PathVariable Long id, @RequestBody TerminNastaveDTO dto) {
        return ResponseEntity.ok(service.azuriraj(id, dto));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('SLUZBENIK')")
    public ResponseEntity<Void> obrisi(@PathVariable Long id) {
        service.obrisi(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/profesor-predmet/{id}")
    @PreAuthorize("hasAnyRole('PROFESOR')")
    public ResponseEntity<List<TerminNastaveResponseDTO>> sviZaProfesorPredmet(@PathVariable Long id) {
        return ResponseEntity.ok(service.sviZaProfesorPredmet(id));
    }
    
    @PutMapping("/{id}/ishod")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<Void> azurirajIshod(@PathVariable Long id, @RequestBody String noviIshod) {
        service.azurirajIshod(id, noviIshod);
        return ResponseEntity.ok().build();
    }
}
