import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Problem } from '../models/problem.model';

@Component({
  selector: 'app-problem-card',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="bg-white rounded-xl border border-slate-200 hover:border-indigo-200 hover:shadow-md transition-all duration-200 p-6 flex flex-col justify-between group">
      <div>
        <!-- Top metadata badges -->
        <div class="flex flex-wrap items-center justify-between gap-2 mb-3">
          <div class="flex flex-wrap items-center gap-1.5">
            <span class="px-2.5 py-0.5 text-xs font-semibold rounded-full bg-slate-100 text-slate-700">
              {{ problem.domain }}
            </span>
            <span [ngClass]="getDifficultyBadgeClass(problem.difficulty)" class="px-2.5 py-0.5 text-xs font-medium rounded-full">
              {{ problem.difficulty }}
            </span>
            <span class="px-2.5 py-0.5 text-xs font-medium rounded-full bg-indigo-50 text-indigo-700 border border-indigo-100">
              {{ formatProjectType(problem.projectType) }}
            </span>
          </div>

          <!-- Bookmark Toggle Button -->
          <button 
            type="button" 
            (click)="onBookmarkClick($event)" 
            [attr.aria-label]="problem.bookmarked ? 'Remove from shortlist' : 'Save to shortlist'"
            class="p-1.5 rounded-lg text-slate-400 hover:text-indigo-600 hover:bg-slate-50 transition-colors"
            [ngClass]="{ 'text-indigo-600 bg-indigo-50': problem.bookmarked }">
            <svg class="w-5 h-5" [attr.fill]="problem.bookmarked ? 'currentColor' : 'none'" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 5a2 2 0 012-2h10a2 2 0 012 2v16l-7-3.5L5 21V5z" />
            </svg>
          </button>
        </div>

        <!-- Problem Title -->
        <h3 class="text-lg font-bold text-slate-900 group-hover:text-indigo-600 transition-colors">
          <a [routerLink]="['/problems', problem.id]">
            {{ problem.title }}
          </a>
        </h3>

        <!-- Problem Description -->
        <p class="text-sm text-slate-600 mt-2 line-clamp-3 leading-relaxed">
          {{ problem.description }}
        </p>

        <!-- Tech Stack Pills -->
        <div *ngIf="problem.technologies && problem.technologies.length > 0" class="mt-4 flex flex-wrap gap-1.5 items-center">
          <span *ngFor="let tech of problem.technologies.slice(0, 4)" class="text-xs px-2 py-0.5 rounded bg-slate-100 text-slate-600 font-mono">
            {{ tech.name }}
          </span>
          <span *ngIf="problem.technologies.length > 4" class="text-xs text-slate-400 font-mono">
            +{{ problem.technologies.length - 4 }} more
          </span>
        </div>
      </div>

      <!-- Bottom action & tags -->
      <div class="mt-6 pt-4 border-t border-slate-100 flex items-center justify-between text-xs text-slate-500">
        <div class="flex items-center gap-2">
          <span *ngFor="let tag of problem.tags.slice(0, 2)" class="text-slate-400">
            #{{ tag.name }}
          </span>
        </div>
        <a [routerLink]="['/problems', problem.id]" class="inline-flex items-center gap-1 font-semibold text-indigo-600 hover:text-indigo-700">
          Details
          <svg class="w-3.5 h-3.5 group-hover:translate-x-0.5 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
          </svg>
        </a>
      </div>
    </div>
  `
})
export class ProblemCardComponent {
  @Input({ required: true }) problem!: Problem;
  @Output() bookmarkToggle = new EventEmitter<Problem>();

  onBookmarkClick(event: Event) {
    event.stopPropagation();
    this.bookmarkToggle.emit(this.problem);
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
