import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ZakazaneEvaluacijeComponent } from './zakazane-evaluacije.component';
import { AuthService } from '../../../core/services/auth.service';

@Component({
    selector: 'app-zakazane-evaluacije-stranica',
    standalone: true,
    imports: [CommonModule, ZakazaneEvaluacijeComponent],
    template: `
    <div class="container">
      <h2>Zakazane evaluacije</h2>
      <app-zakazane-evaluacije
        [predmetId]="predmetId"
        [profesorId]="profesorId">
      </app-zakazane-evaluacije>
    </div>
  `
})
export class ZakazaneEvaluacijeStranicaComponent implements OnInit {
    predmetId!: number;
    profesorId!: number;

    constructor(private route: ActivatedRoute, private authService: AuthService) { }

    ngOnInit(): void {
        this.predmetId = +this.route.snapshot.paramMap.get('predmetId')!;
        this.profesorId = this.authService.getLoggedInUserId(); // SIGURNO VRAĆA ID
    }
}
