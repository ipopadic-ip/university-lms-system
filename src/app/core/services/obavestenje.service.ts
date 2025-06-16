import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Obavestenje } from '../model/obavestenje.model';

@Injectable({ providedIn: 'root' })
export class ObavestenjeService {
  private apiUrl = 'http://localhost:8080/api/obavestenja';

  constructor(private http: HttpClient) { }

  getAll(): Observable<Obavestenje[]> {
  return this.http.get<Obavestenje[]>(`${this.apiUrl}/profesor`);
}

  getById(id: number): Observable<Obavestenje> {
    return this.http.get<Obavestenje>(`${this.apiUrl}/${id}`);
  }

  create(obavestenje: Obavestenje): Observable<Obavestenje> {
    return this.http.post<Obavestenje>(this.apiUrl, obavestenje);
  }

  update(id: number, obavestenje: Obavestenje): Observable<Obavestenje> {
    return this.http.put<Obavestenje>(`${this.apiUrl}/${id}`, obavestenje);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getByIdForProfesor(id: number): Observable<Obavestenje> {
  return this.http.get<Obavestenje>(`${this.apiUrl}/profesor/${id}`);
}

}
