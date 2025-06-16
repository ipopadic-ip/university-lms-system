import { Component, OnInit } from '@angular/core';
import { OcenjivanjeService } from '../../../core/services/ocenjivanje.service';
import { StudentEvaluacijaDTO } from '../../../core/model/student-evaluacija.dto';
import { Predmet } from '../../../core/model/predmet.model';
import { ProfesorService } from '../../../core/services/profesor.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-ocenjivanje-studenata',
  templateUrl: './ocenjivanje-studenata.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule]
})
export class OcenjivanjeStudenataComponent implements OnInit {
  predmeti: Predmet[] = [];
  izabraniPredmetId: number | null = null;
  evaluacije: StudentEvaluacijaDTO[] = [];
  filtriraneEvaluacije: StudentEvaluacijaDTO[] = [];
  noviBodovi: { [evaluacijaId: number]: number } = {};
  izabraniTip: string = '';
  tipoviEvaluacije: string[] = [];
  searchTerm: string = '';

  constructor(
    private profesorService: ProfesorService,
    private ocenjivanjeService: OcenjivanjeService
  ) {}

  ngOnInit(): void {
    this.profesorService.getMojiPredmeti().subscribe(predmeti => {
      this.predmeti = predmeti;
    });
  }

  onPredmetChange(): void {
    if (this.izabraniPredmetId) {
      this.ocenjivanjeService.getEvaluacijeZaPredmet(this.izabraniPredmetId).subscribe(data => {
        const sada = new Date();
        this.evaluacije = data.filter(e => {
          const datum = new Date(e.datumEvaluacije);
          const razlikaDana = (sada.getTime() - datum.getTime()) / (1000 * 60 * 60 * 24);
          return razlikaDana <= 15;
        });
        this.tipoviEvaluacije = [...new Set(this.evaluacije.map(e => e.tipEvaluacije))];
        this.filtrirajEvaluacije();
        this.noviBodovi = {};
      });
    }
  }

  filtrirajEvaluacije(): void {
    const search = this.searchTerm.toLowerCase().trim();
    this.filtriraneEvaluacije = this.evaluacije.filter(e => {
      const matchesTip = !this.izabraniTip || e.tipEvaluacije === this.izabraniTip;
      const matchesSearch =
        (e.ime && e.ime.toLowerCase().includes(search)) ||
        (e.prezime && e.prezime.toLowerCase().includes(search)) ||
        (e.brojIndeksa && e.brojIndeksa.toLowerCase().includes(search));
      return matchesTip && matchesSearch;
    });
  }

  sacuvajBodove(evaluacijaId: number): void {
    const bodovi = this.noviBodovi[evaluacijaId];
    if (bodovi == null || isNaN(bodovi)) return;

    this.ocenjivanjeService.upisiBodove(evaluacijaId, bodovi).subscribe(() => {
      this.onPredmetChange();
    });
  }
}
