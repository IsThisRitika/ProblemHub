import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProblemService } from '../../core/services/problem.service';
import { BookmarkService } from '../../core/services/bookmark.service';
import { AuthService } from '../../core/services/auth.service';
import { Problem, ProblemFilterParams, Tag, Technology } from '../../shared/models/problem.model';
import { ProblemCardComponent } from '../../shared/components/problem-card.component';
import { debounceTime, distinctUntilChanged, Subject } from 'rxjs';

@Component({
  selector: 'app-problems',
  standalone: true,
  imports: [CommonModule, FormsModule, ProblemCardComponent],
  template: `
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Page Header -->
      <div class="flex flex-col md:flex-row md:items-center justify-between gap-4 pb-6 border-b border-slate-200">
        <div>
          <h1 class="text-3xl font-extrabold text-slate-900 tracking-tight">Discover Problem Statements</h1>
          <p class="text-sm text-slate-500 mt-1">
            Browse and filter through vetted real-world project statements ready to build.
          </p>
        </div>
        <div class="flex items-center gap-3">
          <!-- Active filter count badge -->
          <span *ngIf="hasActiveFilters()" class="text-xs px-2.5 py-1 rounded-full bg-indigo-50 text-indigo-700 font-semibold">
            Filters Active
          </span>
          <button 
            *ngIf="hasActiveFilters()" 
            (click)="resetFilters()" 
            class="text-xs text-slate-500 hover:text-indigo-600 underline font-medium">
            Reset Filters
          </button>
        </div>
      </div>

      <!-- Search and Filter Controls -->
      <div class="mt-6 bg-white p-5 rounded-xl border border-slate-200 shadow-xs space-y-4">
        <!-- Top Search Bar & Sort -->
        <div class="flex flex-col md:flex-row gap-3">
          <div class="relative flex-1">
            <span class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/></svg>
            </span>
            <input
              type="text"
              [(ngModel)]="searchKeyword"
              (ngModelChange)="onSearchInput($event)"
              placeholder="Search problems by keyword (e.g. insulin, carbon, solar, security)..."
              class="w-full pl-10 pr-4 py-2 text-sm bg-slate-50 border border-slate-200 rounded-lg focus:bg-white focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
            />
          </div>

          <!-- Sort Selector -->
          <div class="flex items-center gap-2">
            <label class="text-xs font-medium text-slate-500 whitespace-nowrap">Sort by:</label>
            <select
              [(ngModel)]="selectedSort"
              (change)="onSortChange()"
              class="text-xs bg-slate-50 border border-slate-200 rounded-lg py-2 px-3 focus:outline-none focus:ring-2 focus:ring-indigo-500">
              <option value="createdAt-DESC">Newest First</option>
              <option value="createdAt-ASC">Oldest First</option>
              <option value="title-ASC">Title (A - Z)</option>
              <option value="title-DESC">Title (Z - A)</option>
              <option value="difficulty-ASC">Difficulty</option>
            </select>
          </div>
        </div>

        <!-- Filter Dropdown Pills -->
        <div class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-5 gap-3 pt-2 border-t border-slate-100">
          <!-- Domain Filter -->
          <div>
            <label class="block text-xs font-semibold text-slate-600 mb-1">Domain</label>
            <select
              [(ngModel)]="selectedDomain"
              (change)="applyFilters()"
              class="w-full text-xs bg-slate-50 border border-slate-200 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-indigo-500">
              <option value="">All Domains</option>
              <option *ngFor="let d of domains" [value]="d">{{ d }}</option>
            </select>
          </div>

          <!-- Difficulty Filter -->
          <div>
            <label class="block text-xs font-semibold text-slate-600 mb-1">Difficulty</label>
            <select
              [(ngModel)]="selectedDifficulty"
              (change)="applyFilters()"
              class="w-full text-xs bg-slate-50 border border-slate-200 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-indigo-500">
              <option value="">All Difficulties</option>
              <option value="BEGINNER">Beginner</option>
              <option value="INTERMEDIATE">Intermediate</option>
              <option value="ADVANCED">Advanced</option>
            </select>
          </div>

          <!-- Project Type Filter -->
          <div>
            <label class="block text-xs font-semibold text-slate-600 mb-1">Project Type</label>
            <select
              [(ngModel)]="selectedProjectType"
              (change)="applyFilters()"
              class="w-full text-xs bg-slate-50 border border-slate-200 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-indigo-500">
              <option value="">All Project Types</option>
              <option value="HACKATHON">Hackathon</option>
              <option value="MINI_PROJECT">Mini Project</option>
              <option value="MAJOR_PROJECT">Major Project</option>
              <option value="FINAL_YEAR_PROJECT">Final Year Capstone</option>
            </select>
          </div>

          <!-- Technology Filter -->
          <div>
            <label class="block text-xs font-semibold text-slate-600 mb-1">Technology</label>
            <select
              [(ngModel)]="selectedTechnology"
              (change)="applyFilters()"
              class="w-full text-xs bg-slate-50 border border-slate-200 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-indigo-500">
              <option value="">All Technologies</option>
              <option *ngFor="let tech of technologies" [value]="tech.name">{{ tech.name }}</option>
            </select>
          </div>

          <!-- Tag Filter -->
          <div>
            <label class="block text-xs font-semibold text-slate-600 mb-1">Tag</label>
            <select
              [(ngModel)]="selectedTag"
              (change)="applyFilters()"
              class="w-full text-xs bg-slate-50 border border-slate-200 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-indigo-500">
              <option value="">All Tags</option>
              <option *ngFor="let tag of tags" [value]="tag.name">{{ tag.name }}</option>
            </select>
          </div>
        </div>
      </div>

      <!-- Results Stats Banner -->
      <div class="mt-6 flex items-center justify-between text-xs text-slate-500 font-medium">
        <span>
          Found <strong class="text-slate-800">{{ totalElements }}</strong> problems
          <span *ngIf="totalPages > 0"> (Page {{ currentPage + 1 }} of {{ totalPages }})</span>
        </span>
      </div>

      <!-- Loading State Skeleton -->
      <div *ngIf="loading" class="mt-6 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div *ngFor="let n of [1,2,3,4,5,6]" class="bg-white rounded-xl border border-slate-200 p-6 animate-pulse space-y-4">
          <div class="h-4 bg-slate-200 rounded w-1/3"></div>
          <div class="h-6 bg-slate-200 rounded w-3/4"></div>
          <div class="h-16 bg-slate-200 rounded w-full"></div>
          <div class="h-4 bg-slate-200 rounded w-1/2"></div>
        </div>
      </div>

      <!-- Error State -->
      <div *ngIf="error && !loading" class="mt-8 text-center p-12 bg-rose-50 border border-rose-200 rounded-xl">
        <div class="w-12 h-12 mx-auto text-rose-500 mb-3">
          <svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/></svg>
        </div>
        <h3 class="text-base font-bold text-rose-900">Failed to load problems</h3>
        <p class="text-xs text-rose-600 mt-1 max-w-md mx-auto">{{ error }}</p>
        <button (click)="loadProblems()" class="mt-4 px-4 py-2 bg-rose-600 hover:bg-rose-700 text-white rounded-lg text-xs font-semibold">
          Retry
        </button>
      </div>

      <!-- Empty State -->
      <div *ngIf="!loading && !error && problems.length === 0" class="mt-8 text-center py-16 bg-white border border-dashed border-slate-300 rounded-xl">
        <div class="w-12 h-12 mx-auto text-slate-400 mb-3">
          <svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>
        </div>
        <h3 class="text-base font-bold text-slate-800">No matching problem statements found</h3>
        <p class="text-xs text-slate-500 mt-1 max-w-sm mx-auto">
          Try broadening your search query or removing some of your active filter constraints.
        </p>
        <button (click)="resetFilters()" class="mt-4 px-4 py-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-lg text-xs font-semibold">
          Clear All Filters
        </button>
      </div>

      <!-- Problem Cards Grid -->
      <div *ngIf="!loading && !error && problems.length > 0" class="mt-6 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <app-problem-card
          *ngFor="let problem of problems"
          [problem]="problem"
          (bookmarkToggle)="toggleBookmark($event)">
        </app-problem-card>
      </div>

      <!-- Pagination Footer -->
      <div *ngIf="!loading && totalPages > 1" class="mt-10 flex items-center justify-center gap-2">
        <button
          (click)="goToPage(currentPage - 1)"
          [disabled]="currentPage === 0"
          class="px-3.5 py-2 text-xs font-semibold rounded-lg border border-slate-200 bg-white text-slate-700 hover:bg-slate-50 disabled:opacity-40 disabled:cursor-not-allowed">
          Previous
        </button>

        <span class="text-xs font-medium text-slate-600 px-3">
          Page {{ currentPage + 1 }} of {{ totalPages }}
        </span>

        <button
          (click)="goToPage(currentPage + 1)"
          [disabled]="currentPage >= totalPages - 1"
          class="px-3.5 py-2 text-xs font-semibold rounded-lg border border-slate-200 bg-white text-slate-700 hover:bg-slate-50 disabled:opacity-40 disabled:cursor-not-allowed">
          Next
        </button>
      </div>
    </div>
  `
})
export class ProblemsComponent implements OnInit {
  private readonly problemService = inject(ProblemService);
  private readonly bookmarkService = inject(BookmarkService);
  readonly authService = inject(AuthService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  problems: Problem[] = [];
  technologies: Technology[] = [];
  tags: Tag[] = [];
  bookmarkedIds = new Set<number>();

  domains = [
    'Healthcare',
    'FinTech',
    'Civic Tech',
    'EdTech',
    'CleanTech',
    'Cybersecurity',
    'Agriculture',
    'Logistics',
    'Sustainability',
    'Cloud Native',
    'Open Source'
  ];

  searchKeyword = '';
  selectedDomain = '';
  selectedDifficulty = '';
  selectedProjectType = '';
  selectedTechnology = '';
  selectedTag = '';
  selectedSort = 'createdAt-DESC';

  currentPage = 0;
  pageSize = 9;
  totalElements = 0;
  totalPages = 0;

  loading = true;
  error: string | null = null;

  private searchSubject = new Subject<string>();

  ngOnInit(): void {
    // Setup debounced search input
    this.searchSubject.pipe(
      debounceTime(350),
      distinctUntilChanged()
    ).subscribe(() => {
      this.currentPage = 0;
      this.loadProblems();
    });

    // Check query params if any
    this.route.queryParams.subscribe(params => {
      if (params['keyword']) this.searchKeyword = params['keyword'];
      if (params['domain']) this.selectedDomain = params['domain'];
      if (params['difficulty']) this.selectedDifficulty = params['difficulty'];
      if (params['projectType']) this.selectedProjectType = params['projectType'];
    });

    this.loadFilterMetadata();
    this.loadUserBookmarksAndProblems();
  }

  onSearchInput(value: string): void {
    this.searchSubject.next(value);
  }

  onSortChange(): void {
    this.currentPage = 0;
    this.loadProblems();
  }

  applyFilters(): void {
    this.currentPage = 0;
    this.loadProblems();
  }

  hasActiveFilters(): boolean {
    return !!(this.searchKeyword || this.selectedDomain || this.selectedDifficulty ||
              this.selectedProjectType || this.selectedTechnology || this.selectedTag);
  }

  resetFilters(): void {
    this.searchKeyword = '';
    this.selectedDomain = '';
    this.selectedDifficulty = '';
    this.selectedProjectType = '';
    this.selectedTechnology = '';
    this.selectedTag = '';
    this.selectedSort = 'createdAt-DESC';
    this.currentPage = 0;
    this.loadProblems();
  }

  loadFilterMetadata(): void {
    this.problemService.getTechnologies().subscribe({
      next: (techs) => this.technologies = techs,
      error: () => {}
    });

    this.problemService.getTags().subscribe({
      next: (tags) => this.tags = tags,
      error: () => {}
    });
  }

  private loadUserBookmarksAndProblems(): void {
    if (this.authService.isLoggedIn()) {
      this.bookmarkService.getUserBookmarks().subscribe({
        next: (bookmarks) => {
          this.bookmarkedIds = new Set(bookmarks.map(b => b.id));
          this.loadProblems();
        },
        error: () => this.loadProblems()
      });
    } else {
      this.loadProblems();
    }
  }

  loadProblems(): void {
    this.loading = true;
    this.error = null;

    const [sortBy, sortDir] = this.selectedSort.split('-');

    const filterParams: ProblemFilterParams = {
      keyword: this.searchKeyword || undefined,
      domain: this.selectedDomain || undefined,
      difficulty: this.selectedDifficulty || undefined,
      projectType: this.selectedProjectType || undefined,
      technology: this.selectedTechnology || undefined,
      tag: this.selectedTag || undefined,
      page: this.currentPage,
      size: this.pageSize,
      sortBy: sortBy,
      sortDir: (sortDir as 'ASC' | 'DESC') || 'DESC'
    };

    this.problemService.getProblems(filterParams).subscribe({
      next: (res) => {
        this.problems = res.content.map(p => ({
          ...p,
          bookmarked: this.bookmarkedIds.has(p.id)
        }));
        this.totalElements = res.totalElements;
        this.totalPages = res.totalPages;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.message || 'Unable to connect to the Problem Hub API server.';
        this.loading = false;
      }
    });
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadProblems();
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }

  toggleBookmark(problem: Problem): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/auth/login'], { queryParams: { returnUrl: '/problems' } });
      return;
    }

    if (problem.bookmarked) {
      this.bookmarkService.removeBookmark(problem.id).subscribe({
        next: () => {
          problem.bookmarked = false;
          this.bookmarkedIds.delete(problem.id);
        }
      });
    } else {
      this.bookmarkService.addBookmark(problem.id).subscribe({
        next: () => {
          problem.bookmarked = true;
          this.bookmarkedIds.add(problem.id);
        }
      });
    }
  }
}
