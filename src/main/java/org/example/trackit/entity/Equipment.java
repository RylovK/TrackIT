package org.example.trackit.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.example.trackit.entity.properties.AllocationStatus;
import org.example.trackit.entity.properties.HealthStatus;
import org.example.trackit.entity.properties.Job;
import org.example.trackit.entity.properties.PartNumber;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.JOINED)
@Table(uniqueConstraints =
        {@UniqueConstraint(columnNames = {"part_number_id", "serial_Number"})})
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "part_number_id", nullable = false, updatable = false)
    private PartNumber partNumber;

    @NotEmpty
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private HealthStatus healthStatus;

    @Enumerated(EnumType.STRING)
    private AllocationStatus allocationStatus;

    private String comments;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_id")
    private Job job;

    private String lastJob;

    private LocalDate allocationStatusLastModified;

    @CreationTimestamp
    //TODO:раскомментировать после миграции в бд
    private LocalDateTime createdAt;


    public Equipment() {
        healthStatus = HealthStatus.RONG;
        allocationStatus = AllocationStatus.ON_BASE;
    }

    public Equipment(PartNumber partNumber, String serialNumber) {
        this.partNumber = partNumber;
        this.serialNumber = serialNumber;
        healthStatus = HealthStatus.RONG;
        allocationStatus = AllocationStatus.ON_BASE;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Equipment equipment)) return false;

        return getId() == equipment.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }

    public void setPartNumber(PartNumber partNumber) {
        if (this.partNumber != null)
            throw new UnsupportedOperationException("Part number cannot be changed");
        this.partNumber = partNumber;
    }
}

