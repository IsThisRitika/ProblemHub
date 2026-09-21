import { Component } from '@angular/core';

@Component({
  selector: 'app-problem-details',
  standalone: true,
  template: `
    <div class="max-w-4xl mx-auto px-4 py-8">
      <div class="bg-white p-6 rounded-xl border border-slate-200">
        <h1 class="text-2xl font-bold text-slate-900">Problem Details</h1>
        <p class="text-sm text-slate-500 mt-2">Connecting to GET /api/problems/:id in Phase 6.</p>
      </div>
    </div>
  `
})
export class ProblemDetailsComponent {}
