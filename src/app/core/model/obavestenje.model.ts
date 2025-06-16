export interface Obavestenje {
  id: number;
  tekst: string;
  datum?: Date;
  predmetId: number;
  predmetNaziv: string;
  autorId: number;
  autorIme: string;
}
