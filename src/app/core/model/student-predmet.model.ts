export interface StudentPredmetDTO {
  id: number;
  brojIndeksa: string;
  godinaUpisa: number;
  prosecnaOcena: number;
  ukupnoEcts: number;
  slikaPath?: string;
  zavrsniRad?: string;
  ime: string;
  prezime: string;
  predmet: string;
}
