import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Profesor } from '../model/profesor.model';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ProfesorService {

  private apiUrl = '/api/profesor';

  constructor(private http: HttpClient) { }

  getAllProfesori(): Observable<Profesor[]> {
    return this.http.get<Profesor[]>('http://localhost:8080/api/profesor');
  }

  getPredmetiZaProfesora(profesorId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${profesorId}/predmeti`);
  }

  getMojiPredmeti(): Observable<any[]> {
    return this.http.get<any[]>(`/api/profesor-predmet/moji`);
  }

  getProfil(): Observable<any> {
    return this.http.get<any>(`/api/profesor/profil`);
  }

  izmeniProfil(data: FormData): Observable<any> {
    return this.http.put<any>(`/api/profesor/profil`, data);
  }

}
