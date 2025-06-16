import { Routes } from '@angular/router';
import { StudentDashboardComponent } from './student-dashboard/student-dashboard.component';
import { PregledPredmetaComponent } from './pregled_predmeta/pregled-predmeta.component';

export const STUDENT_ROUTES: Routes = [
  {
    path: '',
    component: StudentDashboardComponent,
    children: [
      { path: '', redirectTo: 'predmeti', pathMatch: 'full' },
      { path: 'predmeti', component: PregledPredmetaComponent },
      {
        path: 'moj-profil',
        loadComponent: () => import('./student-profil/student-profil.component')
          .then(m => m.StudentProfilComponent)
      },
      {
        path: 'obavestenja',
        loadComponent: () => import('./obavestenja/obavestenja.component')
          .then(m => m.ObavestenjaComponent)
      },
      {
        path: 'prijava',
        loadComponent: () => import('./prijava_ispita/prijava-ispita.component')
          .then(m => m.PrijavaIspitaComponent)
      },
      {
        path: 'istorija',
        loadComponent: () => import('./istorija-studiranja/istorija-studiranja.component')
        .then(m => m.IstorijaStudiranjaComponent)
      },
      {
        path: 'evaluacije',
        loadComponent: () => import('./evaluacije/evaluacije.component')
        .then(m => m.EvaluacijeComponent)
      }
    ]
  }
];