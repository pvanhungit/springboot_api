package com.awesome.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class PagingSortDTO {
    @NotNull
    @Min(0)
    @Max(20)
    private Integer page;

    @NotNull
    @Min(1)
    private Integer size;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sortOrder;
}
