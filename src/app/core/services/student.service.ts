import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { StudentUProfesor } from '../model/student-u-profesor.model';
import { StudentIstorijaStudiranjaResponseDTO } from '../model/student-istorija-studiranja.model';
import { StudentPredmetDTO } from '../model/student-predmet.model';

import { StudentIstorijaStudiranjaResponseDTOProfesor } from '../model/student-istorija-studiranja.model';

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  private apiUrl = '/api/student';

  private profesorUrl = 'http://localhost:8080/api/student/nastavnik';

  constructor(private http: HttpClient) { }

  getIstorijaStudiranja(studentId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${studentId}/istorija`);
  }

  pretraziPoBrojuIndeksa(indeks: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/pretraga?indeks=${indeks}`);
  }

  getStudentiZaNastavnika(): Observable<StudentUProfesor[]> {
    return this.http.get<StudentUProfesor[]>(`${this.profesorUrl}/studenti`);
  }

  getStudentById(id: number): Observable<StudentUProfesor> {
    return this.http.get<StudentUProfesor>(`http://localhost:8080/api/student/${id}`);
  }

  getStudentIstorijaStudiranja(id: number): Observable<StudentIstorijaStudiranjaResponseDTO> {
    return this.http.get<StudentIstorijaStudiranjaResponseDTO>(`http://localhost:8080/api/student/${id}/istorija`);
  }

  getStudentIstorijaStudiranjaProfesor(id: number): Observable<StudentIstorijaStudiranjaResponseDTOProfesor> {
  return this.http.get<StudentIstorijaStudiranjaResponseDTOProfesor>(`http://localhost:8080/api/student/${id}/istorija`);
}



  getStudentiZaPredmet(predmetId: number): Observable<StudentPredmetDTO[]> {
    return this.http.get<StudentPredmetDTO[]>(`http://localhost:8080/api/student/predmet/${predmetId}/studenti`);
  }

}