import { Component } from '@angular/core';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  template: `
    <div class="max-w-6xl mx-auto px-4 py-8">
      <h1 class="text-2xl font-bold text-slate-900">Admin Control Panel</h1>
      <p class="text-sm text-slate-500 mt-1">Manage problem statements, technologies, and tags.</p>
      <div class="mt-8 text-center py-16 bg-white border border-dashed border-slate-300 rounded-xl">
        <p class="text-slate-500">Admin management console will be activated in Phase 9.</p>
      </div>
    </div>
  `
})
export class AdminDashboardComponent {}
