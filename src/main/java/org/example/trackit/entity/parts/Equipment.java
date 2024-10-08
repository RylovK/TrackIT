package org.example.trackit.entity.parts;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.example.trackit.entity.allocation.Allocation;

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
        @JoinColumn(name = "partnumber_id")
        private PartNumber partNumber;

        @NotEmpty
        private String serialNumber;

        @Enumerated(EnumType.STRING)
        private HealthStatus healthStatus;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "allocation_id")
        private Allocation allocation;

        //@CreationTimestamp TODO:раскомментировать после миграции в бд
        private LocalDateTime createdAt;

}
