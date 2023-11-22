package com.test.record.domain.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModel {
    @JsonIgnore
    private Long id;
    @NotNull(message = "Name cannot be null")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Name should contain only alphanumeric characters")
    private String name;
    @Email(message = "Email should be valid")
    private String email;
    @NotNull(message = "Password cannot be null")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must be at least 8 characters long and contain at least one digit, one lowercase letter, one uppercase letter, one special character, and no whitespace.")
    private String password;
    @NotNull(message = "User name cannot be null")
    private String userName;

}
