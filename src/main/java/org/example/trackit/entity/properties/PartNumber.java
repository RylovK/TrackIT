package org.example.trackit.entity.properties;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private String number;

    @OneToMany(mappedBy = "partNumber", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonManagedReference
    private Set<Equipment> equipmentList;

    private String description;

    private String photo;
}
