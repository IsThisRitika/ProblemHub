import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageResponse, Problem, ProblemFilterParams, Tag, Technology } from '../../shared/models/problem.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProblemService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl;

  getProblems(filterParams: ProblemFilterParams = {}): Observable<PageResponse<Problem>> {
    let params = new HttpParams();

    if (filterParams.keyword) params = params.set('keyword', filterParams.keyword.trim());
    if (filterParams.domain) params = params.set('domain', filterParams.domain);
    if (filterParams.difficulty) params = params.set('difficulty', filterParams.difficulty);
    if (filterParams.projectType) params = params.set('projectType', filterParams.projectType);
    if (filterParams.technology) params = params.set('technology', filterParams.technology);
    if (filterParams.tag) params = params.set('tag', filterParams.tag);
    if (filterParams.status) params = params.set('status', filterParams.status);
    if (filterParams.page !== undefined) params = params.set('page', filterParams.page.toString());
    if (filterParams.size !== undefined) params = params.set('size', filterParams.size.toString());
    if (filterParams.sortBy) params = params.set('sortBy', filterParams.sortBy);
    if (filterParams.sortDir) params = params.set('sortDir', filterParams.sortDir);

    return this.http.get<PageResponse<Problem>>(`${this.apiUrl}/problems`, { params });
  }

  getProblemById(id: number): Observable<Problem> {
    return this.http.get<Problem>(`${this.apiUrl}/problems/${id}`);
  }

  searchProblems(keyword: string): Observable<PageResponse<Problem>> {
    const params = new HttpParams().set('keyword', keyword.trim());
    return this.http.get<PageResponse<Problem>>(`${this.apiUrl}/problems/search`, { params });
  }

  createProblem(data: any): Observable<Problem> {
    return this.http.post<Problem>(`${this.apiUrl}/problems`, data);
  }

  updateProblem(id: number, data: any): Observable<Problem> {
    return this.http.put<Problem>(`${this.apiUrl}/problems/${id}`, data);
  }

  deleteProblem(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/problems/${id}`);
  }

  archiveProblem(id: number): Observable<{ message: string; status: string }> {
    return this.http.patch<{ message: string; status: string }>(`${this.apiUrl}/problems/${id}/archive`, {});
  }

  getTechnologies(): Observable<Technology[]> {
    return this.http.get<Technology[]>(`${this.apiUrl}/technologies`);
  }

  getTags(): Observable<Tag[]> {
    return this.http.get<Tag[]>(`${this.apiUrl}/tags`);
  }
}
