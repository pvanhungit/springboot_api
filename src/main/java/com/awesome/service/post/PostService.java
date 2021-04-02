package com.awesome.service.post;

import com.awesome.controller.dto.PagingSortDTO;
import com.awesome.controller.dto.PostInsertDTO;
import com.awesome.domain.Post;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface PostService {

    Optional<Post> findById(Long postId);

    List<Post> findPostByTitle(String title);

    Page<Post> getAllPosts(PagingSortDTO dto);

    void insertPost(PostInsertDTO postInsertDTO);

    void deletePost(Long id);

}
