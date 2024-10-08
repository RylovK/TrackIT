package org.example.trackit.entity.parts;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    private String jobName;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<Equipment> equipment;

}