import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { TerminNastaveProfesorService } from '../../../core/services/termin-nastave-profesor.service';
import { ProfesorPredmetService } from '../../../core/services/profesor-predmet.service';
import { CommonModule } from '@angular/common';

@Component({
    standalone: true,
    selector: 'app-termini-nastave',
    templateUrl: './termini-nastave.component.html',
    styleUrls: ['./termini-nastave.component.css'],
    imports: [CommonModule, ReactiveFormsModule],

})
export class TerminiNastaveProfesorComponent implements OnInit {
    termini: any[] = [];
    prikaziFormu: { [key: number]: boolean } = {};
    formaIshod: { [key: number]: FormGroup } = {};

    constructor(
        private terminService: TerminNastaveProfesorService,
        private predmetService: ProfesorPredmetService,
        private fb: FormBuilder
    ) { }

    ngOnInit(): void {
        this.predmetService.getMojiPredmeti().subscribe(predmeti => {
            predmeti.forEach(predmet => {
                this.terminService.getByProfesorPredmetId(predmet.id).subscribe(data => {
                    this.termini.push(...data);
                    data.forEach(termin => {
                        this.formaIshod[termin.id] = this.fb.group({ tema: [''] });
                        this.prikaziFormu[termin.id] = false;
                    });
                });
            });
        });
    }

    otvoriFormu(id: number): void {
        this.prikaziFormu[id] = true;
    }

    sacuvajIshod(id: number): void {
    const tema = this.formaIshod[id].value.tema;
    this.terminService.azurirajTermin(id, tema).subscribe(() => {
        const termin = this.termini.find(t => t.id === id);
        if (termin) termin.ishodTema = tema;
        this.prikaziFormu[id] = false;
    });
}
}
