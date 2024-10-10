package org.example.trackit.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.trackit.entity.Equipment;

import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
public class PartNumberDTO {

    private String number;

    private String description;

    private String photo;

    private Set<Equipment> equipmentList;

    public PartNumberDTO() {
        equipmentList = new HashSet<>();
    }
}
