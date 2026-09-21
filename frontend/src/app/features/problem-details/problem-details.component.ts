import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ProblemService } from '../../core/services/problem.service';
import { BookmarkService } from '../../core/services/bookmark.service';
import { AuthService } from '../../core/services/auth.service';
import { Problem } from '../../shared/models/problem.model';

@Component({
  selector: 'app-problem-details',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      <!-- Breadcrumb Navigation -->
      <nav class="flex items-center gap-2 text-xs text-slate-500 mb-6">
        <a routerLink="/problems" class="hover:text-indigo-600 font-medium">← Back to All Problems</a>
        <span>/</span>
        <span *ngIf="problem" class="text-slate-800 truncate font-semibold">{{ problem.title }}</span>
      </nav>

      <!-- Loading State -->
      <div *ngIf="loading" class="bg-white p-8 rounded-xl border border-slate-200 animate-pulse space-y-6">
        <div class="h-6 bg-slate-200 rounded w-1/4"></div>
        <div class="h-10 bg-slate-200 rounded w-3/4"></div>
        <div class="h-32 bg-slate-200 rounded w-full"></div>
      </div>

      <!-- Error State -->
      <div *ngIf="error && !loading" class="text-center p-12 bg-white border border-slate-200 rounded-xl">
        <h2 class="text-lg font-bold text-slate-800">Problem Statement Not Found</h2>
        <p class="text-xs text-slate-500 mt-2">{{ error }}</p>
        <a routerLink="/problems" class="inline-block mt-4 px-4 py-2 bg-indigo-600 text-white text-xs font-semibold rounded-lg">
          Return to Catalog
        </a>
      </div>

      <!-- Problem Content -->
      <article *ngIf="problem && !loading" class="bg-white rounded-xl border border-slate-200 shadow-xs overflow-hidden">
        <!-- Header Banner -->
        <div class="p-6 sm:p-8 border-b border-slate-100">
          <div class="flex flex-wrap items-center justify-between gap-3 mb-4">
            <div class="flex flex-wrap items-center gap-2">
              <span class="px-3 py-1 text-xs font-semibold rounded-full bg-slate-100 text-slate-700">
                {{ problem.domain }}
              </span>
              <span [ngClass]="getDifficultyBadgeClass(problem.difficulty)" class="px-3 py-1 text-xs font-medium rounded-full">
                {{ problem.difficulty }}
              </span>
              <span class="px-3 py-1 text-xs font-medium rounded-full bg-indigo-50 text-indigo-700 border border-indigo-100">
                {{ formatProjectType(problem.projectType) }}
              </span>
            </div>

            <!-- Bookmark Shortlist Button -->
            <button
              (click)="toggleBookmark()"
              [ngClass]="problem.bookmarked ? 'bg-indigo-50 text-indigo-700 border-indigo-200' : 'bg-white text-slate-700 border-slate-200 hover:bg-slate-50'"
              class="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-lg border text-xs font-semibold transition-all">
              <svg class="w-4 h-4" [attr.fill]="problem.bookmarked ? 'currentColor' : 'none'" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 5a2 2 0 012-2h10a2 2 0 012 2v16l-7-3.5L5 21V5z" />
              </svg>
              {{ problem.bookmarked ? 'Shortlisted' : 'Shortlist Problem' }}
            </button>
          </div>

          <h1 class="text-2xl sm:text-3xl font-extrabold text-slate-900 leading-tight">
            {{ problem.title }}
          </h1>

          <div class="mt-4 flex flex-wrap items-center gap-4 text-xs text-slate-500">
            <span *ngIf="problem.createdByName">Curated by <strong class="text-slate-700">{{ problem.createdByName }}</strong></span>
            <span>•</span>
            <span>Added {{ problem.createdAt | date:'mediumDate' }}</span>
          </div>
        </div>

        <!-- Body Details -->
        <div class="p-6 sm:p-8 space-y-8">
          <!-- 1. The Core Problem -->
          <section>
            <h2 class="text-xs font-bold uppercase tracking-wider text-slate-400 mb-2">Problem Statement</h2>
            <p class="text-slate-800 text-base leading-relaxed whitespace-pre-line font-medium">
              {{ problem.description }}
            </p>
          </section>

          <!-- 2. Impact & Why it matters -->
          <section *ngIf="problem.impact" class="p-5 bg-amber-50/50 rounded-lg border border-amber-200/60">
            <h2 class="text-xs font-bold uppercase tracking-wider text-amber-900 mb-2 flex items-center gap-2">
              <svg class="w-4 h-4 text-amber-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"/></svg>
              Real-World Impact & Why It Matters
            </h2>
            <p class="text-sm text-amber-950 leading-relaxed">
              {{ problem.impact }}
            </p>
          </section>

          <!-- 3. Possible Solution Direction -->
          <section *ngIf="problem.solutionDirection" class="p-5 bg-indigo-50/50 rounded-lg border border-indigo-100">
            <h2 class="text-xs font-bold uppercase tracking-wider text-indigo-900 mb-2 flex items-center gap-2">
              <svg class="w-4 h-4 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z"/></svg>
              Architectural & Solution Direction
            </h2>
            <p class="text-sm text-indigo-950 leading-relaxed">
              {{ problem.solutionDirection }}
            </p>
          </section>

          <!-- 4. Expected Deliverable / Outcome -->
          <section *ngIf="problem.expectedOutcome">
            <h2 class="text-xs font-bold uppercase tracking-wider text-slate-400 mb-2">Expected Project Deliverable</h2>
            <p class="text-sm text-slate-700 leading-relaxed bg-slate-50 p-4 rounded-lg border border-slate-200">
              {{ problem.expectedOutcome }}
            </p>
          </section>

          <!-- 5. Technologies Stack -->
          <section>
            <h2 class="text-xs font-bold uppercase tracking-wider text-slate-400 mb-3">Recommended Technologies</h2>
            <div class="flex flex-wrap gap-2">
              <span *ngFor="let tech of problem.technologies" class="px-3 py-1 rounded-md bg-slate-100 text-slate-800 text-xs font-mono font-medium">
                {{ tech.name }}
              </span>
              <span *ngIf="!problem.technologies || problem.technologies.length === 0" class="text-xs text-slate-400 italic">
                Any modern full-stack or mobile architecture
              </span>
            </div>
          </section>

          <!-- 6. Tags -->
          <section *ngIf="problem.tags && problem.tags.length > 0">
            <h2 class="text-xs font-bold uppercase tracking-wider text-slate-400 mb-3">Associated Categories & Tags</h2>
            <div class="flex flex-wrap gap-2">
              <span *ngFor="let tag of problem.tags" class="px-2.5 py-1 rounded-full bg-slate-50 border border-slate-200 text-slate-600 text-xs">
                #{{ tag.name }}
              </span>
            </div>
          </section>

          <!-- Bottom Action Callout -->
          <div class="mt-10 p-6 bg-slate-900 rounded-xl text-white flex flex-col sm:flex-row items-center justify-between gap-4">
            <div>
              <h3 class="font-bold text-base">Ready to build this project?</h3>
              <p class="text-xs text-slate-400 mt-1">Shortlist this problem to your personal review list or share it with your team.</p>
            </div>
            <button
              (click)="toggleBookmark()"
              class="px-5 py-2.5 bg-indigo-600 hover:bg-indigo-500 text-white rounded-lg text-xs font-bold tracking-wide transition-colors whitespace-nowrap">
              {{ problem.bookmarked ? 'Saved to Shortlist' : 'Add to Shortlist' }}
            </button>
          </div>
        </div>
      </article>
    </div>
  `
})
export class ProblemDetailsComponent implements OnInit {
  private readonly problemService = inject(ProblemService);
  private readonly bookmarkService = inject(BookmarkService);
  readonly authService = inject(AuthService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  problem: Problem | null = null;
  loading = true;
  error: string | null = null;

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      const id = Number(idParam);
      this.loadProblem(id);
    } else {
      this.error = 'Invalid problem statement ID.';
      this.loading = false;
    }
  }

  loadProblem(id: number): void {
    this.loading = true;
    this.error = null;

    this.problemService.getProblemById(id).subscribe({
      next: (data) => {
        this.problem = data;
        this.loading = false;
        this.checkBookmarkStatus(id);
      },
      error: (err) => {
        this.error = err.error?.message || 'Problem statement not found.';
        this.loading = false;
      }
    });
  }

  private checkBookmarkStatus(problemId: number): void {
    if (this.authService.isLoggedIn()) {
      this.bookmarkService.getUserBookmarks().subscribe({
        next: (bookmarks) => {
          if (this.problem) {
            this.problem.bookmarked = bookmarks.some(b => b.id === problemId);
          }
        }
      });
    }
  }

  toggleBookmark(): void {
    if (!this.problem) return;

    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/auth/login'], {
        queryParams: { returnUrl: `/problems/${this.problem.id}` }
      });
      return;
    }

    if (this.problem.bookmarked) {
      this.bookmarkService.removeBookmark(this.problem.id).subscribe({
        next: () => {
          if (this.problem) this.problem.bookmarked = false;
        }
      });
    } else {
      this.bookmarkService.addBookmark(this.problem.id).subscribe({
        next: () => {
          if (this.problem) this.problem.bookmarked = true;
        }
      });
    }
  }

  getDifficultyBadgeClass(difficulty: string): string {
    switch (difficulty?.toUpperCase()) {
      case 'BEGINNER':
        return 'bg-emerald-50 text-emerald-700 border border-emerald-200';
      case 'INTERMEDIATE':
        return 'bg-amber-50 text-amber-700 border border-amber-200';
      case 'ADVANCED':
        return 'bg-rose-50 text-rose-700 border border-rose-200';
      default:
        return 'bg-slate-100 text-slate-700';
    }
  }

  formatProjectType(type: string): string {
    if (!type) return '';
    return type.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, l => l.toUpperCase());
  }
}
