package org.example.trackit.entity.properties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.trackit.entity.Equipment;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    private String jobName;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<Equipment> equipment;

    public Job(String jobName) {
        this.jobName = jobName;
    }
}