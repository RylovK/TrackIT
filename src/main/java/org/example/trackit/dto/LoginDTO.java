package org.example.trackit.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    @Size(min = 4, max = 20, message = "The username must be between 4 and 20 symbols")
    private String username;

    @NotEmpty(message = "The password cannot be empty")
    private String password;
}
