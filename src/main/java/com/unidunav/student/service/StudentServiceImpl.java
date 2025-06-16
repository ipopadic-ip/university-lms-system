package com.unidunav.student.service;

import java.util.Set;
import com.unidunav.obavestenje.dto.ObavestenjeStudentuDTO;
import com.unidunav.predmet.dto.EvaluacijaZnanjaDTO;
import com.unidunav.predmet.dto.IstorijaStudiranjaDTO;
import com.unidunav.predmet.dto.PredmetDTO;
import com.unidunav.predmet.dto.StudentIstorijaStudiranjaResponseDTOProfesor;
import com.unidunav.predmet.model.PohadjanjePredmeta;
import com.unidunav.predmet.model.Predmet;
import com.unidunav.predmet.model.PrijavaIspita;
import com.unidunav.predmet.model.PrijavaPrestupa;
import com.unidunav.predmet.repository.EvaluacijaZnanjaRepository;
import com.unidunav.predmet.repository.PohadjanjePredmetaRepository;
import com.unidunav.predmet.repository.PrijavaIspitaRepository;
import com.unidunav.predmet.repository.PrijavaPrestupaRepository;
import com.unidunav.student.dto.StudentDTO;
import com.unidunav.student.dto.StudentPredmetDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.web.multipart.MultipartFile;
import com.unidunav.student.model.Student;
import com.unidunav.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
@Transactional(readOnly = true)
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository repository;
    
    
    @Autowired
    private PohadjanjePredmetaRepository pohadjanjePredmetaRepository; 

    public List<PredmetDTO> getPredmetiKojeStudentSlusa(Long studentId) {
        List<PohadjanjePredmeta> pohadjanja = pohadjanjePredmetaRepository.findByStudentId(studentId);

        return pohadjanja.stream()
            .map(pp -> {
                Predmet predmet = pp.getPredmet();
                return new PredmetDTO(predmet.getId(), predmet.getNaziv(), predmet.getEcts(), predmet.getInformacijeOPredmetu());
            })
            .collect(Collectors.toList());
    }

//    private StudentDTO toDTO(Student student) {
//        StudentDTO dto = new StudentDTO();
//        dto.setId(student.getId());
////        dto.setIme(student.getIme());
////        dto.setPrezime(student.getPrezime());
//        dto.setBrojIndeksa(student.getBrojIndeksa());
//        dto.setGodinaUpisa(student.getGodinaUpisa());
//        dto.setProsecnaOcena(student.getProsecnaOcena());
//        dto.setUkupnoEcts(student.getUkupnoEcts());
//        
//     // u toDTO
//        dto.setSlikaPath(student.getSlikaPath());
//        return dto;
//    }
    
    private StudentDTO toDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setBrojIndeksa(student.getBrojIndeksa());
        dto.setGodinaUpisa(student.getGodinaUpisa());
        dto.setProsecnaOcena(student.getProsecnaOcena());
        dto.setUkupnoEcts(student.getUkupnoEcts());
        dto.setSlikaPath(student.getSlikaPath());
        dto.setZavrsniRad(student.getZavrsniRad());

        // Dodaj ime i prezime ako postoji user
        if (student.getUser() != null) {
            dto.setIme(student.getUser().getIme());
            dto.setPrezime(student.getUser().getPrezime());
        }

        // Dodaj listu naziva predmeta koje student pohađa
        List<String> predmeti = pohadjanjePredmetaRepository
            .findByStudentId(student.getId())
            .stream()
            .map(p -> p.getPredmet().getNaziv())
            .distinct()
            .collect(Collectors.toList());

        dto.setPredmeti(predmeti);

        return dto;
    }
    
    private StudentPredmetDTO toStudentPredmetDTO(Student student, String predmetNaziv, Long pohadjanjeId) {
        StudentPredmetDTO dto = new StudentPredmetDTO();
        dto.setId(student.getId());
        dto.setBrojIndeksa(student.getBrojIndeksa());
        dto.setGodinaUpisa(student.getGodinaUpisa());
        dto.setProsecnaOcena(student.getProsecnaOcena());
        dto.setUkupnoEcts(student.getUkupnoEcts());
        dto.setSlikaPath(student.getSlikaPath());
        dto.setZavrsniRad(student.getZavrsniRad());
        dto.setPredmet(predmetNaziv);

        if (student.getUser() != null) {
            dto.setIme(student.getUser().getIme());
            dto.setPrezime(student.getUser().getPrezime());
        }

        // Dodavanje evaluacija
        List<EvaluacijaZnanjaDTO> evaluacijaDTOs = evaluacijaZnanjaRepository.findByPohadjanjeId(pohadjanjeId)
            .stream()
            .map(e -> {
                EvaluacijaZnanjaDTO eDTO = new EvaluacijaZnanjaDTO();
                eDTO.setId(e.getId());
                eDTO.setVremePocetka(e.getVremePocetka());
                eDTO.setBrojBodova(e.getBrojBodova() != null ? e.getBrojBodova() : 0);
                eDTO.setPohadjanjeId(e.getPohadjanje().getId());
                eDTO.setTipEvaluacijeId(e.getTipEvaluacije().getId());
                eDTO.setTipEvaluacijeNaziv(e.getTipEvaluacije().getTip()); // <- OVDE ISPRAVKA
                return eDTO;
            })
            .collect(Collectors.toList());

        dto.setEvaluacije(evaluacijaDTOs);

        return dto;
    }



    private Student toEntity(StudentDTO dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setBrojIndeksa(dto.getBrojIndeksa());
        student.setGodinaUpisa(dto.getGodinaUpisa());
        student.setProsecnaOcena(dto.getProsecnaOcena());
        student.setUkupnoEcts(dto.getUkupnoEcts());
        student.setSlikaPath(dto.getSlikaPath());
        student.setZavrsniRad(dto.getZavrsniRad());

        
        return student;
    }
    
    @Autowired
    private PohadjanjePredmetaRepository pohadjanjePredmetaRepo;

    @Override
    public List<ObavestenjeStudentuDTO> getObavestenjaZaStudenta(Long studentId) {
        List<PohadjanjePredmeta> pohadjanja = pohadjanjePredmetaRepo.findByStudentId(studentId);

        return pohadjanja.stream()
            .filter(PohadjanjePredmeta::isAktivan)
            .map(PohadjanjePredmeta::getPredmet)
            .filter(predmet -> predmet.getObavestenja() != null && !predmet.getObavestenja().isEmpty())
            .flatMap(predmet ->
                predmet.getObavestenja().stream()
                    .map(obavestenje -> new ObavestenjeStudentuDTO(
                        predmet.getNaziv(),
                        obavestenje.getTekst(),
                        obavestenje.getDatum()
                    ))
            )
            .collect(Collectors.toList());
    }
    

    @Override
    public StudentDTO create(StudentDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public List<StudentDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public StudentDTO findById(Long id) {
        return repository.findById(id).map(this::toDTO).orElse(null);
    }

    @Override
    public StudentDTO update(Long id, StudentDTO dto) {
        return repository.findById(id).map(existing -> {
//            existing.setIme(dto.getIme());
//            existing.setPrezime(dto.getPrezime());
            existing.setBrojIndeksa(dto.getBrojIndeksa());
            existing.setGodinaUpisa(dto.getGodinaUpisa());
            existing.setProsecnaOcena(dto.getProsecnaOcena());
            existing.setUkupnoEcts(dto.getUkupnoEcts());
            return toDTO(repository.save(existing));
        }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
    
    @Override
    public String uploadSlika(Long studentId, MultipartFile slika) throws IOException {
        Student student = repository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student nije pronađen"));

        String folder = "uploads/studenti/";
        File dir = new File(folder);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String originalFilename = slika.getOriginalFilename();
        String filename = "student_" + studentId + "_" + System.currentTimeMillis() + "_" + originalFilename;
        Path filePath = Paths.get(folder + filename);

        Files.write(filePath, slika.getBytes());

        student.setSlikaPath(filePath.toString());
        repository.save(student);

        return filePath.toString();
    }
    
    public List<StudentDTO> findByBrojIndeksa(String indeks) {
        return repository.findByBrojIndeksaContainingIgnoreCaseAndUserNotDeleted(indeks).stream().map(student -> {
            StudentDTO dto = new StudentDTO();
            dto.setId(student.getId());
            dto.setIme(student.getUser().getIme());
            dto.setPrezime(student.getUser().getPrezime());
            dto.setBrojIndeksa(student.getBrojIndeksa());
            return dto;
        }).collect(Collectors.toList());
    }
    
    @Override
    public List<StudentDTO> findStudentiZaProfesora(Long profesorId) {
        List<PohadjanjePredmeta> pohadjanja = pohadjanjePredmetaRepository.findByProfesorId(profesorId);

        List<StudentDTO> studenti = pohadjanja.stream()
            .map(PohadjanjePredmeta::getStudent)
            .distinct()
            .map(this::toDTO)
            .collect(Collectors.toList());

        // Debug ispis u konzolu
        System.out.println("==== Studenti za profesora sa ID " + profesorId + " ====");
        for (StudentDTO s : studenti) {
            System.out.println("Student: " + s.getId() + ", " + s.getBrojIndeksa() + ", " + s.getProsecnaOcena());
        }

        return studenti;
    }

    
    @Autowired
    private PrijavaIspitaRepository prijavaIspitaRepository;

    @Autowired
    private PrijavaPrestupaRepository prijavaPrestupaRepository;
    
    @Autowired
    private EvaluacijaZnanjaRepository evaluacijaZnanjaRepository;

    
    @Override
    public StudentIstorijaStudiranjaResponseDTOProfesor getStudentIstorijaStudiranjaProfesor(Long studentId) {
        List<PohadjanjePredmeta> pohadjanja = pohadjanjePredmetaRepository.findByStudentId(studentId);
        List<IstorijaStudiranjaDTO> polozeniIspiti = new ArrayList<>();
        List<IstorijaStudiranjaDTO> neuspesni = new ArrayList<>();
        List<IstorijaStudiranjaDTO> prijavljeni = new ArrayList<>();
        List<IstorijaStudiranjaDTO> prestupi = new ArrayList<>();

        double sumaOcena = 0;
        int brojOcena = 0;
        int ukupnoEcts = 0;

        for (PohadjanjePredmeta p : pohadjanja) {
            Predmet predmet = p.getPredmet();
            Integer ocenaObj = p.getOcena();

            int ukupnoBodova = evaluacijaZnanjaRepository
                .findByPohadjanjeId(p.getId())
                .stream()
                .mapToInt(e -> e.getBrojBodova())
                .sum();

            IstorijaStudiranjaDTO dto = new IstorijaStudiranjaDTO(
                predmet.getNaziv(),
                p.getBrojPolaganja(),
                ocenaObj != null ? ocenaObj : 0,
                predmet.getEcts(),
                ukupnoBodova
            );

            if (ocenaObj != null && ocenaObj > 5) {
                polozeniIspiti.add(dto);
                sumaOcena += ocenaObj;
                brojOcena++;
                ukupnoEcts += predmet.getEcts();
            } else {
                neuspesni.add(dto);
            }
        }

        // PRIJAVLJENI ISPITI
        List<PrijavaIspita> prijaveIspita = prijavaIspitaRepository.findAll();
        for (PrijavaIspita prijava : prijaveIspita) {
            PohadjanjePredmeta poh = prijava.getPohadjanje();
            if (poh != null && poh.getStudent() != null && poh.getStudent().getId().equals(studentId)) {
                Predmet predmet = poh.getPredmet();
                IstorijaStudiranjaDTO dto = new IstorijaStudiranjaDTO(
                    predmet.getNaziv(),
                    poh.getBrojPolaganja(),
                    poh.getOcena() != null ? poh.getOcena() : 5,
                    predmet.getEcts(),
                    0
                );
                prijavljeni.add(dto);
            }
        }

        // PRIJAVE PRESTUPA
        List<PrijavaPrestupa> prijavePrestupa = prijavaPrestupaRepository.findAll();
        for (PrijavaPrestupa prijava : prijavePrestupa) {
            PohadjanjePredmeta poh = prijava.getPohadjanje();
            if (poh != null && poh.getStudent() != null && poh.getStudent().getId().equals(studentId)) {
                Predmet predmet = poh.getPredmet();
                IstorijaStudiranjaDTO dto = new IstorijaStudiranjaDTO(
                    predmet.getNaziv() + " - " + prijava.getOpis(),
                    poh.getBrojPolaganja(),
                    0,
                    predmet.getEcts(),
                    0
                );
                prestupi.add(dto);
            }
        }

        double prosecnaOcena = brojOcena > 0 ? sumaOcena / brojOcena : 0.0;

        return new StudentIstorijaStudiranjaResponseDTOProfesor(
            new ArrayList<>(), // upisi
            polozeniIspiti,
            neuspesni,
            prijavljeni,
            prestupi,
            null, // zavrsni rad
            prosecnaOcena,
            ukupnoEcts
        );
    }


    @Override
    public List<StudentPredmetDTO> findStudentiZaPredmet(Long predmetId) {
        List<PohadjanjePredmeta> pohadjanja = pohadjanjePredmetaRepository.findByPredmetId(predmetId);
        Set<Long> dodatiStudenti = new HashSet<>();

        System.out.println("DOBAVLJAM STUDENTE ZA PREDMET ID = " + predmetId);

        return pohadjanja.stream()
            .filter(pp -> dodatiStudenti.add(pp.getStudent().getId()))
            .map(pp -> toStudentPredmetDTO(pp.getStudent(), pp.getPredmet().getNaziv(), pp.getId()))
            .collect(Collectors.toList());
    }


}