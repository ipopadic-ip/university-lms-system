import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { StudentService } from '../../../../core/services/student.service';
import { StudentUProfesor } from '../../../../core/model/student-u-profesor.model';
import {
  StudentIstorijaStudiranjaResponseDTOProfesor,
  PolaganjeDTO
} from '../../../../core/model/student-istorija-studiranja.model';



@Component({
  selector: 'app-student-detalji',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './student-detalji.component.html',
  styleUrls: ['./student-detalji.component.css']
})
export class StudentDetaljiComponent implements OnInit {
  student: StudentUProfesor | null = null;
  istorija: StudentIstorijaStudiranjaResponseDTOProfesor | null = null;
  loading = true;
  error: string | null = null;

  polozeniFiltrirani: PolaganjeDTO[] = [];


  constructor(
    private route: ActivatedRoute,
    private studentService: StudentService
  ) { }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    if (!id) {
      this.error = 'Nevažeći ID studenta.';
      this.loading = false;
      return;
    }

    this.studentService.getStudentById(id).subscribe({
      next: (s) => {
        this.student = s;
      },
      error: () => {
        this.error = 'Greška prilikom učitavanja podataka o studentu.';
      }
    });

    this.studentService.getStudentIstorijaStudiranjaProfesor(id).subscribe({
      next: (istorija) => {
        console.log('Dobijena istorija:', istorija);
        if (istorija) {
          const predmeti = (istorija as any).predmeti ?? [];

          this.istorija = {
            prosecnaOcena: istorija.prosecnaOcena,
            ukupnoEcts: (istorija as any).ukupnoECTS,
            upisi: [],
            polozeni: predmeti.filter((p: PolaganjeDTO) => p.ocena >= 6),
            neuspesni: predmeti.filter((p: PolaganjeDTO) => p.ocena < 6),
            prijavljeni: [],
            prestupi: []
          };

          this.polozeniFiltrirani = this.istorija.polozeni;
        }
        this.loading = false;
      },
      error: () => {
        this.error = 'Greška prilikom učitavanja istorije studiranja.';
        this.loading = false;
      }
    });


  }
}
