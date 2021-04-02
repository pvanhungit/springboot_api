package com.awesome.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PostInsertDTO {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "Field is required")
    private Long authorId;

    private Long categoryId;

    @NotBlank(message = "Field is required")
    @NotNull
    private String title;

    @NotBlank(message = "Field is required")
    private String content;

    private String[] img;

    private String[] tags;

}
