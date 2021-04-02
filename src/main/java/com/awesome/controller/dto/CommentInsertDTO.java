package com.awesome.controller.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
public class CommentInsertDTO {

    @NotNull
    private long postId;

    @NotBlank(message = "Field is required")
    private String content;

    @NotNull
    private long userId;

}
