package com.awesome.repository;

import com.awesome.controller.dto.PostInsertDTO;
import com.awesome.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findById(long postId);

    List<Post> findByTitleIgnoreCaseContaining(String titleSearch);

    Page<Post> findAll(Pageable pageRequest);

    void save(PostInsertDTO postInsertDTO);
}
