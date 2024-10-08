package org.example.trackit.entity.allocation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.trackit.entity.parts.Equipment;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
public class Allocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private AllocationStatus status; // On location, on base

    @OneToOne//TODO: переделать
    @JoinColumn(name = "job_id")
    private Job job;

    @OneToMany(mappedBy = "allocation", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Equipment> equipment;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedDate;
}
