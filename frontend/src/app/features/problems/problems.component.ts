import { Component } from '@angular/core';

@Component({
  selector: 'app-problems',
  standalone: true,
  template: `
    <div class="max-w-6xl mx-auto px-4 py-8">
      <div class="flex items-center justify-between pb-6 border-b border-slate-200">
        <div>
          <h1 class="text-2xl font-bold text-slate-900">Explore Problem Statements</h1>
          <p class="text-sm text-slate-500 mt-1">Discover, filter, and shortlist curated real-world problems.</p>
        </div>
      </div>
      <div class="mt-8 text-center py-16 bg-white border border-dashed border-slate-300 rounded-xl">
        <p class="text-slate-500">Problem catalog will connect to the backend API in Phase 5 & 6.</p>
      </div>
    </div>
  `
})
export class ProblemsComponent {}
