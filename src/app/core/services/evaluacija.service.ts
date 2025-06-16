import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { PromenaTipaEvaluacijeDTO } from '../model/promena-tipa-evaluacije.dto';

@Injectable({
  providedIn: 'root'
})
export class EvaluacijaService {
  private apiUrl = '/api/evaluacija-znanja';

  constructor(private http: HttpClient) { }

  kreirajEvaluaciju(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/kreiraj`, data);
  }

  getEvaluacijeZaPredmet(predmetId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/predmet/${predmetId}`);
  }

  updateTip(evaluacijaId: number, tipId: number): Observable<any> {
    return this.http.put(`/api/evaluacija-znanja/${evaluacijaId}/tip`, { tipEvaluacijeId: tipId });
  }


  getTipovi(): Observable<any[]> {
    return this.http.get<any[]>('/api/tip-evaluacije');
  }

  upisiBodove(evaluacijaId: number, dto: { brojBodova: number }): Observable<any> {
    return this.http.put(`/api/evaluacija-znanja/bodovi/${evaluacijaId}`, dto);
  }

  promeniTipEvaluacije(dto: { predmetId: number; vremePocetka: string; noviTipId: number }): Observable<any> {
    return this.http.post('/api/evaluacija-znanja/promeni-tip', dto, { responseType: 'text' });
  }

}
