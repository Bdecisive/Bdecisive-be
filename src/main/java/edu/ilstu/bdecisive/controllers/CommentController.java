package edu.ilstu.bdecisive.controllers;

import edu.ilstu.bdecisive.dtos.CommentDTO;
import edu.ilstu.bdecisive.dtos.CommentRequestDTO;
import edu.ilstu.bdecisive.services.CommentService;
import edu.ilstu.bdecisive.utils.ServiceException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments/")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/review/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDTO> createComment(@PathVariable Long reviewId,
            @RequestBody @Valid CommentRequestDTO request) throws ServiceException {
        CommentDTO comment = commentService.createComment(request, reviewId);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/review/{reviewId}")
    public ResponseEntity<List<CommentDTO>> getReviewComments(@PathVariable Long reviewId) throws ServiceException {
        List<CommentDTO> comments = commentService.getCommentsByReview(reviewId);
        return ResponseEntity.ok(comments);
    }
}
