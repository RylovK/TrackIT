package org.example.trackit.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.trackit.entity.properties.AllocationStatus;
import org.example.trackit.entity.properties.HealthStatus;
import org.example.trackit.entity.properties.Job;
import org.example.trackit.entity.properties.PartNumber;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class Equipment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "part_number_id")
        private PartNumber partNumber;

        @NotEmpty
        private String serialNumber;

        @Enumerated(EnumType.STRING)
        private HealthStatus healthStatus;

        private AllocationStatus allocationStatus;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "job_id", unique = true)
        private Job job;

        @CreationTimestamp
        //TODO:раскомментировать после миграции в бд
        private LocalDateTime createdAt;

        private LocalDateTime allocationStatusLastModified;

        public Equipment() {
                healthStatus = HealthStatus.RONG;
                allocationStatus = AllocationStatus.ON_BASE;
                allocationStatusLastModified = LocalDateTime.now();
        }

        public Equipment(PartNumber partNumber, String serialNumber) {
                this.partNumber = partNumber;
                this.serialNumber = serialNumber;
                healthStatus = HealthStatus.RONG;
                allocationStatus = AllocationStatus.ON_BASE;
                allocationStatusLastModified = LocalDateTime.now();
        }

        public void setAllocationStatus(AllocationStatus status) {
                if (status == AllocationStatus.ON_BASE)
                        job = null;
                allocationStatusLastModified = LocalDateTime.now();
                this.allocationStatus = status;
        }
}

