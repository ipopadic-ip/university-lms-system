
export interface Fakultet {
  id: number;
  naziv: string;
  lokacija: string;
  email: string;
  opis: string;
  brojTelefona: string;

  dekanId:number;
  dekanIme: string;
  dekanPrezime: string;
  dekanOpis: string;
  dekanSlika: string;
  deleted?: boolean;
}
