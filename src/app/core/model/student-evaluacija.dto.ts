export interface StudentEvaluacijaDTO {
  studentId: number;
  ime: string;
  prezime: string;
  brojIndeksa: string;
  evaluacijaId: number;
  tipEvaluacije: string;
  datumEvaluacije: Date;
  brojBodova: number;
  trenutnaOcena: number;
  ukupnoBodova: number;
}
