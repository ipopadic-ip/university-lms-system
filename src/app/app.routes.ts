import { Routes } from '@angular/router';
import { NeregistrovaniKorisniciComponent } from './pages/neregistrovani_korisnici/neregistrovani-korisnici.component';
import { PrijavaComponent } from './pages/prijava/prijava.component';
import { RoleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  {
    path: '',
    component: NeregistrovaniKorisniciComponent, // Ovo je sada parent komponenta
    loadChildren: () => import('./pages/neregistrovani_korisnici/neregistrovani-korisnici.routes')
      .then(m => m.neregistrovaniKorisniciRoutes)
  },
  { path: 'prijava', component: PrijavaComponent },
  {
    path: 'admin',
    loadChildren: () => import('./pages/administrator/administrator.routes')
      .then(m => m.administratorRoutes),
    canActivate: [RoleGuard(['ADMIN'])],
  },
  {
    path: 'profesor',
    loadChildren: () => import('./pages/nastavnici/nastavnik.routes')
      .then(m => m.profesorRoutes),
    canActivate: [RoleGuard(['PROFESOR'])],
  },
  {
    path: 'student',
    loadChildren: () =>
      import('./pages/studenti/student.routes')
        .then(m => m.STUDENT_ROUTES),
    canActivate: [RoleGuard(['STUDENT'])],
  },
  {
    path: 'sluzbenik',
    loadChildren: () => import('./pages/studentska_sluzba/sluzbenik-routes')
      .then(m => m.SLUZBENIK_ROUTES),
    canActivate: [RoleGuard(['SLUZBENIK'])]
  },
  {
    path: 'komercijalista',
    loadChildren: () => import('./pages/komercijalista/komercijalista-routes')
      .then(m => m.KOMERCIJALISTA_ROUTES),
    canActivate: [RoleGuard(['KOMERCIJALISTA'])]
  }
];

