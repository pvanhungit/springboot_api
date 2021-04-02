package com.awesome.controller.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserCreateDTO {
    @NotBlank(message = "Field is required")
    @Email
    @Size(max = 20)
    private String email;

    @NotBlank(message = "Field is required")
    @Length(min = 6, max = 12, message = "between 6 and 12 characters")
    private String password;

    @Size(max = 20)
    private String phoneNumber;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;
}
