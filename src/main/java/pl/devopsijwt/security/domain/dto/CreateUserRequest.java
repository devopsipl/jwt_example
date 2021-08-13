package pl.devopsijwt.security.domain.dto;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import java.util.Set;

@Value
public class CreateUserRequest {

    @NotBlank
    @Email
    String email;
    @NotBlank
    String fullName;
    @NotBlank
    String password;
    @NotBlank
    String rePassword;
    Set<String> authorities;

}
