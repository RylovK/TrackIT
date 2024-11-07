package org.example.trackit.controllers;

import org.example.trackit.security.JWTTokenProvider;
import org.example.trackit.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class JobControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;


    @Autowired
    private JobService jobService;

    @Test
    void getAllJobs() throws Exception {
        String token = jwtTokenProvider.generateToken("user");

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
    void getJobById() {
    }

    @Test
    void createJob() {
    }

    @Test
    void updateJob() {
    }

    @Test
    void deleteJob() {
    }
}