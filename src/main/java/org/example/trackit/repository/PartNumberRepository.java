package org.example.trackit.repository;

import org.example.trackit.entity.properties.PartNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartNumberRepository extends JpaRepository<PartNumber, Integer> {

    Optional<PartNumber> findByNumber(String number);
}
