import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ProfesorPredmet } from '../model/profesor-predmet.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProfesorPredmetService {
  private baseUrl = 'http://localhost:8080/api/profesor-predmet';

  constructor(private http: HttpClient) { }

  getAllAdmin(): Observable<ProfesorPredmet[]> {
    return this.http.get<ProfesorPredmet[]>(`${this.baseUrl}/admin`);
  }

  deaktiviraj(id: number): Observable<void> {
    return this.http.patch<void>(`${this.baseUrl}/${id}/deaktiviraj`, {});
  }

  aktiviraj(id: number): Observable<void> {
    return this.http.patch<void>(`${this.baseUrl}/${id}/aktiviraj`, {});
  }

  createProfesorPredmet(dto: any) {
    return this.http.post<ProfesorPredmet>(`${this.baseUrl}`, dto);
  }

  updateProfesorPredmet(id: number, dto: any) {
    return this.http.put<ProfesorPredmet>(`${this.baseUrl}/${id}`, dto);
  }

  getProfesorPredmetiByProfesorId(id: number) {
    return this.http.get<ProfesorPredmet[]>(`${this.baseUrl}/profesor/${id}`);
  }

  getProfesorPredmetById(id: number) {
    return this.http.get<ProfesorPredmet>(`${this.baseUrl}/${id}`);
  }

  getMojiPredmeti(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8080/api/profesor-predmet/moji-termini');
  }

}
