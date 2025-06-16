import { Routes } from '@angular/router';
import { NastavnikDashboardComponent } from './nastavnik-dashboard/nastavnik-dashboard.component';
import { PregledPredmetaComponent } from './prikaz-predmeta/prikaz-predmeta.component';
import { ObavestenjaComponent } from './obavestenja/obavestenja.component';
import { ObavestenjeFormComponent } from './obavestenja/obavestenje-form.component';
import { StudentiPredmetComponent } from './studenti/studenti-predmet.component';


export const profesorRoutes: Routes = [
  {
    path: '',
    component: NastavnikDashboardComponent,
    children: [
      { path: '', redirectTo: 'predmeti', pathMatch: 'full' },
      { path: 'predmeti', component: PregledPredmetaComponent },
      {
        path: 'predmet/:predmetId/studenti',
        loadComponent: () => import('./studenti/studenti-predmet.component').then(m => m.StudentiPredmetComponent)
      },

      {
        path: 'silabus/:id',
        loadChildren: () => import('./silabus/silabus.routes').then(m => m.silabusRoutes)
      },
      {
        path: 'obavestenja',
        loadComponent: () => import('./obavestenja/obavestenja.component').then(m => m.ObavestenjaComponent)
      },
      {
        path: 'obavestenja/novo',
        component: ObavestenjeFormComponent
      },
      {
        path: 'obavestenja/izmeni/:id',
        component: ObavestenjeFormComponent
      },
      {
        path: 'studenti',
        loadComponent: () =>
          import('./studenti/studenti.component').then(m => m.StudentiComponent)
      },
      {
        path: 'student/:id',
        loadComponent: () => import('./studenti/student-detalji/student-detalji.component').then(m => m.StudentDetaljiComponent)
      },
      {
        path: 'termini-nastave',
        loadComponent: () =>
          import('./termini-nastave/termini-nastave.component').then(m => m.TerminiNastaveProfesorComponent)
      },
      {
        path: 'predmet/:predmetId/evaluacije',
        loadComponent: () => import('./zakazane-evaluacije/zakazane-evaluacije-stranica.component').then(m => m.ZakazaneEvaluacijeStranicaComponent)
      },
      {
        path: 'oceni',
        loadComponent: () =>
          import('./ocenjivanje-studenata/ocenjivanje-studenata.component').then(m => m.OcenjivanjeStudenataComponent)
      },
      {
        path: 'profil',
        loadComponent: () => import('./profesor-profil/profesor-profil.component').then(m => m.ProfesorProfilComponent)
      },
      {
        path: 'xml-evaluacija',
        loadComponent: () => import('./xml-evaluacija/xml-evaluacija.component').then(m => m.XmlEvaluacijaComponent)
      }







    ]
  }
];
