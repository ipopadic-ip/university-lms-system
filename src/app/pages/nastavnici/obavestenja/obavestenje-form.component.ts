import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Obavestenje } from '../../../core/model/obavestenje.model';
import { ObavestenjeService } from '../../../core/services/obavestenje.service';
import { PredmetService } from '../../../core/services/predmet.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-obavestenje-form',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './obavestenje-form.component.html',
  styleUrls: ['./obavestenje-form.component.css']
})
export class ObavestenjeFormComponent implements OnInit {
  obavestenje: Obavestenje = {
    id: 0,
    tekst: '',
    predmetId: 0,
    predmetNaziv: '',
    autorId: 0,
    autorIme: ''
  };

  isEditing: boolean = false;
  predmeti: any[] = [];

  constructor(
    private obavestenjeService: ObavestenjeService,
    private predmetService: PredmetService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    const profesorId = this.authService.getLoggedInUserId();

    // Učitaj listu predmeta
    this.predmetService.getPredmetiZaProfesora(profesorId).subscribe((data) => {
      this.predmeti = data;
    });

    if (id) {
      this.isEditing = true;
      this.obavestenjeService.getByIdForProfesor(+id).subscribe((data) => {
        this.obavestenje = data;
      });
    } else {
      this.isEditing = false;
      this.obavestenje = {
        id: 0,
        tekst: '',
        predmetId: 0,
        predmetNaziv: '',
        autorId: 0,
        autorIme: ''
      };
    }
  }

  onSubmit(): void {
    const obavestenjeZaSlanje = { ...this.obavestenje };
    delete obavestenjeZaSlanje['datum']; // datum postavlja backend

    const operation = this.isEditing
      ? this.obavestenjeService.update(this.obavestenje.id!, obavestenjeZaSlanje)
      : this.obavestenjeService.create(obavestenjeZaSlanje);

    operation.subscribe({
      next: () => this.router.navigate(['/profesor/obavestenja']),
      error: (err) => console.error('Greška pri snimanju obaveštenja:', err)
    });
  }

  cancel(): void {
    this.router.navigate(['/profesor/obavestenja']);
  }

  obrisi(): void {
    if (confirm('Da li ste sigurni da želite da obrišete ovo obaveštenje?')) {
      this.obavestenjeService.delete(this.obavestenje.id!).subscribe({
        next: () => this.router.navigate(['/profesor/obavestenja']),
        error: (err) => console.error('Greška prilikom brisanja:', err)
      });
    }
  }
}
