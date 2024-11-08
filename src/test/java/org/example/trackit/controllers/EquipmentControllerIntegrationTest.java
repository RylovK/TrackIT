package org.example.trackit.controllers;

import org.example.trackit.security.JWTTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EquipmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JWTTokenProvider tokenProvider;
    private String token;

    @BeforeEach
    void setUp() {
        token = tokenProvider.generateToken("user");
    }

    @Test
    void getAllEquipment_WithPaginationWithoutFilters_ShouldReturnPagedEquipment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/equipment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(lessThanOrEqualTo(10))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].partNumber", is("PN101")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].serialNumber", is("SN001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].partNumber", is("PN102")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].serialNumber", is("SN002")));
    }

    @Test
    void getEquipmentById() {
    }

    @Test
    void createEquipment() {
    }

    @Test
    void updateEquipment() {
    }

    @Test
    void deleteEquipment() {
    }
}