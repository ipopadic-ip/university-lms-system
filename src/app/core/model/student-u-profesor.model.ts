import { User } from './user.model';

export interface StudentUProfesor {
  id: number;
  ime: string;
  user: User;
  prezime: string;
  brojIndeksa: string;
  godinaUpisa: number;
  prosecnaOcena: number;
  ukupnoEcts: number;
  slikaPath?: string;
  zavrsniRad?: string;
  predmeti?: string[];
  evaluacije?: any[];
}
