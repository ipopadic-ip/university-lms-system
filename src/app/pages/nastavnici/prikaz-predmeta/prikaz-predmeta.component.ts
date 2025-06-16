import { Component, OnInit } from '@angular/core'; 
import { Predmet } from '../../../core/model/predmet.model';
import { CommonModule } from '@angular/common';
import { PredmetService } from '../../../core/services/predmet.service';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router'; 

@Component({
  selector: 'app-pregled-predmeta',
  standalone: true,
  imports: [CommonModule], 
  templateUrl: './prikaz-predmeta.component.html',
  styleUrls: ['./prikaz-predmeta.component.css']
})
export class PregledPredmetaComponent implements OnInit {
  predmeti: Predmet[] = [];
  profesorId: number = 0;

  constructor(
    private predmetService: PredmetService, 
    private authService: AuthService,
    private router: Router 
  ) {}

  ngOnInit(): void {
    const profesorId = this.authService.getLoggedInUserId();
    this.profesorId = profesorId;
    this.predmetService.getPredmetiZaProfesora(profesorId).subscribe({
      next: (data) => {
        this.predmeti = data;
      },
      error: (err) => {
        console.error('Greška pri učitavanju predmeta', err);
      }
    });
  }

  otvoriSilabus(predmetId: number): void {
    this.router.navigate(['/profesor/silabus', predmetId]);
  }

  otvoriStudente(predmetId: number): void {
    this.router.navigate(['/profesor/predmet', predmetId, 'studenti']);
  }

  otvoriEvaluacije(predmetId: number): void {
    this.router.navigate(['/profesor/predmet', predmetId, 'evaluacije']);
  }
}
