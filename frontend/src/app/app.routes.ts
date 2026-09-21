import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { ProblemsComponent } from './features/problems/problems.component';
import { ProblemDetailsComponent } from './features/problem-details/problem-details.component';
import { BookmarksComponent } from './features/bookmarks/bookmarks.component';
import { LoginComponent } from './features/auth/login.component';
import { RegisterComponent } from './features/auth/register.component';
import { AdminDashboardComponent } from './features/admin/admin-dashboard.component';

export const routes: Routes = [
  { path: '', component: HomeComponent, pathMatch: 'full' },
  { path: 'problems', component: ProblemsComponent },
  { path: 'problems/:id', component: ProblemDetailsComponent },
  { path: 'bookmarks', component: BookmarksComponent },
  { path: 'auth/login', component: LoginComponent },
  { path: 'auth/register', component: RegisterComponent },
  { path: 'admin', component: AdminDashboardComponent },
  { path: '**', redirectTo: '' }
];
