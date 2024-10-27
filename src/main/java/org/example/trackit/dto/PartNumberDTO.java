package org.example.trackit.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartNumberDTO {

    @NotEmpty(message = "Part number must not be empty")
    @Size(min = 4, max = 25, message = "The part number must be between 4 and 25 symbols")
    private String number;

    @NotEmpty(message = "Description must not be empty")
    @Size(min = 4, max = 50, message = "The description must be between 4 and 50 symbols")
    private String description;

    private String photo;
}

