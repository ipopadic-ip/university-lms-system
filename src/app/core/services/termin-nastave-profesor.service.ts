import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TerminNastaveProfesor } from '../model/termin-nastave-profesor.model';

@Injectable({ providedIn: 'root' })
export class TerminNastaveProfesorService {
    private baseUrl = '/api/termin-nastave';

    constructor(private http: HttpClient) { }

    getByProfesorPredmetId(id: number): Observable<TerminNastaveProfesor[]> {
        return this.http.get<any[]>(`http://localhost:8080/api/termin-nastave/profesor-predmet/${id}`);
    }

    azurirajTermin(id: number, noviIshod: string): Observable<void> {
        return this.http.put<void>(
            `http://localhost:8080/api/termin-nastave/${id}/ishod`,
            JSON.stringify(noviIshod),
            { headers: { 'Content-Type': 'application/json' } }
        );
    }

}
