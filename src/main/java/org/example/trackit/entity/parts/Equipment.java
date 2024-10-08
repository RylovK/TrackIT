package org.example.trackit.entity.parts;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.example.trackit.entity.enums.AllocationStatus;
import org.example.trackit.entity.enums.HealthStatus;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class Equipment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "partNumber_id")
        private PartNumber partNumber;

        @NotEmpty
        private String serialNumber;

        @Enumerated(EnumType.STRING)
        @NotEmpty
        private HealthStatus healthStatus;

        @NotEmpty
        private AllocationStatus allocationStatus;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn
        private Job job;

        //@CreationTimestamp TODO:раскомментировать после миграции в бд
        private LocalDateTime createdAt;

        private LocalDateTime allocationStatusLastModified;


        public void setAllocationStatus(AllocationStatus status) {
                if (status == AllocationStatus.ON_BASE)
                        job = null;
                allocationStatusLastModified = LocalDateTime.now();
                this.allocationStatus = status;
        }
}
