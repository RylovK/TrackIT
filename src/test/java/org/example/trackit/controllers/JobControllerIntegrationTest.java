package org.example.trackit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.trackit.dto.JobResponseDTO;
import org.example.trackit.security.JWTTokenProvider;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JobControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    private String token;
    @Autowired
    private ObjectMapper jacksonObjectMapper;


    @BeforeEach
    void setUp() {
        token = jwtTokenProvider.generateToken("user");
    }

    @Test
    @Order(1)
    void getAllJobs() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/job")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2))) // Ожидаем 2 элемента в ответе
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(1))) // Проверка id первого элемента
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].jobName", is("Job01"))) // Проверка jobName первого элемента
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(2))) // Проверка id второго элемента
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].jobName", is("Job02"))); // Проверка jobName второго элемента
    }

    @Test
    @Order(2)
    void getJobById_ValidId() throws Exception {
        int jobId = 1;

        mockMvc.perform(MockMvcRequestBuilders.get("/job/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(jobId))
                .andExpect(jsonPath("$.jobName").value("Job01"));
    }

    @Test
    @Order(3)
    void getJobById_InvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/job/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());

    }

    @Test
    @Order(4)
    void createJob_Success() throws Exception {
        String newJobName = "Job999";
        JobResponseDTO request = new JobResponseDTO();
        request.setJobName(newJobName);

        mockMvc.perform(MockMvcRequestBuilders.post("/job")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(jacksonObjectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jobName", is(newJobName)));
    }

    @Test
    @Order(5)
    void createJob_DuplicateJobName() throws Exception {
        JobResponseDTO request = new JobResponseDTO();
        request.setJobName("Job01");

        mockMvc.perform(MockMvcRequestBuilders.post("/job")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(jacksonObjectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(6)
    void updateJob() throws Exception {
        JobResponseDTO request = new JobResponseDTO();
        request.setJobName("Updated");

        mockMvc.perform(MockMvcRequestBuilders.patch("/job/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(jacksonObjectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobName", is("Updated")));
    }

    @Test
    @Order(7)
    void deleteJob_ValidId_ShouldDeleteJob() throws Exception {
        String token = jwtTokenProvider.generateToken("admin");

        mockMvc.perform(MockMvcRequestBuilders.delete("/job/3")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    void deleteJob_InvalidId_ShouldReturn404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/job/3")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
}