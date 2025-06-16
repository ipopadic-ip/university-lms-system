package com.unidunav.predmet.dto;

import java.util.List;

public class StudentIstorijaStudiranjaResponseDTOProfesor {
	private List<IstorijaStudiranjaDTO> upisi;
    private List<IstorijaStudiranjaDTO> polozeni;
    private List<IstorijaStudiranjaDTO> neuspesni;
    private List<IstorijaStudiranjaDTO> prijavljeni;
    private List<IstorijaStudiranjaDTO> prestupi;
    private Object zavrsniRad;
    private Double prosecnaOcena;
    private int ukupnoECTS;

    

    public StudentIstorijaStudiranjaResponseDTOProfesor(List<IstorijaStudiranjaDTO> upisi,
			List<IstorijaStudiranjaDTO> polozeni, List<IstorijaStudiranjaDTO> neuspesni,
			List<IstorijaStudiranjaDTO> prijavljeni, List<IstorijaStudiranjaDTO> prestupi, Object zavrsniRad,
			Double prosecnaOcena, int ukupnoECTS) {
		super();
		this.upisi = upisi;
		this.polozeni = polozeni;
		this.neuspesni = neuspesni;
		this.prijavljeni = prijavljeni;
		this.prestupi = prestupi;
		this.zavrsniRad = zavrsniRad;
		this.prosecnaOcena = prosecnaOcena;
		this.ukupnoECTS = ukupnoECTS;
	}
	// Getteri i setteri
    public List<IstorijaStudiranjaDTO> getUpisi() { return upisi; }
    public void setUpisi(List<IstorijaStudiranjaDTO> upisi) { this.upisi = upisi; }

    public List<IstorijaStudiranjaDTO> getPolozeni() { return polozeni; }
    public void setPolozeni(List<IstorijaStudiranjaDTO> polozeni) { this.polozeni = polozeni; }

    public List<IstorijaStudiranjaDTO> getNeuspesni() { return neuspesni; }
    public void setNeuspesni(List<IstorijaStudiranjaDTO> neuspesni) { this.neuspesni = neuspesni; }

    public List<IstorijaStudiranjaDTO> getPrijavljeni() { return prijavljeni; }
    public void setPrijavljeni(List<IstorijaStudiranjaDTO> prijavljeni) { this.prijavljeni = prijavljeni; }

    public List<IstorijaStudiranjaDTO> getPrestupi() { return prestupi; }
    public void setPrestupi(List<IstorijaStudiranjaDTO> prestupi) { this.prestupi = prestupi; }

    public Object getZavrsniRad() { return zavrsniRad; }
    public void setZavrsniRad(Object zavrsniRad) { this.zavrsniRad = zavrsniRad; }

    public Double getProsecnaOcena() { return prosecnaOcena; }
    public void setProsecnaOcena(Double prosecnaOcena) { this.prosecnaOcena = prosecnaOcena; }

    public int getUkupnoECTS() { return ukupnoECTS; }
    public void setUkupnoECTS(int ukupnoECTS) { this.ukupnoECTS = ukupnoECTS; }
}
