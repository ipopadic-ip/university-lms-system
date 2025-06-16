import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { StudentService } from '../../../core/services/student.service';
import { StudentUProfesor } from '../../../core/model/student-u-profesor.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-studenti',
  standalone: true, // ← moraš dodati ovo
  imports: [CommonModule, FormsModule], // ← i ovo da bi ngModel radio
  templateUrl: './studenti.component.html',
  styleUrls: ['./studenti.component.css']
})
export class StudentiComponent implements OnInit {
  studenti: StudentUProfesor[] = [];
  searchTerm: string = '';

  constructor(private studentService: StudentService, private router: Router) { }

  ngOnInit(): void {
    this.studentService.getStudentiZaNastavnika().subscribe(data => {
      this.studenti = data;
    });
  }

  get filtriraniStudenti(): StudentUProfesor[] {
  const search = this.searchTerm.toLowerCase().trim();

  return this.studenti.filter(s =>
    (s.ime && s.ime.toLowerCase().includes(search)) ||
    (s.prezime && s.prezime.toLowerCase().includes(search)) ||
    (s.brojIndeksa && s.brojIndeksa.toLowerCase().includes(search)) ||
    (s.godinaUpisa && s.godinaUpisa.toString().includes(search)) ||
    (s.prosecnaOcena !== null && s.prosecnaOcena.toString().includes(search)) ||
    (s.predmeti && s.predmeti.some(p => p.toLowerCase().includes(search)))

  );
}



  otvoriDetalje(id: number): void {
    console.log("Detalji za studenta:", id);
    this.router.navigate(['/profesor/student', id]);
  }
}
