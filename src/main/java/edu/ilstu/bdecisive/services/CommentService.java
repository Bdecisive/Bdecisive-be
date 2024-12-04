package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.dtos.CommentDTO;
import edu.ilstu.bdecisive.dtos.CommentRequestDTO;
import edu.ilstu.bdecisive.utils.ServiceException;
import jakarta.validation.Valid;

import java.util.List;

public interface CommentService {

    CommentDTO createComment(@Valid CommentRequestDTO request, Long reviewId) throws ServiceException;

    List<CommentDTO> getCommentsByReview(Long reviewId) throws ServiceException;
}
