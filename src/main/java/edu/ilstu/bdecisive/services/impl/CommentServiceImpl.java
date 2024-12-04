package edu.ilstu.bdecisive.services.impl;

import edu.ilstu.bdecisive.dtos.CommentDTO;
import edu.ilstu.bdecisive.dtos.CommentRequestDTO;
import edu.ilstu.bdecisive.models.Comment;
import edu.ilstu.bdecisive.models.Review;
import edu.ilstu.bdecisive.models.User;
import edu.ilstu.bdecisive.repositories.CommentRepository;
import edu.ilstu.bdecisive.repositories.ReviewRepository;
import edu.ilstu.bdecisive.services.CommentService;
import edu.ilstu.bdecisive.services.UserService;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserService userService;
    @Autowired
    ReviewRepository productRepository;

    public CommentDTO createComment(CommentRequestDTO request, Long reviewId) throws ServiceException {
        User user = userService.getCurrentUser();

        Review review = productRepository.findById(reviewId)
                .orElseThrow(() -> new ServiceException("Review not found", HttpStatus.NOT_FOUND));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setUser(user);
        comment.setReview(review);

        Comment savedComment = commentRepository.save(comment);
        return convertToDTO(savedComment);
    }

    public List<CommentDTO> getCommentsByReview(Long reviewId) throws ServiceException {
        Review review = productRepository.findById(reviewId)
                .orElseThrow(() -> new ServiceException("Review not found", HttpStatus.NOT_FOUND));
        List<Comment> comments = commentRepository.findByReview(review);
        return comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CommentDTO convertToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setUserId(comment.getUser().getUserId());
        dto.setUserName(comment.getUser().getUsername());
        dto.setReviewId(comment.getReview().getId());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}
