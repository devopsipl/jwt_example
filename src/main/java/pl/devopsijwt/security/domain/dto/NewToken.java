package pl.devopsijwt.security.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class NewToken {

    @NotBlank
    private String some;
}
