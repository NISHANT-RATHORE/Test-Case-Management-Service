package com.example.testcasemagementservice.Repository;

import com.example.testcasemagementservice.Enums.Priority;
import com.example.testcasemagementservice.Enums.Status;
import com.example.testcasemagementservice.Model.TestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class TestCaseRepositoryTest {

    @Autowired
    private TestCaseRepository testCaseRepository;

    private TestCase testCase1, testCase2;

    @BeforeEach
    void setUp() {
        testCaseRepository.deleteAll();

        testCase1 = TestCase.builder()
                .title("Test Case 1")
                .description("Sample description 1")
                .priority(Priority.High)
                .status(Status.Pending)
                .build();

        testCase2 = TestCase.builder()
                .title("Test Case 2")
                .description("Sample description 2")
                .priority(Priority.Low)
                .status(Status.InProgress)
                .build();

        testCaseRepository.save(testCase1);
        testCaseRepository.save(testCase2);
    }

    @Test
    void findById_WhenExists_ReturnsTestCase() {
        // Act
        Optional<TestCase> foundTestCase = testCaseRepository.findById(testCase1.getId());

        // Assert
        assertTrue(foundTestCase.isPresent());
        assertEquals("Test Case 1", foundTestCase.get().getTitle());
    }

    @Test
    void findById_WhenDoesNotExist_ReturnsEmpty() {
        // Act
        Optional<TestCase> foundTestCase = testCaseRepository.findById("NonExistentID");

        // Assert
        assertFalse(foundTestCase.isPresent());
    }

    @Test
    void findAll_ReturnsAllTestCases() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<TestCase> testCases = testCaseRepository.findAll(pageable);

        // Assert
        assertEquals(2, testCases.getTotalElements());
    }

    @Test
    void findAllByPriority_ReturnsFilteredResults() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<TestCase> highPriorityCases = testCaseRepository.findAllByPriority(pageable, Priority.High);

        // Assert
        assertEquals(1, highPriorityCases.getTotalElements());
        assertEquals(Priority.High, highPriorityCases.getContent().get(0).getPriority());
    }

    @Test
    void findAllByStatus_ReturnsFilteredResults() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<TestCase> pendingCases = testCaseRepository.findAllByStatus(pageable, Status.Pending);

        // Assert
        assertEquals(1, pendingCases.getTotalElements());
        assertEquals(Status.Pending, pendingCases.getContent().get(0).getStatus());
    }

    @Test
    void existsByTitle_WhenExists_ReturnsTrue() {
        // Act
        boolean exists = testCaseRepository.existsByTitle("Test Case 1");

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByTitle_WhenDoesNotExist_ReturnsFalse() {
        // Act
        boolean exists = testCaseRepository.existsByTitle("Non-Existing Title");

        // Assert
        assertFalse(exists);
    }
}
