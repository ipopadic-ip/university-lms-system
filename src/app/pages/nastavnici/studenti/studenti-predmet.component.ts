import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StudentService } from '../../../core/services/student.service';
import { StudentUProfesor } from '../../../core/model/student-u-profesor.model';
import { StudentPredmetDTO } from '../../../core/model/student-predmet.model';

@Component({
  selector: 'app-studenti-predmet',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './studenti.component.html',
  styleUrls: ['./studenti.component.css']
})
export class StudentiPredmetComponent implements OnInit {
  studenti: StudentPredmetDTO[] = [];
  searchTerm: string = '';

  constructor(
    private route: ActivatedRoute,
    private studentService: StudentService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const predmetId = +this.route.snapshot.paramMap.get('predmetId')!;
    this.studentService.getStudentiZaPredmet(predmetId).subscribe(data => {
      this.studenti = data;
    });
  }

  get filtriraniStudenti(): StudentPredmetDTO[] {
  const search = this.searchTerm.toLowerCase().trim();
  return this.studenti.filter(s =>
    (s.ime && s.ime.toLowerCase().includes(search)) ||
    (s.prezime && s.prezime.toLowerCase().includes(search)) ||
    (s.brojIndeksa && s.brojIndeksa.toLowerCase().includes(search)) ||
    (s.godinaUpisa && s.godinaUpisa.toString().includes(search)) ||
    (s.prosecnaOcena !== null && s.prosecnaOcena.toString().includes(search))

  );
}

  otvoriDetalje(id: number): void {
    this.router.navigate(['/profesor/student', id]);
  }

  toggleEvaluacije(student: any): void {
  student.prikaziEvaluacije = !student.prikaziEvaluacije;
}

}
