package pl.devopsijwt.security.domain.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Data
@RequiredArgsConstructor
@Value
public class CreateUserResponse {

    String id;

    String username;
    String fullName;

}
