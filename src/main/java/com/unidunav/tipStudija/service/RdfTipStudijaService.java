package com.unidunav.tipStudija.service;


import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.*;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.RDF;
import org.springframework.stereotype.Service;

import com.unidunav.tipStudija.dto.TipStudijaDTO;

import java.util.ArrayList;
import java.util.List;

//RDF entiteti nemaju Long id, već koristiš "tip" kao identifikator (npr. "Osnovne", "Master", itd.)
//deleted se čuva kao xsd:boolean literal u RDF triplovima

@Service
public class RdfTipStudijaService {

    private static final String TDB_DIRECTORY = "data/tdb";
    private static final String NS = "http://www.ftn.uns.ac.rs/tipStudija/";

//    public List<String> findAll() {
//        List<String> result = new ArrayList<>();
//
//        Dataset dataset = TDBFactory.createDataset(TDB_DIRECTORY);
//        dataset.begin(ReadWrite.READ);
//        try {
//            Model model = dataset.getDefaultModel();
//
//            ResIterator iterator = model.listResourcesWithProperty(RDF.type, model.createResource(NS + "TipStudija"));
//            while (iterator.hasNext()) {
//                Resource res = iterator.next();
//                String tip = res.getProperty(model.getProperty(NS + "tip")).getString();
//                result.add(tip);
//            }
//        } finally {
//            dataset.end();
//        }
//
//        return result;
//    }
    
    public void updateTipStudija(String stariTip, String noviTip) {
        Dataset dataset = TDBFactory.createDataset(TDB_DIRECTORY);
        dataset.begin(ReadWrite.WRITE);
        try {
            Model model = dataset.getDefaultModel();
            Resource stariResurs = model.getResource(NS + "tipStudija-" + stariTip.hashCode());

            if (stariResurs == null || !stariResurs.hasProperty(model.getProperty(NS + "tip"))) {
                throw new RuntimeException("Tip studija sa imenom '" + stariTip + "' ne postoji.");
            }

            // Ažuriranje vrednosti atributa "tip"
            stariResurs.removeAll(model.getProperty(NS + "tip"));
            stariResurs.addProperty(model.getProperty(NS + "tip"), noviTip);

            // Promena URI-ja nije moguća direktno, pa ostaje stari URI (ili bi trebalo kreirati novi entitet)
            dataset.commit();
        } finally {
            dataset.end();
        }
    }

    
    public List<String> findAll() {
        return findAllByDeleted(false);
    }

    public List<String> findAllAdmin() {
        return findAllByDeleted(null); // null znači "vrati sve"
    }
    
    public String findById(String tip) {
        Dataset dataset = TDBFactory.createDataset(TDB_DIRECTORY);
        dataset.begin(ReadWrite.READ);
        try {
            Model model = dataset.getDefaultModel();
            String uri = NS + "tipStudija-" + tip.hashCode();
            Resource res = model.getResource(uri);

            if (res != null && res.hasProperty(model.getProperty(NS + "tip"))) {
                if (!res.hasProperty(model.getProperty(NS + "deleted")) ||
                    !res.getProperty(model.getProperty(NS + "deleted")).getBoolean()) {
                    return res.getProperty(model.getProperty(NS + "tip")).getString();
                } else {
                    throw new RuntimeException("Entitet je deaktiviran.");
                }
            }

            throw new RuntimeException("Entitet nije pronađen.");
        } finally {
            dataset.end();
        }
    }




//    public String findById(String tip) {
//        Dataset dataset = TDBFactory.createDataset(TDB_DIRECTORY);
//        dataset.begin(ReadWrite.READ);
//        try {
//            Model model = dataset.getDefaultModel();
//            Resource res = model.getResource(NS + tip);
//            if (res != null && res.hasProperty(RDF.type, model.createResource(NS + "TipStudija"))) {
//                if (!res.hasProperty(model.getProperty(NS + "deleted")) || !res.getProperty(model.getProperty(NS + "deleted")).getBoolean()) {
//                    return res.getProperty(model.getProperty(NS + "tip")).getString();
//                } else {
//                    throw new RuntimeException("Entitet je deaktiviran.");
//                }
//            } else {
//                throw new RuntimeException("Entitet nije pronađen.");
//            }
//        } finally {
//            dataset.end();
//        }
//    }

    public void deaktiviraj(String tip) {
        updateDeletedStatus(tip, true);
    }

    public void aktiviraj(String tip) {
        updateDeletedStatus(tip, false);
    }

    private void updateDeletedStatus(String tip, boolean deleted) {
        Dataset dataset = TDBFactory.createDataset(TDB_DIRECTORY);
        dataset.begin(ReadWrite.WRITE);
        try {
            Model model = dataset.getDefaultModel();
            Property tipProp = model.getProperty(NS + "tip");

            // Pronađi resurs koji ima vrednost "tip"
            ResIterator iter = model.listResourcesWithProperty(tipProp, tip);
            if (!iter.hasNext()) {
                throw new RuntimeException("Tip studija sa imenom '" + tip + "' ne postoji.");
            }

            Resource res = iter.next();
            Property deletedProp = model.getProperty(NS + "deleted");
            res.removeAll(deletedProp);
            res.addProperty(deletedProp, model.createTypedLiteral(deleted));

            dataset.commit();
        } finally {
            dataset.end();
        }
    }
    
    public List<TipStudijaDTO> getAllActiveTipoviStudija() {
        List<TipStudijaDTO> result = new ArrayList<>();
        Dataset dataset = TDBFactory.createDataset(TDB_DIRECTORY);
        dataset.begin(ReadWrite.READ);
        try {
            Model model = dataset.getDefaultModel();
            ResIterator iterator = model.listResourcesWithProperty(RDF.type, model.createResource(NS + "TipStudija"));

            while (iterator.hasNext()) {
                Resource res = iterator.next();
                Statement tipStmt = res.getProperty(model.getProperty(NS + "tip"));
                Statement deletedStmt = res.getProperty(model.getProperty(NS + "deleted"));
                
                boolean deleted = deletedStmt != null && deletedStmt.getBoolean();

                if (tipStmt != null && !deleted) {
                    TipStudijaDTO dto = new TipStudijaDTO();
                    dto.setTip(tipStmt.getString());
                    dto.setDeleted(false); // već filtrirano
                    dto.setUri(res.getURI()); // koristi uri iz RDF resursa
                    result.add(dto);
                }
            }
        } finally {
            dataset.end();
        }
        return result;
    }


    
//    private void updateDeletedStatus(String tip, boolean deleted) {
//        Dataset dataset = TDBFactory.createDataset(TDB_DIRECTORY);
//        dataset.begin(ReadWrite.WRITE);
//        try {
//            Model model = dataset.getDefaultModel();
//            Resource res = model.getResource(NS + "tipStudija-" + tip.hashCode());
//            
//            if (res == null || !res.hasProperty(model.getProperty(NS + "tip"))) {
//                throw new RuntimeException("Tip studija sa imenom '" + tip + "' ne postoji.");
//            }
//
//            Property deletedProp = model.getProperty(NS + "deleted");
//            res.removeAll(deletedProp);
//            res.addProperty(deletedProp, model.createTypedLiteral(deleted));
//            dataset.commit();
//        } finally {
//            dataset.end();
//        }
//    }

    private List<String> findAllByDeleted(Boolean deleted) {
        List<String> result = new ArrayList<>();

        Dataset dataset = TDBFactory.createDataset(TDB_DIRECTORY);
        dataset.begin(ReadWrite.READ);
        try {
            Model model = dataset.getDefaultModel();
            ResIterator iterator = model.listResourcesWithProperty(RDF.type, model.createResource(NS + "TipStudija"));
            while (iterator.hasNext()) {
                Resource res = iterator.next();
                Statement deletedStmt = res.getProperty(model.getProperty(NS + "deleted"));
                boolean isDeleted = deletedStmt != null && deletedStmt.getBoolean();
                
                if (deleted == null || isDeleted == deleted) {
                    Statement tipStmt = res.getProperty(model.getProperty(NS + "tip"));
                    if (tipStmt != null) {
                        result.add(tipStmt.getString());
                    }
                }

//                if (deleted == null || isDeleted == deleted) {
//                    String tip = res.getProperty(model.getProperty(NS + "tip")).getString();
//                    result.add(tip);
//                }
            }
        } finally {
            dataset.end();
        }

        return result;
    }
    
    public List<TipStudijaDTO> findAllActiveDto() {
        List<TipStudijaDTO> result = new ArrayList<>();
        Dataset dataset = TDBFactory.createDataset(TDB_DIRECTORY);
        dataset.begin(ReadWrite.READ);
        try {
            Model model = dataset.getDefaultModel();
            ResIterator iterator = model.listResourcesWithProperty(RDF.type, model.createResource(NS + "TipStudija"));

            while (iterator.hasNext()) {
                Resource res = iterator.next();
                Statement tipStmt = res.getProperty(model.getProperty(NS + "tip"));
                Statement deletedStmt = res.getProperty(model.getProperty(NS + "deleted"));

                boolean isDeleted = deletedStmt != null && deletedStmt.getBoolean();

                if (tipStmt != null && !isDeleted) {
                    TipStudijaDTO dto = new TipStudijaDTO();
                    dto.setTip(tipStmt.getString());
                    dto.setUri(res.getURI());
                    dto.setDeleted(false); // jer ih već filtriramo
                    result.add(dto);
                }
            }
        } finally {
            dataset.end();
        }
        return result;
    }
    
    public List<TipStudijaDTO> searchByTip(String queryText) {
        List<TipStudijaDTO> result = new ArrayList<>();
        Dataset dataset = TDBFactory.createDataset(TDB_DIRECTORY);
        dataset.begin(ReadWrite.READ);
        try {
            Model model = dataset.getDefaultModel();
            String sparqlQuery = String.format("""
                PREFIX ns: <%s>
                SELECT ?s ?tip WHERE {
                    ?s a ns:TipStudija ;
                       ns:tip ?tip .
                    FILTER(CONTAINS(LCASE(STR(?tip)), LCASE("%s")))
                    OPTIONAL { ?s ns:deleted ?deleted }
                    FILTER(!BOUND(?deleted) || ?deleted = false)
                }
                """, NS, queryText);

            Query query = QueryFactory.create(sparqlQuery);
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                while (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();
                    Resource s = soln.getResource("s");
                    Literal tip = soln.getLiteral("tip");

                    TipStudijaDTO dto = new TipStudijaDTO();
                    dto.setUri(s.getURI());
                    dto.setTip(tip.getString());
                    dto.setDeleted(false); // jer smo ih već filtrirali
                    result.add(dto);
                }
            }
        } finally {
            dataset.end();
        }
        return result;
    }
    
//    public List<TipStudijaDTO> searchByDeleted(String queryText) {
//        List<TipStudijaDTO> result = new ArrayList<>();
//        Dataset dataset = TDBFactory.createDataset(TDB_DIRECTORY);
//        dataset.begin(ReadWrite.READ);
//        try {
//            Model model = dataset.getDefaultModel();
//
//            // Pretvori unos korisnika u boolean vrednost ako je moguće
//            boolean deletedValue = queryText.trim().equalsIgnoreCase("true");
//
//            String sparqlQuery = String.format("""
//                PREFIX ns: <%s>
//                SELECT ?s ?tip ?deleted WHERE {
//                    ?s a ns:TipStudija ;
//                       ns:tip ?tip ;
//                       ns:deleted ?deleted .
//                    FILTER(STR(?deleted) = "%s")
//                }
//                """, NS, deletedValue);
//
//            Query query = QueryFactory.create(sparqlQuery);
//            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
//                ResultSet results = qexec.execSelect();
//                while (results.hasNext()) {
//                    QuerySolution soln = results.nextSolution();
//                    Resource s = soln.getResource("s");
//                    Literal tip = soln.getLiteral("tip");
//
//                    TipStudijaDTO dto = new TipStudijaDTO();
//                    dto.setUri(s.getURI());
//                    dto.setTip(tip.getString());
//                    dto.setDeleted(deletedValue);
//                    result.add(dto);
//                }
//            }
//        } finally {
//            dataset.end();
//        }
//        return result;
//    }
    
    public List<TipStudijaDTO> searchByDeleted(String queryText) {
        List<TipStudijaDTO> result = new ArrayList<>();
        Dataset dataset = TDBFactory.createDataset(TDB_DIRECTORY);
        dataset.begin(ReadWrite.READ);
        try {
            Model model = dataset.getDefaultModel();

            // Napravi SPARQL boolean literal (true ili false)
            String deletedValue = queryText.trim().equalsIgnoreCase("true") ? "true" : "false";

            // String sparqlQuery = String.format("""
            //     PREFIX ns: <%s>
            //     SELECT ?s ?tip ?deleted WHERE {
            //         ?s a ns:TipStudija ;
            //            ns:tip ?tip ;
            //            ns:deleted ?deleted .
            //         FILTER(?deleted = %s)
            //     }
            //     """, NS, deletedValue);

             String sparqlQuery = String.format("""
            	    PREFIX ns: <%s>
            	    SELECT ?s ?tip ?deleted WHERE {
            	        ?s a ns:TipStudija ;
            	           ns:tip ?tip .
            	        OPTIONAL { ?s ns:deleted ?deleted }
            	        %s
            	    }
            	    """, NS,
            	    deletedValue.equals("true")
            	        ? "FILTER(?deleted = true)"
            	        : "FILTER(!BOUND(?deleted) || ?deleted = false)"
            	);

            Query query = QueryFactory.create(sparqlQuery);
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                while (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();
                    Resource s = soln.getResource("s");
                    Literal tip = soln.getLiteral("tip");
                    Literal deleted = soln.getLiteral("deleted");

                    TipStudijaDTO dto = new TipStudijaDTO();
                    dto.setUri(s.getURI());
                    dto.setTip(tip.getString());
                    dto.setDeleted(deleted != null && deleted.getBoolean());
                    // dto.setDeleted(deleted.getBoolean());
                    result.add(dto);
                }
            }
        } finally {
            dataset.end();
        }
        return result;
    }






    public void saveTipStudija(String tip) {
        Dataset dataset = TDBFactory.createDataset(TDB_DIRECTORY);
        dataset.begin(ReadWrite.WRITE);
        try {
            Model model = dataset.getDefaultModel();

            Resource tipStudija = model.createResource(NS + "tipStudija-" + tip.hashCode());
            tipStudija.addProperty(RDF.type, model.createResource(NS + "TipStudija"));
            tipStudija.addProperty(model.createProperty(NS + "tip"), tip);

            dataset.commit();
        } finally {
            dataset.end();
        }
    }
    
    public void updateTipStudijaByUri(String uri, String noviTip) {
        Dataset dataset = TDBFactory.createDataset(TDB_DIRECTORY);
        dataset.begin(ReadWrite.WRITE);
        try {
            Model model = dataset.getDefaultModel();
            Resource res = model.getResource(uri);

            if (res == null || !res.hasProperty(model.getProperty(NS + "tip"))) {
                throw new RuntimeException("Tip studija ne postoji za URI: " + uri);
            }

            // Ažuriranje "tip" svojstva
            res.removeAll(model.getProperty(NS + "tip"));
            res.addProperty(model.getProperty(NS + "tip"), noviTip);

            dataset.commit();
        } finally {
            dataset.end();
        }
    }

    
    public TipStudijaDTO findByUri(String uri) {
        Dataset dataset = TDBFactory.createDataset(TDB_DIRECTORY);
        dataset.begin(ReadWrite.READ);
        try {
            Model model = dataset.getDefaultModel();
            Resource res = model.getResource(uri);

            if (res != null && res.hasProperty(model.getProperty(NS + "tip"))) {
                boolean deleted = res.hasProperty(model.getProperty(NS + "deleted")) &&
                                  res.getProperty(model.getProperty(NS + "deleted")).getBoolean();

                TipStudijaDTO dto = new TipStudijaDTO();
                dto.setUri(uri);
                dto.setTip(res.getProperty(model.getProperty(NS + "tip")).getString());
                dto.setDeleted(deleted);
                return dto;
            } else {
                throw new RuntimeException("Entitet nije pronađen.");
            }
        } finally {
            dataset.end();
        }
    }



    public void deleteTipStudija(String tip) {
        Dataset dataset = TDBFactory.createDataset(TDB_DIRECTORY);
        dataset.begin(ReadWrite.WRITE);
        try {
            Model model = dataset.getDefaultModel();
            Resource res = model.getResource(NS + "tipStudija-" + tip.hashCode());
            if (res != null) {
                model.removeAll(res, null, null);
                model.removeAll(null, null, res);
            }
            dataset.commit();
        } finally {
            dataset.end();
        }
    }
    
    public List<TipStudijaDTO> findAllAdminDto() {
        List<TipStudijaDTO> result = new ArrayList<>();
        Dataset dataset = TDBFactory.createDataset(TDB_DIRECTORY);
        dataset.begin(ReadWrite.READ);
        try {
            Model model = dataset.getDefaultModel();
            ResIterator iterator = model.listResourcesWithProperty(RDF.type, model.createResource(NS + "TipStudija"));
            while (iterator.hasNext()) {
                Resource res = iterator.next();
                Statement tipStmt = res.getProperty(model.getProperty(NS + "tip"));
                Statement deletedStmt = res.getProperty(model.getProperty(NS + "deleted"));
                if (tipStmt != null) {
                    TipStudijaDTO dto = new TipStudijaDTO();
                    dto.setTip(tipStmt.getString());
                    dto.setDeleted(deletedStmt != null && deletedStmt.getBoolean());
                    dto.setUri(res.getURI());
                    result.add(dto);
                }
            }
        } finally {
            dataset.end();
            dataset.close();
        }
        return result;
    }

}

