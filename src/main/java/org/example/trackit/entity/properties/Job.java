package org.example.trackit.entity.properties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
    @Column(unique = true)
    private String jobName;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @OrderBy("partNumber.number")
    private Set<Equipment> equipment;

    public Job(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Job job)) return false;

        return getId() == job.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}