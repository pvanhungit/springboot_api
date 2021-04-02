package com.awesome.service.comment;

import com.awesome.domain.Comment;
import com.awesome.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Optional<Comment> findById(Long commentId) {
        return commentRepository.findById(commentId);
    }

    @Override
    public List<Comment> findListCommentByPostIdLimit(Pageable pageable, long postId) {
        Pageable pageTop = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return commentRepository.findByPostId(postId, pageTop);
    }

}
