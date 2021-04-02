package com.awesome.controller.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserUpdateDTO {
    @NotNull
    private Long clientId;

    @Length(max=20)
    private String phoneNumber;

    @Email
    @Length (max=255)
    @Email(message = "Email is invalid")
    private String email;

    @NotBlank(message = "Field is required")
    @Length (max=50)
    private String firstName;

    @Length (max=50)
    private String lastName;

    @Nullable
    @Min(value=6)
    private String password;
}
