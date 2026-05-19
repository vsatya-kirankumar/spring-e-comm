package com.ecommerce.project.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {

    @NotBlank
    @Size(min = 5, max= 20)
    private String username;

    @NotBlank
    @Size(min = 5, max= 15)
    private String password;

    @NotBlank
    @Size(max= 50)
    @Email
    private String email;

    private Set<String> role;
}