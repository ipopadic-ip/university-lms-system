import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProfesorService } from '../../../core/services/profesor.service';

@Component({
    selector: 'app-profesor-profil',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './profesor-profil.component.html'
})
export class ProfesorProfilComponent implements OnInit {
    ime: string = '';
    prezime: string = '';
    biografija: string = '';
    novaSlika: File | null = null;
    slikaPath: string = '';
    staraLozinka: string = '';
    novaLozinka: string = '';


    constructor(private profesorService: ProfesorService) { }

    ngOnInit(): void {
        this.profesorService.getProfil().subscribe(data => {
            this.ime = data.ime;
            this.prezime = data.prezime;
            this.biografija = data.biografija;
            this.slikaPath = data.slikaPath;
        });
    }

    onFileSelected(event: any) {
        this.novaSlika = event.target.files[0];
    }

    sacuvaj() {
        const formData = new FormData();
        formData.append('ime', this.ime);
        formData.append('prezime', this.prezime);
        formData.append('biografija', this.biografija);
        formData.append('staraLozinka', this.staraLozinka);
        formData.append('novaLozinka', this.novaLozinka);
        if (this.novaSlika) formData.append('slika', this.novaSlika);

        this.profesorService.izmeniProfil(formData).subscribe({
            next: () => {
                alert('Uspešno sačuvano');
                this.staraLozinka = '';
                this.novaLozinka = '';
            },
            error: (err) => {
                if (err.error && err.error.message && err.error.message.includes('Stara lozinka nije tačna')) {
                    alert('Pogrešna stara lozinka.');
                } else {
                    alert('Greška prilikom čuvanja.');
                }
            }
        });
    }


}
