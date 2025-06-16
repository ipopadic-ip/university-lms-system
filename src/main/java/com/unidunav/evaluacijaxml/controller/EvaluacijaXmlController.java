package com.unidunav.evaluacijaxml.controller;

import com.unidunav.evaluacijaxml.dto.EvaluacijaXmlList;
import com.unidunav.evaluacijaxml.service.EvaluacijaXmlService;
import jakarta.xml.bind.JAXBException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/evaluacija/xml")
public class EvaluacijaXmlController {

    private final EvaluacijaXmlService xmlService;

    public EvaluacijaXmlController(EvaluacijaXmlService xmlService) {
        this.xmlService = xmlService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadXml(@RequestParam("file") MultipartFile file) {
        try {
            EvaluacijaXmlList lista = xmlService.parseXml(file.getInputStream());
            xmlService.sacuvajEvaluacije(lista);
            return ResponseEntity.ok("Uspešno ubačeno!");
        } catch (IOException | JAXBException e) {
            return ResponseEntity.badRequest().body("Greška u validaciji: " + e.getMessage());
        }
    }

    @PostMapping("/paste")
    public ResponseEntity<String> pasteXml(@RequestBody String xml) {
        try {
            EvaluacijaXmlList lista = xmlService.parseXml(new ByteArrayInputStream(xml.getBytes()));
            xmlService.sacuvajEvaluacije(lista);
            return ResponseEntity.ok("Uspešno ubačeno!");
        } catch (JAXBException e) {
            return ResponseEntity.badRequest().body("Greška u validaciji: " + e.getMessage());
        }
    }
}
