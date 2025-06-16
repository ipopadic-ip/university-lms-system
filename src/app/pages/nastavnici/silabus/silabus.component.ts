import { Component, OnInit } from '@angular/core';
import { SilabusService } from '../../../core/services/silabus.service';
import { Silabus } from '../../../core/model/silabus.model';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-silabus',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './silabus.component.html',
  styleUrls: ['./silabus.component.css']
})
export class SilabusComponent implements OnInit {
  silabus: Silabus = { id: 0, sadrzaj: '', predmetId: 0 };
  originalSadrzaj = '';
  editMode = false;
  predmetId = 0;

  constructor(
    private silabusService: SilabusService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.predmetId = idParam ? +idParam : 0;

    if (this.predmetId > 0) {
      this.silabusService.getSilabusByPredmetId(this.predmetId).subscribe({
        next: (data: Silabus) => {
          this.silabus = data;
          this.originalSadrzaj = data.sadrzaj;
        },
        error: (err: any) => console.error('Greška pri dohvatanju silabusa:', err)
      });
    }
  }

  toggleEdit(): void {
    this.editMode = true;
  }

  cancelEdit(): void {
    this.silabus.sadrzaj = this.originalSadrzaj;
    this.editMode = false;
  }

  saveChanges(): void {
    if (this.silabus && this.silabus.id) {
      this.silabusService.updateSilabus(this.silabus.id, this.silabus).subscribe({
        next: (updated: Silabus) => {
          this.silabus = updated;
          this.originalSadrzaj = updated.sadrzaj;
          this.editMode = false;
        },
        error: (err: any) => {
          console.error('Greška pri čuvanju izmena:', err);
        }
      });
    }
  }
}
