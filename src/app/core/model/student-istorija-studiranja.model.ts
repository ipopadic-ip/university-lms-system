export interface PolaganjeDTO {
  nazivPredmeta: string;
  brojPolaganja: number;
  ocena: number;
  brojECTS: number;
  brojBodova: number;
}

export interface PrijavljenIspit {
  nazivPredmeta: string;
}

export interface Prestup {
  nazivPredmeta: string;
  opis: string;
}

export interface StudentIstorijaStudiranjaResponseDTO {
  prosecnaOcena: number;
  ukupnoEcts: number;
  upisi: any[];
  polozeni: PolaganjeDTO[];
  neuspesni: PolaganjeDTO[];
  prijavljeni: PrijavljenIspit[];
  prestupi: Prestup[];
}

export interface StudentIstorijaStudiranjaResponseDTOProfesor {
  prosecnaOcena: number;
  ukupnoEcts: number;
  upisi: any[];
  polozeni: PolaganjeDTO[];
  neuspesni: PolaganjeDTO[];
  prijavljeni: PrijavljenIspit[];
  prestupi: Prestup[];
}
