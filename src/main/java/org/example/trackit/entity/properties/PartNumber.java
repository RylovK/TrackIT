package org.example.trackit.entity.properties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.example.trackit.entity.Equipment;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Getter
@Setter
public class PartNumber {

    @Id
    @NotEmpty

    private String number;

    @NotEmpty
    private String description;

    private String photo;

    @OneToMany(mappedBy = "partNumber", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Equipment> equipmentList;

    public PartNumber() {
        equipmentList = new HashSet<>();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PartNumber that)) return false;

        return Objects.equals(getNumber(), that.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getNumber());
    }
}
