import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Silabus } from '../model/silabus.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SilabusService {
  private apiUrl = 'http://localhost:8080/api/silabus';

  constructor(private http: HttpClient) {}

  getSilabusByPredmetId(predmetId: number): Observable<Silabus> {
    return this.http.get<Silabus>(`${this.apiUrl}/predmet/${predmetId}`);
  }

  updateSilabus(id: number, silabus: Silabus): Observable<Silabus> {
    return this.http.put<Silabus>(`${this.apiUrl}/${id}`, silabus);
  }
}
