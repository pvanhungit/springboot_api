package com.awesome.controller.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
public class UserInsertDTO {

    @NotBlank(message = "Field is required")
    @Size(max=50)
    private String name;

    @NotBlank(message = "Field is required")
    @Length(min = 6, max = 12,message = "between 6 and 12 characters")
    private String password;

    @Size(max=20)
    private String phoneNumber;

    @Email
    @Size(max=20)
    private String email;

    @Size(max=50)
    private String firstName;

    @Size(max=50)
    private String lastName;

    private String userKeyCloakId;

}
