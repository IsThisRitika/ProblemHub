import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ProblemService } from './problem.service';
import { environment } from '../../../environments/environment';

describe('ProblemService', () => {
  let service: ProblemService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ProblemService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(ProblemService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch problems with query parameters', () => {
    service.getProblems({ keyword: 'AI', domain: 'Healthcare', page: 0, size: 10 }).subscribe(response => {
      expect(response.content.length).toBe(1);
      expect(response.content[0].title).toBe('AI Diagnostics');
    });

    const req = httpMock.expectOne((request) => {
      return request.url === `${environment.apiUrl}/problems` &&
        request.params.get('keyword') === 'AI' &&
        request.params.get('domain') === 'Healthcare' &&
        request.params.get('page') === '0';
    });

    expect(req.request.method).toBe('GET');
    req.flush({
      content: [{
        id: 1,
        title: 'AI Diagnostics',
        description: 'Testing AI',
        domain: 'Healthcare',
        difficulty: 'BEGINNER',
        projectType: 'MINI_PROJECT',
        status: 'PUBLISHED',
        createdAt: '2026-01-01T00:00:00',
        technologies: [],
        tags: []
      }],
      pageNumber: 0,
      pageSize: 10,
      totalElements: 1,
      totalPages: 1,
      first: true,
      last: true
    });
  });

  it('should fetch problem by ID', () => {
    service.getProblemById(42).subscribe(problem => {
      expect(problem.id).toBe(42);
      expect(problem.title).toBe('Sample Title');
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/problems/42`);
    expect(req.request.method).toBe('GET');
    req.flush({
      id: 42,
      title: 'Sample Title',
      description: 'Sample Description',
      domain: 'FinTech',
      difficulty: 'INTERMEDIATE',
      projectType: 'MAJOR_PROJECT',
      status: 'PUBLISHED',
      createdAt: '2026-01-01T00:00:00',
      technologies: [],
      tags: []
    });
  });
});
