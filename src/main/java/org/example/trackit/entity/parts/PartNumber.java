package org.example.trackit.entity.parts;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class PartNumber {

    @Id
    private int partNumber;

    @OneToMany(mappedBy = "partNumber", cascade = CascadeType.ALL)
    private Set<Equipment> equipmentList;

    private String description;

    private String photo;
}
