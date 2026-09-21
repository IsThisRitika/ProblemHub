import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  template: `
    <div class="max-w-md mx-auto px-4 py-16">
      <div class="bg-white p-8 rounded-2xl border border-slate-200 shadow-sm">
        <div class="text-center mb-8">
          <div class="w-12 h-12 mx-auto rounded-xl bg-indigo-600 text-white flex items-center justify-center font-bold text-xl mb-3 shadow-xs">
            PH
          </div>
          <h2 class="text-2xl font-extrabold text-slate-900 tracking-tight">Sign in to Problem Hub</h2>
          <p class="text-xs text-slate-500 mt-1">Shortlist problems and build real-world software.</p>
        </div>

        <!-- Quick Demo Credentials Helpers -->
        <div class="mb-6 p-3 bg-slate-50 rounded-lg border border-slate-200/80 text-xs space-y-2">
          <span class="font-semibold text-slate-600 block">Quick Demo Logins:</span>
          <div class="flex gap-2">
            <button
              type="button"
              (click)="fillDemo('student@problemhub.com', 'student123')"
              class="flex-1 py-1.5 px-2 bg-white hover:bg-slate-100 border border-slate-200 rounded text-indigo-600 font-medium transition-colors">
              Student Account
            </button>
            <button
              type="button"
              (click)="fillDemo('admin@problemhub.com', 'admin123')"
              class="flex-1 py-1.5 px-2 bg-white hover:bg-slate-100 border border-slate-200 rounded text-slate-700 font-medium transition-colors">
              Admin Account
            </button>
          </div>
        </div>

        <!-- Error Message Alert -->
        <div *ngIf="error" class="mb-5 p-3 rounded-lg bg-rose-50 border border-rose-200 text-xs text-rose-700 font-medium">
          {{ error }}
        </div>

        <!-- Login Form -->
        <form (ngSubmit)="onSubmit()" class="space-y-4">
          <div>
            <label class="block text-xs font-semibold text-slate-700 mb-1">Email Address</label>
            <input
              type="email"
              [(ngModel)]="email"
              name="email"
              required
              placeholder="you@university.edu"
              class="w-full px-3.5 py-2 text-sm bg-slate-50 border border-slate-200 rounded-lg focus:bg-white focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
          </div>

          <div>
            <label class="block text-xs font-semibold text-slate-700 mb-1">Password</label>
            <input
              type="password"
              [(ngModel)]="password"
              name="password"
              required
              placeholder="••••••••"
              class="w-full px-3.5 py-2 text-sm bg-slate-50 border border-slate-200 rounded-lg focus:bg-white focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
          </div>

          <button
            type="submit"
            [disabled]="loading || !email || !password"
            class="w-full py-2.5 px-4 bg-indigo-600 hover:bg-indigo-700 text-white font-medium rounded-lg text-sm shadow-xs transition-colors disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2">
            <span *ngIf="loading" class="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin"></span>
            {{ loading ? 'Signing in...' : 'Sign In' }}
          </button>
        </form>

        <div class="mt-6 text-center text-xs text-slate-500">
          New to Problem Hub?
          <a routerLink="/auth/register" class="text-indigo-600 hover:text-indigo-700 font-semibold ml-1">
            Create an account
          </a>
        </div>
      </div>
    </div>
  `
})
export class LoginComponent {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  email = '';
  password = '';
  loading = false;
  error: string | null = null;

  fillDemo(email: string, pass: string): void {
    this.email = email;
    this.password = pass;
    this.error = null;
  }

  onSubmit(): void {
    if (!this.email || !this.password) return;

    this.loading = true;
    this.error = null;

    this.authService.login({ email: this.email, password: this.password }).subscribe({
      next: () => {
        this.loading = false;
        const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/problems';
        this.router.navigateByUrl(returnUrl);
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.message || 'Invalid email or password. Please try again.';
      }
    });
  }
}
