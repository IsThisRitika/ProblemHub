import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterLink],
  template: `
    <div class="max-w-md mx-auto px-4 py-16">
      <div class="bg-white p-8 rounded-xl border border-slate-200 shadow-xs">
        <h2 class="text-2xl font-bold text-slate-900 mb-6">Create Account</h2>
        <p class="text-sm text-slate-500 mb-4">Registration flow will be wired in Phase 7.</p>
        <p class="text-xs text-slate-400">Already registered? <a routerLink="/auth/login" class="text-indigo-600 underline">Sign In</a></p>
      </div>
    </div>
  `
})
export class RegisterComponent {}
