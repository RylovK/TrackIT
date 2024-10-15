package org.example.trackit.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.example.trackit.entity.Equipment;

import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
public class PartNumberDTO {

    @NotEmpty
    private String number;

    @NotEmpty
    private String description;

    private String photo;
}
