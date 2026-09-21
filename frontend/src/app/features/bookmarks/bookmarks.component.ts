import { Component } from '@angular/core';

@Component({
  selector: 'app-bookmarks',
  standalone: true,
  template: `
    <div class="max-w-6xl mx-auto px-4 py-8">
      <h1 class="text-2xl font-bold text-slate-900">Saved Problems (Shortlist)</h1>
      <p class="text-sm text-slate-500 mt-1">Review and manage your shortlisted problems.</p>
      <div class="mt-8 text-center py-16 bg-white border border-dashed border-slate-300 rounded-xl">
        <p class="text-slate-500">Bookmarks will be available after authentication in Phase 8.</p>
      </div>
    </div>
  `
})
export class BookmarksComponent {}
