import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Problem } from '../../shared/models/problem.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BookmarkService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl;

  getUserBookmarks(): Observable<Problem[]> {
    return this.http.get<Problem[]>(`${this.apiUrl}/users/me/bookmarks`);
  }

  addBookmark(problemId: number): Observable<{ message: string; bookmarked: boolean }> {
    return this.http.post<{ message: string; bookmarked: boolean }>(
      `${this.apiUrl}/problems/${problemId}/bookmark`,
      {}
    );
  }

  removeBookmark(problemId: number): Observable<{ message: string; bookmarked: boolean }> {
    return this.http.delete<{ message: string; bookmarked: boolean }>(
      `${this.apiUrl}/problems/${problemId}/bookmark`
    );
  }
}
