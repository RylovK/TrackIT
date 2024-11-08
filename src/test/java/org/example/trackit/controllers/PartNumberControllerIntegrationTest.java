package org.example.trackit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.trackit.dto.PartNumberDTO;
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


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PartNumberControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    private String token;
    private String adminToken;
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @BeforeEach
    void setUp() {
        token = jwtTokenProvider.generateToken("user");
        adminToken = jwtTokenProvider.generateToken("admin");
    }

    @Test
    @Order(1)
    void getAllPartNumbers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/partNumber")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].number", is("PN101")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", is("Description PN101")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].number", is("PN102")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description", is("Description PN102")));
    }

    @Test
    @Order(2)
    void createPartNumber_ValidPartNumber_WithAdminRole_ShouldBeCreated() throws Exception {
        PartNumberDTO dto = new PartNumberDTO();
        dto.setNumber("NEW");
        dto.setDescription("New Description");

        mockMvc.perform(MockMvcRequestBuilders.post("/partNumber")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken)
                        .content(jacksonObjectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.number", is("NEW")));
    }

    @Test
    @Order(3)
    void createPartNumber_InvalidPartNumber_WithAdminRole_ShouldReturnBadRequest() throws Exception {
        PartNumberDTO dto = new PartNumberDTO();
        dto.setNumber("");

        mockMvc.perform(MockMvcRequestBuilders.post("/partNumber")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken)
                        .content(jacksonObjectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(4)
    void createPartNumber_ValidPartNumber_WithUserRole_ShouldReturnForbidden() throws Exception {
        PartNumberDTO dto = new PartNumberDTO();
        dto.setNumber("USER CANNOT DO IT");
        dto.setDescription("New Description");

        mockMvc.perform(MockMvcRequestBuilders.post("/partNumber")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(jacksonObjectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @Order(5)
    void getPartNumber_ValidNumber() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/partNumber/PN101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.number", is("PN101")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("Description PN101")));
    }

    @Test
    @Order(5)
    void getPartNumber_InvalidNumber() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/partNumber/INVALID")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Order(6)
    void updatePartNumber_UpdatedDescription_WithAdminRole_ShouldBeUpdated() throws Exception {
        PartNumberDTO dto = new PartNumberDTO();
        dto.setNumber("PN101");
        dto.setDescription("Updated Description PN101");
        mockMvc.perform(MockMvcRequestBuilders.patch("/partNumber/PN101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken)
                        .content(jacksonObjectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.number", is("PN101")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("Updated Description PN101")));
    }

    @Test
    @Order(7)
    void updatePartNumber_UpdatedNumber_WithAdminRole_ShouldThrowUnsupportedOperationException() throws Exception {
        PartNumberDTO dto = new PartNumberDTO();
        dto.setNumber("Number cannot be changed");
        dto.setDescription("Updated Description PN102");
        mockMvc.perform(MockMvcRequestBuilders.patch("/partNumber/PN102")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken)
                        .content(jacksonObjectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    @Order(8)
    void updatePartNumber_EmptyDescription_WithAdminRole_ShouldReturnBadRequest() throws Exception {
        PartNumberDTO dto = new PartNumberDTO();
        dto.setNumber("PN102");
        dto.setDescription("");
        mockMvc.perform(MockMvcRequestBuilders.patch("/partNumber/PN102")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken)
                        .content(jacksonObjectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updatePartNumber_ValidPartNumber_WithUserRole_ShouldReturnForbidden() throws Exception {
        PartNumberDTO dto = new PartNumberDTO();
        dto.setNumber("PN102");
        dto.setDescription("User cannot change part number");
        mockMvc.perform(MockMvcRequestBuilders.patch("/partNumber/PN102")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(jacksonObjectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @Test
    void deletePartNumber_UserRole_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/partNumber/PN102")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void deletePartNumber_UserAdmin_ShouldBeDeleted() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/partNumber/PN102")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}