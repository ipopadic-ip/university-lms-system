package com.unidunav.profesor.controller;

import com.unidunav.predmet.dto.PredmetDTO;
import com.unidunav.profesor.dto.ProfesorDTO;
import com.unidunav.profesorPredmet.dto.ProfesorPredmetResponseDTO;
import com.unidunav.profesor.service.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.core.Authentication;
import com.unidunav.user.model.User;

import java.util.List;

@RestController
@RequestMapping("/api/profesor")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
public class ProfesorController {

    @Autowired
    private ProfesorService service;

    @PostMapping
    public ProfesorDTO create(@RequestBody ProfesorDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<ProfesorDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ProfesorDTO getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public ProfesorDTO update(@PathVariable Long id, @RequestBody ProfesorDTO dto) {
        return service.update(id, dto);
    }

//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable Long id) {
//        service.delete(id);
//    }
//    POST /api/profesor/{id}/slika
//    Body: form-data
//    Key: slika (type: file)
    @PostMapping("/{id}/slika")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<String> uploadSlika(@PathVariable Long id, @RequestParam("slika") MultipartFile slika) {
        try {
            String putanja = service.uploadSlika(id, slika);	
            return ResponseEntity.ok("Slika uspešno sačuvana: " + putanja);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Greška: " + e.getMessage());
        }
    }
    
    @GetMapping("/profesori")
    @PreAuthorize("hasRole('SLUZBENIK')")
    public List<ProfesorDTO> getAllProfesori() {
        return service.findAll();
    }
    
    @GetMapping("/{id}/predmeti")
    @PreAuthorize("hasRole('PROFESOR') or #id == authentication.principal.id")
    public List<PredmetDTO> getPredmetiZaProfesora(@PathVariable Long id) {
        return service.findPredmetiByProfesorId(id);
    }
    
    @GetMapping("/profil")
    public ProfesorDTO getProfil(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return service.getProfilByUserId(user.getId());
    }

    @PutMapping("/profil")
    public ProfesorDTO izmeniProfil(Authentication auth,
        @RequestParam String ime,
        @RequestParam String prezime,
        @RequestParam(required = false) String staraLozinka,
        @RequestParam(required = false) String novaLozinka,
        @RequestParam String biografija,
        @RequestParam(value = "slika", required = false) MultipartFile slika
    ) {
        User user = (User) auth.getPrincipal();
        return service.izmeniProfil(user.getId(), ime, prezime, staraLozinka, novaLozinka, biografija, slika);
    }

}
