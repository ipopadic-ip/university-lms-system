import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { StorageService } from './storage.service'; 

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth/login';
  private tokenKey = 'authToken';
  private rolesKey = 'userRoles';

  isLoggedIn$ = new BehaviorSubject<boolean>(false);

  constructor(
    private http: HttpClient,
    private router: Router,
    private storageService: StorageService 
  ) {
    this.isLoggedIn$.next(this.hasToken());
  }

  login(email: string, password: string): Observable<any> {
    return this.http.post<{ token: string }>(this.apiUrl, { email, password }).pipe(
      tap(response => {
        const token = response.token;
        this.storeToken(token);
        this.isLoggedIn$.next(true);
      })
    );
  }

  logout(): void {
    this.storageService.removeItem(this.tokenKey);
    this.storageService.removeItem(this.rolesKey);
    this.isLoggedIn$.next(false);
    this.router.navigate(['/prijava']);
  }

  private storeToken(token: string): void {
    this.storageService.setItem(this.tokenKey, token);
    const payload = JSON.parse(atob(token.split('.')[1]));
    this.storageService.setItem(this.rolesKey, JSON.stringify(payload.roles));
  }

  getToken(): string | null {
    return this.storageService.getItem(this.tokenKey);
  }

  getRoles(): string[] {
    const roles = this.storageService.getItem(this.rolesKey);
    return roles ? JSON.parse(roles) : [];
  }

  hasToken(): boolean {
    return !!this.storageService.getItem(this.tokenKey);
  }

  isTokenExpired(token: string): boolean {
    if (!token) {
      return true;
    }
    const payload = JSON.parse(atob(token.split('.')[1]));
    const expiryTime = payload.exp; // exp je u sekundama
  
    const currentTime = Math.floor(Date.now() / 1000); // sekunde
    return expiryTime < currentTime;
  }

  hasRole(role: string): boolean {
    const roles = this.getRoles();
    return roles.includes(role);
  }

  getLoggedInUserId(): number {
  const token = this.getToken();
  if (!token) {
    console.warn('❗ Token nije pronađen.');
    return 0;
  }
  // if (!token) return 0;

  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.id || 0; // <- koristi tačno naziv "id"
  } catch (e) {
    console.error('Greška pri dekodiranju tokena', e);
    return 0;
  }
}

  getCurrentUser(): any {
  const token = this.getToken();
  if (!token) return null;

  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    console.log(payload);

    return {
      id: payload.id,
      email: payload.sub,
      roles: payload.roles
    };
  } catch (e) {
    console.error('Greška pri dekodiranju tokena', e);
    return null;
  }
}
  
}
