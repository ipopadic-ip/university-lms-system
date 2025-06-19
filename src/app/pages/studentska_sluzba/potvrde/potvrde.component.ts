import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { StudentService } from '../../../core/services/student.service';
import { PotvrdaService } from '../../../core/services/potvrda.service';

@Component({
  selector: 'app-potvrde',
  standalone: true,
  templateUrl: './potvrde.component.html',
  styleUrls: ['./potvrde.component.css'],
  imports: [CommonModule, ReactiveFormsModule, FormsModule] 
})
export class PotvrdeComponent implements OnInit {
  forma!: FormGroup;
  studenti: any[] = [];
  potvrde: any[] = [];
  pretraga: string = '';
  selektovaniStudentId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private studentService: StudentService,
    private potvrdaService: PotvrdaService
  ) {}

  ngOnInit(): void {
    this.forma = this.fb.group({
      studentId: [null, Validators.required],
      tip: ['', Validators.required],
      tekst: ['', [Validators.required, Validators.maxLength(1000)]]
    });

    this.ucitajPotvrde();
  }

  pretraziStudente(): void {
    if (this.pretraga.length < 2) {
      this.studenti = [];
      return;
    }

    this.studentService.pretraziPoBrojuIndeksa(this.pretraga).subscribe(res => {
      this.studenti = res;

      // Ako smo već izabrali studenta, zadrži ga u selekciji
      if (this.selektovaniStudentId) {
        const postoji = this.studenti.find(s => s.id === this.selektovaniStudentId);
        if (postoji) {
          this.forma.get('studentId')?.setValue(this.selektovaniStudentId);
        }
      }
    });
  }

  izdajPotvrdu(): void {
    if (this.forma.invalid) return;

    const selectedId = this.forma.get('studentId')?.value;
    this.selektovaniStudentId = selectedId;

    this.potvrdaService.izdajPotvrdu(this.forma.value).subscribe(() => {
      this.forma.patchValue({ tip: '', tekst: '' });  // resetuj samo unos, ne studentId
      this.ucitajPotvrde();
    });
  }

  private ucitajPotvrde(): void {
    this.potvrdaService.getAll().subscribe(res => {
      this.potvrde = res;
    });
  }
}
