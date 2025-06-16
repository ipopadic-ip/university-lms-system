import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CreateUserRequest } from '../model/CreateUserRequest.model';
import { Observable } from 'rxjs';
import { User } from '../model/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) { }

  createUser(request: CreateUserRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/add`, request, {
      responseType: 'text' as const
    });
  }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/admin`);
  }

  deactivateUser(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}/delete`, {}, {
      responseType: 'text' as const,
    });
  }

  reactivateUser(userId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${userId}/restore`, {}, {
      responseType: 'text' as const
    });
  }

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  updateUser(id: number, data: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, data, {
      responseType: 'text' as const
    });
  }

  getMe(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/me`);
  }

  updateMyProfile(dto: { ime: string; prezime: string }) {
    return this.http.put(`${this.apiUrl}/me`, dto, {
      responseType: 'text' as const
    });
  }

  changePassword(oldPassword: string, newPassword: string) {
    return this.http.put(`${this.apiUrl}/change-password`, {
      oldPassword,
      newPassword,
    }, {
      responseType: 'text' as const
    });
  }

}
