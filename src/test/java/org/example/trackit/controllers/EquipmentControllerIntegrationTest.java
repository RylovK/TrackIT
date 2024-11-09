package org.example.trackit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.trackit.dto.CreateEquipmentDTO;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.properties.AllocationStatus;
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
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @BeforeEach
    void setUp() {
        token = tokenProvider.generateToken("user");
    }

    @Test
    @Order(1)
    void getAllEquipment_WithPaginationWithoutFilters_ShouldReturnPagedEquipment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/equipment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(lessThanOrEqualTo(10))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].partNumber", is("PN101")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].serialNumber", is("SN001")));
    }

    @Test
    @Order(2)
    void getEquipmentById_ValidId_ShouldReturnEquipment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/equipment/1", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.partNumber", is("PN101")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.serialNumber", is("SN001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.healthStatus", is("RITE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.allocationStatus", is("ON_LOCATION")));
    }

    @Test
    @Order(3)
    void getEquipmentById_InvalidId_ShouldReturn404t() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/equipment/999", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Order(4)
    void createEquipment_ValidDTO_ShouldCreateEquipment() throws Exception {
        CreateEquipmentDTO dto = new CreateEquipmentDTO();
        dto.setPartNumber("PN101");
        dto.setSerialNumber("NEW_EQUIPMENT");

        mockMvc.perform(MockMvcRequestBuilders.post("/equipment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(jacksonObjectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.partNumber", is("PN101")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.serialNumber", is("NEW_EQUIPMENT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.healthStatus", is("RONG")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.allocationStatus", is("ON_BASE")));
    }

    @Test
    @Order(5)
    void createEquipment_InvalidDTO_ShouldReturn400() throws Exception {
        CreateEquipmentDTO dto = new CreateEquipmentDTO();
        dto.setPartNumber("WRONG");
        dto.setSerialNumber("NEW");

        mockMvc.perform(MockMvcRequestBuilders.post("/equipment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(jacksonObjectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(6)
    void updateEquipment_ValidId_InvalidPN_ShouldReturn400() throws Exception {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setPartNumber("WRONG");
        dto.setSerialNumber("SN001");
        dto.setAllocationStatus(AllocationStatus.ON_BASE);

        mockMvc.perform(MockMvcRequestBuilders.patch("/equipment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(jacksonObjectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @Order(7)
    void updateEquipment_ValidId_ValidDTO_ReturnToBase_ShouldUpdateEquipment() throws Exception {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setPartNumber("PN101");
        dto.setSerialNumber("SN001");
        dto.setAllocationStatus(AllocationStatus.ON_BASE);

        mockMvc.perform(MockMvcRequestBuilders.patch("/equipment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(jacksonObjectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.partNumber", is("PN101")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.serialNumber", is("SN001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.healthStatus", is("RONG")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.allocationStatus", is("ON_BASE")));

    }

    @Test
    @Order(8)
    void deleteEquipment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/equipment/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}