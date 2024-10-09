package org.example.trackit.entity.properties;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.trackit.entity.Equipment;

import java.util.Set;

@Entity
@Getter
@Setter
public class PartNumber {

    @Id
    private String partNumber;

    @OneToMany(mappedBy = "partNumber", cascade = CascadeType.ALL)
    private Set<Equipment> equipmentList;

    private String description;

    private String photo;
}
