import { Routes } from '@angular/router';
import { SluzbenikDashboardComponent } from './sluzbenik-dashboard/sluzbenik-dashboard.component';

// export const SLUZBENIK_ROUTES: Routes = [
//       {
//         path: '',
//         loadComponent: () => import('./sluzbenik-dashboard/sluzbenik-dashboard.component')
//           .then(m => m.SluzbenikDashboardComponent)
//       },
//       {
//         path: 'moj-profil',
//         loadComponent: () => import('./sluzbenik-profil/sluzbenik-profil.component')
//           .then(m => m.SluzbenikProfilComponent)
//       },
//       {
//         path: 'trebovanje',
//         loadComponent: () => import('./trebovanje/trebovanje.component')
//           .then(m => m.TrebovanjeComponent)
//       },
//       {
//         path: 'potvrde',
//         loadComponent: () => import('./potvrde/potvrde.component')
//           .then(m => m.PotvrdeComponent)
//       },
//       {
//         path: 'obavestenja',
//         loadComponent: () => import('./opsta_obavestenja/opsta-obavestenja.component')
//           .then(m => m.OpstaObavestenjaComponent)
//       },

//       {
//         path: 'upis',
//         loadComponent: () => import('./upis-studenta/upis-studenta.component')
//           .then(m => m.UpisStudentaComponent)
//       },{
//         path: 'biblioteka',
//         loadComponent: () => import('./biblioteka/biblioteka.component')
//           .then(m => m.BibliotekaComponent)
//       },
//       {
//         path: 'termini-nastave',
//         loadComponent: () => import('./termin-nastave/termin-nastave.component')
//           .then(m => m.TerminiNastaveComponent)
//       },
//       {
//         path: 'kreiraj-evaluaciju',
//         loadComponent: () => import('./kreiraj-evaluaciju/kreiraj-evaluaciju.component')
//           .then(m => m.KreirajEvaluacijuComponent)
//       },
//       {
//         path: 'kreiraj-prijavu-ispita',
//         loadComponent: () => import('./kreiraj-prijavu-ispita/kreiraj-prijavu-ispita.component')
//           .then(m => m.KreirajPrijavuIspitaComponent)
//       },
//       {
//               path: 'trebovanje-knjiga',
//               loadComponent: () => import('./trebovanje-knjiga/trebovanje-knjiga.component')
//                 .then(m => m.TrebovanjeKnjigaComponent)
//       }
// ];

export const SLUZBENIK_ROUTES: Routes = [
  {
    path: '',
    component: SluzbenikDashboardComponent,
    children: [
      { path: '', redirectTo: 'obavestenja', pathMatch: 'full' },
      {
        path: 'moj-profil',
        loadComponent: () => import('./sluzbenik-profil/sluzbenik-profil.component')
          .then(m => m.SluzbenikProfilComponent)
      },
      {
        path: 'trebovanje',
        loadComponent: () => import('./trebovanje/trebovanje.component')
          .then(m => m.TrebovanjeComponent)
      },
      {
        path: 'potvrde',
        loadComponent: () => import('./potvrde/potvrde.component')
          .then(m => m.PotvrdeComponent)
      },
      {
        path: 'obavestenja',
        loadComponent: () => import('./opsta_obavestenja/opsta-obavestenja.component')
          .then(m => m.OpstaObavestenjaComponent)
      },

      {
        path: 'upis',
        loadComponent: () => import('./upis-studenta/upis-studenta.component')
          .then(m => m.UpisStudentaComponent)
      },{
        path: 'biblioteka',
        loadComponent: () => import('./biblioteka/biblioteka.component')
          .then(m => m.BibliotekaComponent)
      },
      {
        path: 'termini-nastave',
        loadComponent: () => import('./termin-nastave/termin-nastave.component')
          .then(m => m.TerminiNastaveComponent)
      },
      {
        path: 'kreiraj-evaluaciju',
        loadComponent: () => import('./kreiraj-evaluaciju/kreiraj-evaluaciju.component')
          .then(m => m.KreirajEvaluacijuComponent)
      },
      {
        path: 'kreiraj-prijavu-ispita',
        loadComponent: () => import('./kreiraj-prijavu-ispita/kreiraj-prijavu-ispita.component')
          .then(m => m.KreirajPrijavuIspitaComponent)
      },
      {
              path: 'trebovanje-knjiga',
              loadComponent: () => import('./trebovanje-knjiga/trebovanje-knjiga.component')
                .then(m => m.TrebovanjeKnjigaComponent)
      },
      {
              path: 'dokument',
              loadComponent: () => import('./dokument/dokument-list/dokument-list.component')
                .then(m => m.DokumentListComponent)
      },
      {
              path: 'dokument/add',
              loadComponent: () => import('./dokument/dokument-form/dokument-form.component')
                .then(m => m.DokumentFormComponent)
      },
      {
              path: 'dokument/edit/:id',
              loadComponent: () => import('./dokument/dokument-form/dokument-form.component')
                .then(m => m.DokumentFormComponent)
      },
      {
              path: 'verzije',
              loadComponent: () => import('./verzija-dokumenta/verzija-dokumenta-list/verzija-dokumenta-list.component')
                .then(m => m.VerzijaDokumentaListComponent)
      },
      {
              path: 'verzije/edit/:id',
              loadComponent: () => import('./verzija-dokumenta/verzija-dokumenta-form/verzija-dokumenta-form.component')
                .then(m => m.VerzijaDokumentaFormComponent)
      },
      {
              path: 'verzije/add',
              loadComponent: () => import('./verzija-dokumenta/verzija-dokumenta-form/verzija-dokumenta-form.component')
                .then(m => m.VerzijaDokumentaFormComponent)
      },
    ]
  }
];