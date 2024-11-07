package org.example.trackit.services.impl;

import org.example.trackit.Mapper.EquipmentMapper;
import org.example.trackit.Mapper.JobMapper;
import org.example.trackit.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class JobServiceImplTest {

    @Mock
    private JobRepository jobRepository;
    @Mock
    private JobMapper jobMapper;
    @Mock
    private EquipmentMapper equipmentMapper;

    @InjectMocks
    private JobServiceImpl jobService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void findAllJobs() {
    }

    @Test
    void findJobById() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}