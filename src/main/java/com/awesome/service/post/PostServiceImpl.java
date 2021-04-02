package com.awesome.service.post;

import com.awesome.controller.dto.PagingSortDTO;
import com.awesome.controller.dto.PostInsertDTO;
import com.awesome.domain.Post;
import com.awesome.exception.AppException;
import com.awesome.repository.PostRepository;
import com.google.common.base.Strings;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private  PostRepository postRepository;

    @Autowired
    @Lazy
    private Mapper dozerMapper;

    @Override
    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
    }

    @Override
    public List<Post> findPostByTitle(String title) {
        return postRepository.findByTitleIgnoreCaseContaining(title);
    }

    @Override
    public Page<Post> getAllPosts(PagingSortDTO dto) {
        //TODO: move checking for paging and sorting to common or util service for reusable-code
        int pageNum = dto.getPage() > 0 ? dto.getPage() : 0;
        int size = dto.getSize() > 20 ? 20 : dto.getSize();

        Pageable pageOptions = PageRequest.of(pageNum, size, Sort.by("createdAt").descending());

        if (!Strings.isNullOrEmpty(dto.getSortOrder()) && dto.getSortOrder().equals("asc")) {
            pageOptions = PageRequest.of(pageNum, size, Sort.by("createdAt").ascending());
        }

        return postRepository.findAll(pageOptions);
    }

    @Override
    @Transactional
    public void insertPost(PostInsertDTO postInsertDTO) {
        Post post = dozerMapper.map(postInsertDTO, Post.class);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void deletePost(Long id) throws AppException {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "EX20001", Arrays.asList());
        }
        postRepository.deleteById(id);
    }
}
