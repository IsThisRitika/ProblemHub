package com.problemhub.service;

import com.problemhub.dto.PageResponse;
import com.problemhub.dto.ProblemRequest;
import com.problemhub.dto.ProblemResponse;
import com.problemhub.exception.BadRequestException;
import com.problemhub.exception.ProblemNotFoundException;
import com.problemhub.model.Problem;
import com.problemhub.model.Tag;
import com.problemhub.model.Technology;
import com.problemhub.repository.ProblemRepository;
import com.problemhub.repository.TagRepository;
import com.problemhub.repository.TechnologyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final TechnologyRepository technologyRepository;
    private final TagRepository tagRepository;

    private static final Set<String> ALLOWED_DIFFICULTIES = Set.of("BEGINNER", "INTERMEDIATE", "ADVANCED");
    private static final Set<String> ALLOWED_PROJECT_TYPES = Set.of("HACKATHON", "MINI_PROJECT", "MAJOR_PROJECT", "FINAL_YEAR_PROJECT");
    private static final Set<String> ALLOWED_STATUSES = Set.of("DRAFT", "PUBLISHED", "ARCHIVED");

    public ProblemService(ProblemRepository problemRepository,
                          TechnologyRepository technologyRepository,
                          TagRepository tagRepository) {
        this.problemRepository = problemRepository;
        this.technologyRepository = technologyRepository;
        this.tagRepository = tagRepository;
    }

    public PageResponse<ProblemResponse> getAllProblems(
            String keyword, String domain, String difficulty,
            String projectType, String technology, String tag,
            String status, int page, int size, String sortBy, String sortDir
    ) {
        int validatedPage = Math.max(0, page);
        int validatedSize = (size > 0 && size <= 100) ? size : 10;
        int offset = validatedPage * validatedSize;

        List<Problem> problems = problemRepository.findWithFilters(
                keyword, domain, difficulty, projectType, technology, tag, status,
                offset, validatedSize, sortBy, sortDir
        );
        long total = problemRepository.countWithFilters(
                keyword, domain, difficulty, projectType, technology, tag, status
        );

        if (problems.isEmpty()) {
            return new PageResponse<>(Collections.emptyList(), validatedPage, validatedSize, total);
        }

        // Batch fetch technologies and tags to avoid N+1 queries
        List<Long> problemIds = problems.stream().map(Problem::getId).toList();
        Map<Long, List<Technology>> techMap = technologyRepository.findByProblemIds(problemIds);
        Map<Long, List<Tag>> tagMap = tagRepository.findByProblemIds(problemIds);

        List<ProblemResponse> responses = problems.stream().map(problem -> {
            ProblemResponse response = toResponse(problem);
            response.setTechnologies(techMap.getOrDefault(problem.getId(), Collections.emptyList()));
            response.setTags(tagMap.getOrDefault(problem.getId(), Collections.emptyList()));
            return response;
        }).toList();

        return new PageResponse<>(responses, validatedPage, validatedSize, total);
    }

    public ProblemResponse getProblemById(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new ProblemNotFoundException(id));

        ProblemResponse response = toResponse(problem);
        response.setTechnologies(technologyRepository.findByProblemId(id));
        response.setTags(tagRepository.findByProblemId(id));
        return response;
    }

    @Transactional
    public ProblemResponse createProblem(ProblemRequest request, Long creatorId) {
        validateEnumValues(request.getDifficulty(), request.getProjectType(), request.getStatus());

        Problem problem = new Problem();
        problem.setTitle(request.getTitle().trim());
        problem.setDescription(request.getDescription().trim());
        problem.setDomain(request.getDomain().trim());
        problem.setDifficulty(request.getDifficulty().toUpperCase());
        problem.setProjectType(request.getProjectType().toUpperCase());
        problem.setImpact(request.getImpact());
        problem.setSolutionDirection(request.getSolutionDirection());
        problem.setExpectedOutcome(request.getExpectedOutcome());
        problem.setStatus(request.getStatus() != null ? request.getStatus().toUpperCase() : "PUBLISHED");
        problem.setCreatedBy(creatorId);

        Problem saved = problemRepository.save(problem);

        if (request.getTechnologyIds() != null && !request.getTechnologyIds().isEmpty()) {
            problemRepository.setProblemTechnologies(saved.getId(), request.getTechnologyIds());
        }
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            problemRepository.setProblemTags(saved.getId(), request.getTagIds());
        }

        return getProblemById(saved.getId());
    }

    @Transactional
    public ProblemResponse updateProblem(Long id, ProblemRequest request) {
        Problem existing = problemRepository.findById(id)
                .orElseThrow(() -> new ProblemNotFoundException(id));

        validateEnumValues(request.getDifficulty(), request.getProjectType(), request.getStatus());

        existing.setTitle(request.getTitle().trim());
        existing.setDescription(request.getDescription().trim());
        existing.setDomain(request.getDomain().trim());
        existing.setDifficulty(request.getDifficulty().toUpperCase());
        existing.setProjectType(request.getProjectType().toUpperCase());
        existing.setImpact(request.getImpact());
        existing.setSolutionDirection(request.getSolutionDirection());
        existing.setExpectedOutcome(request.getExpectedOutcome());
        existing.setStatus(request.getStatus() != null ? request.getStatus().toUpperCase() : existing.getStatus());

        problemRepository.update(existing);

        if (request.getTechnologyIds() != null) {
            problemRepository.setProblemTechnologies(id, request.getTechnologyIds());
        }
        if (request.getTagIds() != null) {
            problemRepository.setProblemTags(id, request.getTagIds());
        }

        return getProblemById(id);
    }

    @Transactional
    public void deleteProblem(Long id) {
        if (!problemRepository.deleteById(id)) {
            throw new ProblemNotFoundException(id);
        }
    }

    @Transactional
    public void archiveProblem(Long id) {
        if (!problemRepository.updateStatus(id, "ARCHIVED")) {
            throw new ProblemNotFoundException(id);
        }
    }

    private void validateEnumValues(String difficulty, String projectType, String status) {
        if (difficulty != null && !ALLOWED_DIFFICULTIES.contains(difficulty.toUpperCase())) {
            throw new BadRequestException("Invalid difficulty: " + difficulty + ". Allowed: " + ALLOWED_DIFFICULTIES);
        }
        if (projectType != null && !ALLOWED_PROJECT_TYPES.contains(projectType.toUpperCase())) {
            throw new BadRequestException("Invalid projectType: " + projectType + ". Allowed: " + ALLOWED_PROJECT_TYPES);
        }
        if (status != null && !ALLOWED_STATUSES.contains(status.toUpperCase())) {
            throw new BadRequestException("Invalid status: " + status + ". Allowed: " + ALLOWED_STATUSES);
        }
    }

    private ProblemResponse toResponse(Problem p) {
        ProblemResponse dto = new ProblemResponse();
        dto.setId(p.getId());
        dto.setTitle(p.getTitle());
        dto.setDescription(p.getDescription());
        dto.setDomain(p.getDomain());
        dto.setDifficulty(p.getDifficulty());
        dto.setProjectType(p.getProjectType());
        dto.setImpact(p.getImpact());
        dto.setSolutionDirection(p.getSolutionDirection());
        dto.setExpectedOutcome(p.getExpectedOutcome());
        dto.setStatus(p.getStatus());
        dto.setCreatedBy(p.getCreatedBy());
        dto.setCreatedAt(p.getCreatedAt());
        dto.setUpdatedAt(p.getUpdatedAt());
        dto.setCreatedByName(problemRepository.findCreatorName(p.getCreatedBy()));
        return dto;
    }
}
