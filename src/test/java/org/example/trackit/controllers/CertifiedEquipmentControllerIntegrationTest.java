package org.example.trackit.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.dto.CreateCertifiedEquipmentDTO;
import org.example.trackit.dto.CreateEquipmentDTO;
import org.example.trackit.security.JWTTokenProvider;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CertifiedEquipmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JWTTokenProvider tokenProvider;
    private String token;
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @BeforeEach
    void setUp() {
        token = tokenProvider.generateToken("user");
    }

    @Test
    @Order(1)
    void getAllCertifiedEquipment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/certified")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(lessThanOrEqualTo(10))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].partNumber", is("PN101")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].serialNumber", is("SN001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].certificationStatus", is("VALID")));
    }

    @Test
    @Order(2)
    void testGetAllCertifiedEquipment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/certified/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(lessThanOrEqualTo(2))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].partNumber", is("PN101")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].serialNumber", is("SN001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].certificationStatus", is("VALID")));
    }

    @Test
    @Order(3)
    void createEquipment() throws Exception {
        CreateCertifiedEquipmentDTO dto = new CreateCertifiedEquipmentDTO();
        dto.setPartNumber("PN101");
        dto.setSerialNumber("NEW");

        mockMvc.perform(MockMvcRequestBuilders.post("/certified")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(jacksonObjectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.partNumber", is("PN101")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.serialNumber", is("NEW")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.healthStatus", is("RONG")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.allocationStatus", is("ON_BASE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.certificationStatus", is("EXPIRED")));
    }

    @Test
    @Order(4)
    void updateEquipment() throws Exception {
        CertifiedEquipmentDTO dto = new CertifiedEquipmentDTO();
        dto.setPartNumber("PN102");
        dto.setSerialNumber("SN002");
        dto.setCertificationDate(LocalDate.now());
        dto.setCertificationPeriod(12);
        LocalDate nextCertificationDate = LocalDate.now().plusMonths(12);
        LocalDate now = LocalDate.now();

        mockMvc.perform(MockMvcRequestBuilders.patch("/certified/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(jacksonObjectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.partNumber", is("PN102")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.serialNumber", is("SN002")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.certificationStatus", is("VALID")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.certificationDate", is(now.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nextCertificationDate", is(nextCertificationDate.toString())));

    }
}