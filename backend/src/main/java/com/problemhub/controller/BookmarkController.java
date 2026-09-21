package com.problemhub.controller;

import com.problemhub.dto.ProblemResponse;
import com.problemhub.security.ProblemUserPrincipal;
import com.problemhub.service.BookmarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping("/api/problems/{id}/bookmark")
    public ResponseEntity<Map<String, Object>> addBookmark(
            @PathVariable Long id,
            @AuthenticationPrincipal ProblemUserPrincipal principal
    ) {
        bookmarkService.addBookmark(principal.getUserId(), id);
        return ResponseEntity.ok(Map.of("message", "Problem bookmarked successfully", "bookmarked", true));
    }

    @DeleteMapping("/api/problems/{id}/bookmark")
    public ResponseEntity<Map<String, Object>> removeBookmark(
            @PathVariable Long id,
            @AuthenticationPrincipal ProblemUserPrincipal principal
    ) {
        bookmarkService.removeBookmark(principal.getUserId(), id);
        return ResponseEntity.ok(Map.of("message", "Bookmark removed successfully", "bookmarked", false));
    }

    @GetMapping("/api/users/me/bookmarks")
    public ResponseEntity<List<ProblemResponse>> getUserBookmarks(
            @AuthenticationPrincipal ProblemUserPrincipal principal
    ) {
        List<ProblemResponse> bookmarks = bookmarkService.getUserBookmarks(principal.getUserId());
        return ResponseEntity.ok(bookmarks);
    }
}
