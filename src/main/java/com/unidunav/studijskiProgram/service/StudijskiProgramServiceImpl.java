package com.unidunav.studijskiProgram.service;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unidunav.godinaStudija.model.GodinaStudija;
import com.unidunav.godinaStudija.repository.GodinaStudijaRepository;
import com.unidunav.katedra.Repository.KatedraRepository;
import com.unidunav.katedra.model.Katedra;
import com.unidunav.predmet.dto.PredmetDTO;
import com.unidunav.predmet.model.Predmet;
import com.unidunav.predmet.repository.PredmetRepository;
import com.unidunav.profesor.dto.ProfesorDTO;
import com.unidunav.profesor.model.Profesor;
import com.unidunav.profesor.repository.ProfesorRepository;
import com.unidunav.profesor.service.ProfesorService;
import com.unidunav.studijskiProgram.dto.GrupisaniProgramiDTO;
import com.unidunav.studijskiProgram.dto.StudijskiProgramDTO;
import com.unidunav.studijskiProgram.model.StudijskiProgram;
import com.unidunav.studijskiProgram.repository.StudijskiProgramRepository;
import com.unidunav.tipStudija.dto.TipStudijaDTO;
import com.unidunav.tipStudija.model.TipStudija;
import com.unidunav.tipStudija.repository.TipStudijaRepository;
import com.unidunav.tipStudija.service.RdfTipStudijaService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class StudijskiProgramServiceImpl implements StudijskiProgramService {

    private final StudijskiProgramRepository studijskiProgramRepository;
    private final TipStudijaRepository tipStudijaRepository;
    private final ProfesorRepository profesorRepository;
    
    private final KatedraRepository katedraRepository;
    
    @Autowired
    private PredmetRepository predmetRepository;
    
    @Autowired
    private GodinaStudijaRepository godinaStudijaRepository;
    
    @Autowired
    private RdfTipStudijaService rdfTipStudijaService;
    
    @Autowired
    private ProfesorService profesorService;

    public StudijskiProgramServiceImpl(StudijskiProgramRepository studijskiProgramRepository,
                                       TipStudijaRepository tipStudijaRepository,
                                       ProfesorRepository profesorRepository, KatedraRepository katedraRepository) {
        this.studijskiProgramRepository = studijskiProgramRepository;
        this.tipStudijaRepository = tipStudijaRepository;
        this.profesorRepository = profesorRepository;
        this.katedraRepository = katedraRepository;
    }

    @Override
    public StudijskiProgramDTO create(StudijskiProgramDTO dto) {
        StudijskiProgram entity = toEntity(dto);
        entity = studijskiProgramRepository.save(entity);
        return toDTO(entity);
    }
    
//    @Override
//    public Map<String, List<StudijskiProgramDTO>> findByKatedraGroupedByTipStudija(Long katedraId) {
//        List<StudijskiProgram> programi = studijskiProgramRepository.findByKatedraId(katedraId);
//        
//        return programi.stream()
//        		.filter(p -> !p.isDeleted()) // filtriraj obrisane studijske programe
//                .filter(p -> p.getTipStudija() != null && !p.getTipStudija().isDeleted()) // filtriraj obrisane tipove
//                .map(this::toDTO)
//                .collect(Collectors.groupingBy(dto -> dto.getTipStudija().getTip()));
//    }
    
//    @Override
//    public Map<String, List<StudijskiProgramDTO>> findByKatedraGroupedByTipStudija(Long katedraId) {
//        List<StudijskiProgram> programi = studijskiProgramRepository.findByKatedraId(katedraId);
//
//        // Učitaj sve RDF tipove koji su aktivni
//        List<TipStudijaDTO> rdfTipovi = rdfTipStudijaService.getAllActiveTipoviStudija();
//        Map<String, TipStudijaDTO> tipMap = rdfTipovi.stream()
//            .collect(Collectors.toMap(TipStudijaDTO::getTip, Function.identity()));
//
//        return programi.stream()
//            .filter(p -> !p.isDeleted())
//            .filter(p -> p.getTipStudija() != null)
//            .filter(p -> tipMap.containsKey(p.getTipStudija().getTip()))
//            .map(p -> {
//                StudijskiProgramDTO dto = toDTO(p);
//                dto.setTipStudija(tipMap.get(p.getTipStudija().getTip())); // koristi RDF DTO
//                return dto;
//            })
//            .collect(Collectors.groupingBy(dto -> dto.getTipStudija().getTip()));
//    }
    
    @Override
    public List<GrupisaniProgramiDTO> findByKatedraGroupedByTipStudija(Long katedraId) {
        List<StudijskiProgram> programi = studijskiProgramRepository.findByKatedraId(katedraId);

        // RDF tipovi
        List<TipStudijaDTO> rdfTipovi = rdfTipStudijaService.getAllActiveTipoviStudija();
        Map<String, TipStudijaDTO> tipMap = rdfTipovi.stream()
            .collect(Collectors.toMap(TipStudijaDTO::getUri, Function.identity()));

        Map<String, List<StudijskiProgramDTO>> grupisani = new LinkedHashMap<>();

        for (StudijskiProgram p : programi) {
            if (p.isDeleted()) continue;

            String uri = p.getTipStudijaUri(); // koristi direktno URI iz baze
            if (uri == null || !tipMap.containsKey(uri)) continue;

            TipStudijaDTO tip = tipMap.get(uri);
            StudijskiProgramDTO dto = toDTO(p);
            dto.setTipStudija(tip); // setuj RDF info

            grupisani.computeIfAbsent(tip.getTip(), k -> new ArrayList<>()).add(dto);
        }

        List<GrupisaniProgramiDTO> rezultat = new ArrayList<>();
        grupisani.forEach((tip, lista) -> rezultat.add(new GrupisaniProgramiDTO(tip, lista)));

        return rezultat;
    }





    
    
    @Override
    public Map<Integer, List<PredmetDTO>> getPredmetiPoGodinama(Long studijskiProgramId) {
//        List<GodinaStudija> godine = godinaStudijaRepository.findByStudijskiProgramId(studijskiProgramId);
//    	List<GodinaStudija> godine = godinaStudijaRepository.findByStudijskiProgramIdAndIsDeletedFalse(studijskiProgramId);
    	List<GodinaStudija> godine = godinaStudijaRepository.findAktivneGodineByStudijskiProgramId(studijskiProgramId);


        Map<Integer, List<PredmetDTO>> result = new TreeMap<>();

        for (GodinaStudija godina : godine) {
//            List<Predmet> predmeti = predmetRepository.findByGodinaStudijaId(godina.getId());
        	 List<Predmet> predmeti = predmetRepository.findByGodinaStudijaIdAndDeletedFalse(godina.getId());
            

            List<PredmetDTO> predmetiDTO = predmeti.stream()
                .map(p -> new PredmetDTO(
                    p.getId(),
                    p.getNaziv(),
                    p.getEcts(),
                    p.getInformacijeOPredmetu(),
                    godina.getId()
//                    studijskiProgramId
                ))
                .collect(Collectors.toList());

            result.computeIfAbsent(godina.getGodina(), k -> new ArrayList<>()).addAll(predmetiDTO);
        }

        return result;
    }
    
    @Override
    public List<StudijskiProgramDTO> findAll() {
        return studijskiProgramRepository.findByDeletedFalse().stream()
                .filter(p -> p.getKatedra() != null && p.getKatedra().getDepartman() != null && p.getKatedra().getDepartman().getFakultet() != null)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudijskiProgramDTO> findAllGroupedByFakultetSortedByDeleted() {
        return studijskiProgramRepository.findAllByOrderByDeletedAsc().stream()
                .filter(p -> p.getKatedra() != null && p.getKatedra().getDepartman() != null && p.getKatedra().getDepartman().getFakultet() != null)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deaktiviraj(Long id) {
        StudijskiProgram sp = studijskiProgramRepository.findById(id).orElseThrow();
        sp.setDeleted(true);
        studijskiProgramRepository.save(sp);
    }

    @Override
    public void aktiviraj(Long id) {
        StudijskiProgram sp = studijskiProgramRepository.findById(id).orElseThrow();
        sp.setDeleted(false);
        studijskiProgramRepository.save(sp);
    }




//    @Override
//    public List<StudijskiProgramDTO> findAll() {
//        return studijskiProgramRepository.findAll()
//                .stream()
//                .map(this::toDTO)
//                .collect(Collectors.toList());
//    }

    @Override
    public StudijskiProgramDTO findById(Long id) {
        StudijskiProgram entity = studijskiProgramRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Studijski program sa ID " + id + " nije pronađen."));
        return toDTO(entity);
    }

    public StudijskiProgramDTO update(Long id, StudijskiProgramDTO dto) {
        StudijskiProgram entity = studijskiProgramRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Studijski program sa ID " + id + " nije pronađen."));

        entity.setNaziv(dto.getNaziv());
        entity.setOpis(dto.getOpis());

        if (dto.getKatedraId() != null) {
            Katedra katedra = katedraRepository.findById(dto.getKatedraId())
                .orElseThrow(() -> new EntityNotFoundException("Katedra sa ID " + dto.getKatedraId() + " nije pronađena."));
            entity.setKatedra(katedra);
        }
        
        if (dto.getTipStudijaUri() != null) {
            entity.setTipStudijaUri(dto.getTipStudijaUri()); // koristi URI, ne entitet
        }

//        if (dto.getTipStudijaId() != null) {
//            TipStudija tipStudija = tipStudijaRepository.findById(dto.getTipStudijaId())
//                .orElseThrow(() -> new EntityNotFoundException("Tip studija sa ID " + dto.getTipStudijaId() + " nije pronađen."));
//            entity.setTipStudija(tipStudija);
//        }

        if (dto.getRukovodilacId() != null) {
            Profesor rukovodilac = profesorRepository.findById(dto.getRukovodilacId())
                .orElseThrow(() -> new EntityNotFoundException("Profesor rukovodilac sa ID " + dto.getRukovodilacId() + " nije pronađen."));
            entity.setRukovodilac(rukovodilac);
        }

        entity = studijskiProgramRepository.save(entity);
        return toDTO(entity);
    }


    @Override
    public void delete(Long id) {
        if (!studijskiProgramRepository.existsById(id)) {
            throw new EntityNotFoundException("Studijski program sa ID " + id + " ne postoji.");
        }
        studijskiProgramRepository.deleteById(id);
    }
    
    private StudijskiProgramDTO toDTO(StudijskiProgram entity) {
        StudijskiProgramDTO dto = new StudijskiProgramDTO();

        dto.setId(entity.getId());
        dto.setNaziv(entity.getNaziv());
        dto.setOpis(entity.getOpis());
        dto.setDeleted(entity.isDeleted());

        if (entity.getKatedra() != null) {
            dto.setKatedraId(entity.getKatedra().getId());
            dto.setKatedraNaziv(entity.getKatedra().getNaziv());
        }

        dto.setTipStudijaUri(entity.getTipStudijaUri());

         //Ako želiš odmah da ubaciš i naziv RDF tipa
        if (entity.getTipStudijaUri() != null) {
            try {
                TipStudijaDTO rdfDto = rdfTipStudijaService.findByUri(entity.getTipStudijaUri());
                dto.setTipStudija(rdfDto); // puni DTO direktno iz RDF-a
            } catch (Exception e) {
                // Možeš logovati ili ignorisati ako RDF entitet ne postoji
            }
        }

        if (entity.getRukovodilac() != null) {
            dto.setRukovodilac(profesorService.toDTO(entity.getRukovodilac()));
            dto.setRukovodilacId(entity.getRukovodilac().getId());
        }

        return dto;
    }


//    private StudijskiProgramDTO toDTO(StudijskiProgram entity) {
//        StudijskiProgramDTO dto = new StudijskiProgramDTO();
//
//        dto.setId(entity.getId());
//        dto.setNaziv(entity.getNaziv());
//        dto.setOpis(entity.getOpis());
//        dto.setKatedraId(entity.getKatedra() != null ? entity.getKatedra().getId() : null);
//        dto.setDeleted(entity.isDeleted());
//        dto.setKatedraNaziv(entity.getKatedra().getNaziv());
//
//
//        if (entity.getTipStudija() != null) {
//            TipStudijaDTO tipDto = new TipStudijaDTO();
////            tipDto.setId(entity.getTipStudija().getId());
//            tipDto.setTip(entity.getTipStudija().getTip());
//            dto.setTipStudija(tipDto);
//            dto.setTipStudijaUri(entity.getTipStudija().getUri());//
//
//        }
//        
//        if (entity.getRukovodilac() != null) {
//          	 dto.setRukovodilac(profesorService.toDTO(entity.getRukovodilac()));
//          }
//
//        return dto;
//    }
    
    private StudijskiProgram toEntity(StudijskiProgramDTO dto) {
        StudijskiProgram entity = new StudijskiProgram();

        entity.setId(dto.getId());
        entity.setNaziv(dto.getNaziv());
        entity.setOpis(dto.getOpis());
        entity.setDeleted(dto.isDeleted());

        if (dto.getKatedraId() != null) {
            Katedra katedra = katedraRepository.findById(dto.getKatedraId())
                .orElseThrow(() -> new EntityNotFoundException("Katedra sa ID " + dto.getKatedraId() + " nije pronađena."));
            entity.setKatedra(katedra);
        }
        
        if (dto.getTipStudijaUri() != null) {
            entity.setTipStudijaUri(dto.getTipStudijaUri()); // setuj URI direktno
        }

//        if (dto.getTipStudijaUri() != null) {
//            TipStudija tipStudija = tipStudijaRepository.findById(dto.getTipStudijaId())
//                .orElseThrow(() -> new EntityNotFoundException("Tip studija sa ID " + dto.getTipStudijaId() + " nije pronađen."));
//            entity.setTipStudija(tipStudija);
//        }

        if (dto.getRukovodilacId() != null) {
            Profesor rukovodilac = profesorRepository.findById(dto.getRukovodilacId())
                .orElseThrow(() -> new EntityNotFoundException("Profesor rukovodilac sa ID " + dto.getRukovodilacId() + " nije pronađen."));
            entity.setRukovodilac(rukovodilac);
        }

        return entity;
    }


//    private StudijskiProgram toEntity(StudijskiProgramDTO dto) {
//        StudijskiProgram entity = new StudijskiProgram();
//
//        entity.setId(dto.getId());
//        entity.setNaziv(dto.getNaziv());
//        entity.setOpis(dto.getOpis());
//        entity.setDeleted(dto.isDeleted());
//        
//        if (dto.getKatedraId() != null) {
//            Katedra katedra = katedraRepository.findById(dto.getKatedraId())
//                .orElseThrow(() -> new EntityNotFoundException("Katedra sa ID " + dto.getKatedraId() + " nije pronađena."));
//            entity.setKatedra(katedra);
//        }
//
//
//        TipStudija tipStudija = tipStudijaRepository.findById(dto.getTipStudija().getId())
//                .orElseThrow(() -> new EntityNotFoundException("Tip studija sa ID " + dto.getTipStudija().getId() + " nije pronađen."));
//        entity.setTipStudija(tipStudija);
//
//        Profesor rukovodilac = profesorRepository.findById(dto.getRukovodilac().getId())
//                .orElseThrow(() -> new EntityNotFoundException("Profesor rukovodilac sa ID " + dto.getRukovodilac().getId() + " nije pronađen."));
//        entity.setRukovodilac(rukovodilac);
//
//        return entity;
//    }
}
