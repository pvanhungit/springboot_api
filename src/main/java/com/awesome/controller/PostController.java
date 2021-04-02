package com.awesome.controller;

import com.awesome.controller.dto.PagingSortDTO;
import com.awesome.controller.dto.PostInsertDTO;
import com.awesome.domain.Comment;
import com.awesome.domain.Post;
import com.awesome.exception.AppException;
import com.awesome.service.comment.CommentService;
import com.awesome.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.dozer.Mapper;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    @Lazy
    private Mapper dozerMapper;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/list")
    @Operation(summary = "Get list of all posts paginated")
    @Parameters({
            @Parameter(name = "page", description = "Page you want to retrieve (0..N)",
                    in = ParameterIn.QUERY,
                    schema = @Schema(type = "integer", defaultValue = "0", minimum = "0"), required = true),
            @Parameter(name = "size", description = "Number of records per page.",
                    in = ParameterIn.QUERY,
                    schema = @Schema(type = "integer", defaultValue = "1", minimum = "1"), required = true),
            @Parameter(in = ParameterIn.QUERY, description = "Sorting criteria in the format: property(asc|desc). "
                    + "Default sort order is ascending. " + "Multiple sort criteria are supported.",
                    name = "sortOrder", schema = @Schema(type = "string"))
    })
    public ResponseEntity<Page<Post>> getAllPostsLimit(@Parameter(hidden = true) @PageableDefault Pageable pageable,
                                                       @ParameterObject @Validated PagingSortDTO dto) throws AppException {
        var listResult = postService.getAllPosts(dto);
        return ResponseEntity.ok(listResult);
    }

    @GetMapping("/search")
    @Operation(summary = "Search post by title")
    public ResponseEntity<List<Post>> getPostContainTitle(@RequestParam("title") @NotNull() @NotBlank
                                                          @Validated String title) {
        var listResult = postService.findPostByTitle(title);
        return ResponseEntity.ok(listResult);
    }

    @GetMapping("/comments/{postId}")
    @Operation(summary = "Get all comments of a post paginated")
    @Parameters({
            @Parameter(name = "pageNumber", description = "Page you want to retrieve (0..N)",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "pageSize", description = "Number of records per page.",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "20")),
            @Parameter(in = ParameterIn.QUERY, description = "Sorting criteria in the format: property(asc|desc). "
                    + "Default sort order is ascending. " + "Multiple sort criteria are supported.",
                    name = "sortBy", content = @Content(array = @ArraySchema(schema = @Schema(type = "string"))))
    })
    //TODO: implement the same as get post list
    public ResponseEntity<List<Comment>> getCommentByPostIdLimit(@PathVariable("postId") long postId,
                                                                 @Parameter(hidden = true) Pageable pageable) {
        var listResult = commentService.findListCommentByPostIdLimit(pageable, postId);
        return ResponseEntity.ok(listResult);
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new record")
    public ResponseEntity createPost(@RequestBody @Validated PostInsertDTO postInsertDTO) {
        postService.insertPost(postInsertDTO);
        return ResponseEntity.ok("Post created");
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "Delete a record")
    public ResponseEntity deletePost(@RequestParam Long id) throws AppException {
        postService.deletePost(id);
        return ResponseEntity.ok("Record deleted");
    }
}