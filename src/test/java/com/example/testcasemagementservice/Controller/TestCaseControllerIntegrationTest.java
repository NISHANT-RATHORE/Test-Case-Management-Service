package com.example.testcasemagementservice.Controller;

import com.example.testcasemagementservice.DTO.AddTestCaseDto;
import com.example.testcasemagementservice.Enums.Priority;
import com.example.testcasemagementservice.Enums.Status;
import com.example.testcasemagementservice.Model.TestCase;
import com.example.testcasemagementservice.Repository.TestCaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestCaseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private TestCase testCase;

    @BeforeEach
    void setUp() {
        testCaseRepository.deleteAll();

        testCase = TestCase.builder()
                .title("Integration Test Case")
                .description("Sample integration test description")
                .priority(Priority.High)
                .status(Status.Pending)
                .build();

        testCaseRepository.save(testCase);
    }

    @Test
    void createTestCase_ReturnsCreatedTestCase() throws Exception {
        // Arrange
        AddTestCaseDto newTestCase = AddTestCaseDto.builder()
                .title("New Test Case")
                .description("New test case description")
                .priority(Priority.Medium)
                .status(Status.InProgress)
                .build();

        // Act
        ResultActions response = mockMvc.perform(post("/api/testcases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTestCase)));

        // Assert
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Test Case"));
    }

    @Test
    void getTestCaseById_WhenExists_ReturnsTestCase() throws Exception {
        // Act
        ResultActions response = mockMvc.perform(get("/api/testcases/" + testCase.getId()));

        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testCase.getId()))
                .andExpect(jsonPath("$.title").value("Integration Test Case"));
    }

    @Test
    void getTestCaseById_WhenNotExists_ReturnsNotFound() throws Exception {
        // Act
        ResultActions response = mockMvc.perform(get("/api/testcases/invalid-id"));

        // Assert
        response.andExpect(status().isNotFound());
    }

    @Test
    void getAllTestCases_ReturnsListOfTestCases() throws Exception {
        // Act
        ResultActions response = mockMvc.perform(get("/api/testcases"));

        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    void updateTestCase_WhenExists_ReturnsUpdatedTestCase() throws Exception {
        // Arrange
        AddTestCaseDto updatedTestCase = AddTestCaseDto.builder()
                .title("Updated Title")
                .description("Updated description")
                .priority(Priority.Low)
                .status(Status.Passed)
                .build();

        // Act
        ResultActions response = mockMvc.perform(put("/api/testcases/" + testCase.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTestCase)));

        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.status").value("Passed"));
    }

    @Test
    void deleteTestCase_WhenExists_ReturnsNoContent() throws Exception {
        // Act
        mockMvc.perform(delete("/api/testcases/" + testCase.getId()))
                .andExpect(status().isNoContent());

        // Verify deletion in DB
        assertFalse(testCaseRepository.existsById(testCase.getId()), "Test case was not deleted from the database");
    }

}
