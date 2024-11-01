package org.example.trackit.repository.specifications;

import jakarta.persistence.criteria.Predicate;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.AllocationStatus;
import org.example.trackit.entity.properties.CertificationStatus;
import org.example.trackit.entity.properties.HealthStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EquipmentSpecifications {

    public static  <T extends Equipment> Specification<T> filter(Map<String, String> filters) {
        return  (root, _, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, String> entry : filters.entrySet()) {

                String key = entry.getKey();
                String value = entry.getValue().toLowerCase();

                if (key.equalsIgnoreCase("partNumber")) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(key)), "%" + value + "%"));
                    continue;
                }
                if (key.equalsIgnoreCase("serialNumber")) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(key)), "%" + value + "%"));
                    continue;
                }
                if (key.equalsIgnoreCase("healthStatus")) {
                    HealthStatus status = HealthStatus.valueOf(value.toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get(key), status));
                    continue;
                }
                if (key.equalsIgnoreCase("allocationStatus")) {
                    AllocationStatus status = AllocationStatus.valueOf(value.toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get(key), status));
                }
                if (key.equalsIgnoreCase("jobName")) {
                    predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("job").get("jobName")), value));
                }
                if (key.equalsIgnoreCase("certificationStatus")) {
                    CertificationStatus status = CertificationStatus.valueOf(value.toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get(key), status));
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
