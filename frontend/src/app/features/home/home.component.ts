import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ProblemService } from '../../core/services/problem.service';
import { Problem } from '../../shared/models/problem.model';
import { ProblemCardComponent } from '../../shared/components/problem-card.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, ProblemCardComponent],
  template: `
    <div class="space-y-16 pb-20">
      <!-- 1. Hero Section -->
      <section class="max-w-5xl mx-auto px-4 pt-16 sm:pt-20 text-center space-y-6">
        <div class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-indigo-50 border border-indigo-200 text-indigo-700 text-xs font-semibold tracking-wide uppercase">
          Curated Real-World Problems
        </div>
        
        <h1 class="text-4xl sm:text-6xl font-extrabold text-slate-900 tracking-tight leading-tight">
          Discover Real Problems.<br />
          <span class="text-indigo-600">Build High-Impact Projects.</span>
        </h1>
        
        <p class="text-base sm:text-lg text-slate-600 max-w-2xl mx-auto leading-relaxed">
          The curated discovery hub for hackathon contenders, capstone researchers, and portfolio builders.
          Move from problem ambiguity to concrete software architecture.
        </p>

        <!-- Quick Search Bar -->
        <div class="max-w-2xl mx-auto pt-4">
          <form (submit)="onSearchSubmit()" class="flex items-center bg-white rounded-xl border-2 border-slate-200 shadow-sm focus-within:border-indigo-600 focus-within:ring-2 focus-within:ring-indigo-100 transition-all p-1.5">
            <div class="pl-3.5 text-slate-400">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/></svg>
            </div>
            <input
              type="text"
              [(ngModel)]="searchKeyword"
              name="search"
              placeholder="Search problems by keyword (e.g. insulin, carbon, solar, drone)..."
              class="w-full px-3 py-2 text-sm bg-transparent focus:outline-none text-slate-800 placeholder-slate-400"
            />
            <button
              type="submit"
              class="px-5 py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white rounded-lg text-xs font-bold transition-colors whitespace-nowrap shadow-xs">
              Search
            </button>
          </form>
        </div>

        <!-- 5-Step Workflow Principle -->
        <div class="pt-10 grid grid-cols-2 sm:grid-cols-5 gap-3 text-center max-w-4xl mx-auto text-xs">
          <div class="p-3 bg-white rounded-lg border border-slate-200 shadow-2xs">
            <span class="font-bold text-indigo-600">1. Discover</span>
            <p class="text-slate-400 text-[11px] mt-0.5">Industry gaps</p>
          </div>
          <div class="p-3 bg-white rounded-lg border border-slate-200 shadow-2xs">
            <span class="font-bold text-blue-600">2. Understand</span>
            <p class="text-slate-400 text-[11px] mt-0.5">Root pain points</p>
          </div>
          <div class="p-3 bg-white rounded-lg border border-slate-200 shadow-2xs">
            <span class="font-bold text-amber-600">3. Filter</span>
            <p class="text-slate-400 text-[11px] mt-0.5">By tech & scope</p>
          </div>
          <div class="p-3 bg-white rounded-lg border border-slate-200 shadow-2xs">
            <span class="font-bold text-emerald-600">4. Shortlist</span>
            <p class="text-slate-400 text-[11px] mt-0.5">Evaluate ideas</p>
          </div>
          <div class="p-3 bg-white rounded-lg border border-slate-200 shadow-2xs col-span-2 sm:col-span-1">
            <span class="font-bold text-purple-600">5. Build</span>
            <p class="text-slate-400 text-[11px] mt-0.5">Ship products</p>
          </div>
        </div>
      </section>

      <!-- 2. Popular Domains -->
      <section class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex items-center justify-between mb-6">
          <div>
            <h2 class="text-xl font-bold text-slate-900">Explore by Domain</h2>
            <p class="text-xs text-slate-500 mt-0.5">Browse challenges across specialized industries and fields.</p>
          </div>
          <a routerLink="/problems" class="text-xs font-semibold text-indigo-600 hover:text-indigo-700">
            View All →
          </a>
        </div>

        <div class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-3">
          <a
            *ngFor="let domain of popularDomains"
            [routerLink]="['/problems']"
            [queryParams]="{ domain: domain.name }"
            class="p-4 bg-white rounded-xl border border-slate-200 hover:border-indigo-300 hover:shadow-xs transition-all group text-left">
            <div class="text-2xl mb-2">{{ domain.icon }}</div>
            <h3 class="text-xs font-bold text-slate-800 group-hover:text-indigo-600">{{ domain.name }}</h3>
            <p class="text-[11px] text-slate-400 mt-0.5">{{ domain.count }}</p>
          </a>
        </div>
      </section>

      <!-- 3. Featured & Recently Added Problems -->
      <section class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex items-center justify-between mb-6">
          <div>
            <h2 class="text-xl font-bold text-slate-900">Featured Problem Statements</h2>
            <p class="text-xs text-slate-500 mt-0.5">Curated, high-impact statements verified and ready to build.</p>
          </div>
          <a routerLink="/problems" class="text-xs font-semibold text-indigo-600 hover:text-indigo-700">
            See all 20+ problems →
          </a>
        </div>

        <!-- Cards Grid -->
        <div *ngIf="loading" class="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div *ngFor="let n of [1,2,3]" class="bg-white rounded-xl border border-slate-200 p-6 animate-pulse space-y-4">
            <div class="h-4 bg-slate-200 rounded w-1/3"></div>
            <div class="h-6 bg-slate-200 rounded w-3/4"></div>
            <div class="h-16 bg-slate-200 rounded w-full"></div>
          </div>
        </div>

        <div *ngIf="!loading && featuredProblems.length > 0" class="grid grid-cols-1 md:grid-cols-3 gap-6">
          <app-problem-card
            *ngFor="let problem of featuredProblems"
            [problem]="problem"
            (bookmarkToggle)="toggleBookmark($event)">
          </app-problem-card>
        </div>
      </section>
    </div>
  `
})
export class HomeComponent implements OnInit {
  private readonly problemService = inject(ProblemService);
  private readonly router = inject(Router);
  private readonly cdr = inject(ChangeDetectorRef);

  searchKeyword = '';
  featuredProblems: Problem[] = [];
  loading = true;

  popularDomains = [
    { name: 'Healthcare', icon: '🩺', count: 'Real-time & Telemetry' },
    { name: 'FinTech', icon: '💳', count: 'Credit & Anomaly Models' },
    { name: 'Civic Tech', icon: '🏛️', count: 'Community & Public Services' },
    { name: 'CleanTech', icon: '⚡', count: 'Energy & Sustainability' },
    { name: 'Cybersecurity', icon: '🛡️', count: 'API & Supply Chain' },
    { name: 'EdTech', icon: '🎓', count: 'Learning & Accessibility' }
  ];

  ngOnInit(): void {
    this.problemService.getProblems({ size: 6, sortBy: 'createdAt', sortDir: 'DESC' }).subscribe({
      next: (res) => {
        this.featuredProblems = res.content.slice(0, 3);
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  onSearchSubmit(): void {
    if (this.searchKeyword.trim()) {
      this.router.navigate(['/problems'], { queryParams: { keyword: this.searchKeyword.trim() } });
    } else {
      this.router.navigate(['/problems']);
    }
  }

  toggleBookmark(problem: Problem): void {
    problem.bookmarked = !problem.bookmarked;
  }
}
