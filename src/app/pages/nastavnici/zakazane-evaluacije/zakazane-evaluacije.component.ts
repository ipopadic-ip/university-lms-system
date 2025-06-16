import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EvaluacijaService } from '../../../core/services/evaluacija.service';

@Component({
  selector: 'app-zakazane-evaluacije',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './zakazane-evaluacije.component.html',
  styleUrls: ['./zakazane-evaluacije.component.css']
})
export class ZakazaneEvaluacijeComponent implements OnInit {
  @Input() predmetId!: number;
  @Input() profesorId!: number;

  izabranoVreme: string = '';
  izabraniTipId: number = 0;

  evaluacije: any[] = [];
  jedinstvenaVremena: string[] = [];
  tipoviEvaluacije: any[] = [];

  constructor(private evaluacijaService: EvaluacijaService) {}

  ngOnInit(): void {
    this.ucitajEvaluacije();
    this.ucitajTipove();
  }

  ucitajEvaluacije(): void {
    this.evaluacijaService.getEvaluacijeZaPredmet(this.predmetId)
      .subscribe(data => {
        this.evaluacije = data;

        // Izdvajamo jedinstvena vremena evaluacija
        const unique = new Set<string>();
        data.forEach(e => {
          const iso = new Date(e.vremePocetka).toISOString();
          unique.add(iso);
        });

        // Konvertujemo u niz i sortiramo po datumu
        this.jedinstvenaVremena = Array.from(unique).sort();
      });
  }

  ucitajTipove(): void {
    this.evaluacijaService.getTipovi()
      .subscribe(data => this.tipoviEvaluacije = data);
  }

  sacuvaj(evaluacijaId: number, noviTipId: number): void {
    this.evaluacijaService.updateTip(evaluacijaId, noviTipId)
      .subscribe(() => alert('Uspešno sačuvano!'));
  }

  promeniGrupnoTipEvaluacije(): void {
    if (!this.izabranoVreme || !this.izabraniTipId) {
      alert('Izaberite datum evaluacije i tip.');
      return;
    }

    const dto = {
      predmetId: this.predmetId,
      vremePocetka: this.izabranoVreme,
      noviTipId: this.izabraniTipId
    };

    this.evaluacijaService.promeniTipEvaluacije(dto).subscribe({
      next: () => {
        alert('Uspešno sačuvan tip!');
        this.ucitajEvaluacije();
      },
      error: () => alert('Greška prilikom izmene.')
    });
  }
}
