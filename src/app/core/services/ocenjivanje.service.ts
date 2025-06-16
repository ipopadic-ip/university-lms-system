import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StudentEvaluacijaDTO } from '../model/student-evaluacija.dto';

@Injectable({ providedIn: 'root' })
export class OcenjivanjeService {
  private apiUrl = 'http://localhost:8080/api/ocenjivanje';

  constructor(private http: HttpClient) {}

  getEvaluacijeZaPredmet(predmetId: number): Observable<StudentEvaluacijaDTO[]> {
    return this.http.get<StudentEvaluacijaDTO[]>(`${this.apiUrl}/predmet/${predmetId}`);
  }

  upisiBodove(evaluacijaId: number, brojBodova: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${evaluacijaId}/bodovi?brojBodova=${brojBodova}`, {});
  }
}
