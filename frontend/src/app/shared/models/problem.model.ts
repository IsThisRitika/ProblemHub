export interface Technology {
  id: number;
  name: string;
}

export interface Tag {
  id: number;
  name: string;
}

export type DifficultyLevel = 'BEGINNER' | 'INTERMEDIATE' | 'ADVANCED';
export type ProjectType = 'HACKATHON' | 'MINI_PROJECT' | 'MAJOR_PROJECT' | 'FINAL_YEAR_PROJECT';
export type ProblemStatus = 'DRAFT' | 'PUBLISHED' | 'ARCHIVED';

export interface Problem {
  id: number;
  title: string;
  description: string;
  domain: string;
  difficulty: DifficultyLevel;
  projectType: ProjectType;
  impact?: string;
  solutionDirection?: string;
  expectedOutcome?: string;
  status: ProblemStatus;
  createdBy?: number;
  createdByName?: string;
  createdAt: string;
  updatedAt?: string;
  technologies: Technology[];
  tags: Tag[];
  bookmarked?: boolean;
}

export interface PageResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

export interface ProblemFilterParams {
  keyword?: string;
  domain?: string;
  difficulty?: string;
  projectType?: string;
  technology?: string;
  tag?: string;
  status?: string;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDir?: 'ASC' | 'DESC';
}
