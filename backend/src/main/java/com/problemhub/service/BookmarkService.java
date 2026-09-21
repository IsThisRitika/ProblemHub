package com.problemhub.service;

import com.problemhub.dto.ProblemResponse;
import com.problemhub.exception.ProblemNotFoundException;
import com.problemhub.model.Problem;
import com.problemhub.model.Tag;
import com.problemhub.model.Technology;
import com.problemhub.repository.BookmarkRepository;
import com.problemhub.repository.ProblemRepository;
import com.problemhub.repository.TagRepository;
import com.problemhub.repository.TechnologyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final ProblemRepository problemRepository;
    private final TechnologyRepository technologyRepository;
    private final TagRepository tagRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository,
                           ProblemRepository problemRepository,
                           TechnologyRepository technologyRepository,
                           TagRepository tagRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.problemRepository = problemRepository;
        this.technologyRepository = technologyRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public void addBookmark(Long userId, Long problemId) {
        if (problemRepository.findById(problemId).isEmpty()) {
            throw new ProblemNotFoundException(problemId);
        }
        bookmarkRepository.addBookmark(userId, problemId);
    }

    @Transactional
    public void removeBookmark(Long userId, Long problemId) {
        bookmarkRepository.removeBookmark(userId, problemId);
    }

    public boolean isBookmarked(Long userId, Long problemId) {
        return bookmarkRepository.isBookmarked(userId, problemId);
    }

    public List<ProblemResponse> getUserBookmarks(Long userId) {
        List<Problem> problems = bookmarkRepository.findBookmarkedProblems(userId);
        if (problems.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> problemIds = problems.stream().map(Problem::getId).toList();
        Map<Long, List<Technology>> techMap = technologyRepository.findByProblemIds(problemIds);
        Map<Long, List<Tag>> tagMap = tagRepository.findByProblemIds(problemIds);

        return problems.stream().map(p -> {
            ProblemResponse res = new ProblemResponse();
            res.setId(p.getId());
            res.setTitle(p.getTitle());
            res.setDescription(p.getDescription());
            res.setDomain(p.getDomain());
            res.setDifficulty(p.getDifficulty());
            res.setProjectType(p.getProjectType());
            res.setImpact(p.getImpact());
            res.setSolutionDirection(p.getSolutionDirection());
            res.setExpectedOutcome(p.getExpectedOutcome());
            res.setStatus(p.getStatus());
            res.setCreatedBy(p.getCreatedBy());
            res.setCreatedAt(p.getCreatedAt());
            res.setUpdatedAt(p.getUpdatedAt());
            res.setCreatedByName(problemRepository.findCreatorName(p.getCreatedBy()));
            res.setTechnologies(techMap.getOrDefault(p.getId(), Collections.emptyList()));
            res.setTags(tagMap.getOrDefault(p.getId(), Collections.emptyList()));
            res.setBookmarked(true);
            return res;
        }).toList();
    }
}
