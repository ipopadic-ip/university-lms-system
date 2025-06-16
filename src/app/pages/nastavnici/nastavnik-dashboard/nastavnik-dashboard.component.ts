import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-nastavnik-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './nastavnik-dashboard.component.html',
  styleUrls: ['./nastavnik-dashboard.component.css']
})
export class NastavnikDashboardComponent {
  constructor(private authService: AuthService, private router: Router) {}

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/prijava']);
  }
}
