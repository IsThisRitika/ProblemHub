import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink],
  template: `
    <div class="max-w-6xl mx-auto px-4 py-12">
      <!-- Hero Section -->
      <div class="text-center max-w-3xl mx-auto space-y-6">
        <div class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-indigo-50 border border-indigo-200 text-indigo-700 text-xs font-semibold tracking-wide uppercase">
          Curated Real-World Problems
        </div>
        <h1 class="text-4xl sm:text-5xl font-extrabold text-slate-900 tracking-tight leading-tight">
          Find Your Next <span class="text-indigo-600">Breakthrough Project</span> Idea
        </h1>
        <p class="text-lg text-slate-600 leading-relaxed">
          Discover vetted, high-impact problem statements for hackathons, capstone projects, and portfolios. Filter by domain, difficulty, tech stack, and project scope.
        </p>
        
        <div class="flex flex-wrap items-center justify-center gap-4 pt-2">
          <a routerLink="/problems" class="px-6 py-3 bg-indigo-600 hover:bg-indigo-700 text-white font-medium rounded-lg shadow-sm transition-colors duration-150 inline-flex items-center gap-2">
            Explore Problems
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14 5l7 7m0 0l-7 7m7-7H3"/></svg>
          </a>
          <a routerLink="/auth/register" class="px-6 py-3 bg-white hover:bg-slate-50 text-slate-700 font-medium rounded-lg border border-slate-300 shadow-sm transition-colors duration-150">
            Create Free Account
          </a>
        </div>
      </div>

      <!-- Core Principle: Discover -> Understand -> Filter -> Shortlist -> Build -->
      <div class="mt-20 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-4 text-center">
        <div class="p-5 bg-white rounded-xl border border-slate-200 shadow-xs">
          <div class="w-10 h-10 mx-auto rounded-lg bg-indigo-50 text-indigo-600 flex items-center justify-center font-bold mb-3">1</div>
          <h3 class="font-semibold text-slate-800">Discover</h3>
          <p class="text-xs text-slate-500 mt-1">Explore real industry and societal pain points.</p>
        </div>
        <div class="p-5 bg-white rounded-xl border border-slate-200 shadow-xs">
          <div class="w-10 h-10 mx-auto rounded-lg bg-blue-50 text-blue-600 flex items-center justify-center font-bold mb-3">2</div>
          <h3 class="font-semibold text-slate-800">Understand</h3>
          <p class="text-xs text-slate-500 mt-1">Deep-dive into root causes and user impact.</p>
        </div>
        <div class="p-5 bg-white rounded-xl border border-slate-200 shadow-xs">
          <div class="w-10 h-10 mx-auto rounded-lg bg-amber-50 text-amber-600 flex items-center justify-center font-bold mb-3">3</div>
          <h3 class="font-semibold text-slate-800">Filter</h3>
          <p class="text-xs text-slate-500 mt-1">Target your preferred domain, stack, and scope.</p>
        </div>
        <div class="p-5 bg-white rounded-xl border border-slate-200 shadow-xs">
          <div class="w-10 h-10 mx-auto rounded-lg bg-emerald-50 text-emerald-600 flex items-center justify-center font-bold mb-3">4</div>
          <h3 class="font-semibold text-slate-800">Shortlist</h3>
          <p class="text-xs text-slate-500 mt-1">Bookmark and evaluate candidate problems.</p>
        </div>
        <div class="p-5 bg-white rounded-xl border border-slate-200 shadow-xs">
          <div class="w-10 h-10 mx-auto rounded-lg bg-purple-50 text-purple-600 flex items-center justify-center font-bold mb-3">5</div>
          <h3 class="font-semibold text-slate-800">Build</h3>
          <p class="text-xs text-slate-500 mt-1">Architect and ship meaningful software.</p>
        </div>
      </div>
    </div>
  `
})
export class HomeComponent {}
