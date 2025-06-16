import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import { Obavestenje } from '../../../core/model/obavestenje.model';
import { ObavestenjeService } from '../../../core/services/obavestenje.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-obavestenja',
  standalone: true,
  imports: [CommonModule], // << ovo omogućava korišćenje Angular pipe-ova kao što je | date
  templateUrl: './obavestenja.component.html',
  styleUrls: ['./obavestenja.component.css']
})
export class ObavestenjaComponent implements OnInit {
  obavestenja: Obavestenje[] = [];

  constructor(
    private obavestenjeService: ObavestenjeService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const profesorId = this.authService.getLoggedInUserId();

    this.obavestenjeService.getAll().subscribe({
      next: (data: Obavestenje[]) => {
        this.obavestenja = data;
      },
      error: (err) => {
        console.error('Greška pri učitavanju obaveštenja', err);
      }
    });
  }

  dodajObavestenje(): void {
    console.log('Kliknuto na dugme za dodavanje obaveštenja');
    this.router.navigate(['/profesor/obavestenja/novo']);
  }

  izmeniObavestenje(obavestenje: Obavestenje): void {
    this.router.navigate(['/profesor/obavestenja/izmeni', obavestenje.id]);
  }

  obrisi(id: number): void {
  if (confirm('Da li ste sigurni da želite da obrišete ovo obaveštenje?')) {
    this.obavestenjeService.delete(id).subscribe({
      next: () => {
        // Osveži listu nakon brisanja
        this.obavestenja = this.obavestenja.filter(o => o.id !== id);
      },
      error: (err) => {
        console.error('Greška prilikom brisanja:', err);
      }
    });
  }
}
}
