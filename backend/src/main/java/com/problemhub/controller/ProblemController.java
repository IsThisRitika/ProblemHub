package com.problemhub.controller;

import com.problemhub.dto.PageResponse;
import com.problemhub.dto.ProblemRequest;
import com.problemhub.dto.ProblemResponse;
import com.problemhub.service.ProblemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProblemResponse>> getAllProblems(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String domain,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String projectType,
            @RequestParam(required = false) String technology,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        PageResponse<ProblemResponse> response = problemService.getAllProblems(
                keyword, domain, difficulty, projectType, technology, tag, status,
                page, size, sortBy, sortDir
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<ProblemResponse>> searchProblems(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        PageResponse<ProblemResponse> response = problemService.getAllProblems(
                keyword, null, null, null, null, null, null,
                page, size, sortBy, sortDir
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProblemResponse> getProblemById(@PathVariable Long id) {
        ProblemResponse response = problemService.getProblemById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProblemResponse> createProblem(@Valid @RequestBody ProblemRequest request) {
        // In Phase 3, default to user 1 (Admin) until auth context is integrated in Phase 7
        ProblemResponse created = problemService.createProblem(request, 1L);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProblemResponse> updateProblem(
            @PathVariable Long id,
            @Valid @RequestBody ProblemRequest request
    ) {
        ProblemResponse updated = problemService.updateProblem(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProblem(@PathVariable Long id) {
        problemService.deleteProblem(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<Map<String, String>> archiveProblem(@PathVariable Long id) {
        problemService.archiveProblem(id);
        return ResponseEntity.ok(java.util.Map.of("message", "Problem successfully archived", "status", "ARCHIVED"));
    }
}
