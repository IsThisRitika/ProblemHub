import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Tag, Technology } from '../../shared/models/problem.model';
import { environment } from '../../../environments/environment';

export interface AdminStats {
  totalProblems: number;
  publishedProblems: number;
  archivedProblems: number;
  draftProblems: number;
  totalUsers: number;
  totalStudents: number;
  totalBookmarks: number;
  totalTechnologies: number;
  totalTags: number;
}

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl;

  getStats(): Observable<AdminStats> {
    return this.http.get<AdminStats>(`${this.apiUrl}/admin/stats`);
  }

  createTechnology(name: string): Observable<Technology> {
    return this.http.post<Technology>(`${this.apiUrl}/technologies`, { name });
  }

  deleteTechnology(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/technologies/${id}`);
  }

  createTag(name: string): Observable<Tag> {
    return this.http.post<Tag>(`${this.apiUrl}/tags`, { name });
  }

  deleteTag(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/tags/${id}`);
  }
}
