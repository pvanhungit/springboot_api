package com.awesome.service.comment;

import com.awesome.domain.Comment;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<Comment> findById(Long id);

    List<Comment> findListCommentByPostIdLimit(Pageable pageable, long postId);

}
