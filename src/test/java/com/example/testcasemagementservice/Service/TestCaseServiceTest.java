package com.example.testcasemagementservice.Service;

import com.example.testcasemagementservice.DTO.AddTestCaseDto;
import com.example.testcasemagementservice.Enums.Priority;
import com.example.testcasemagementservice.Enums.Status;
import com.example.testcasemagementservice.Exceptions.DuplicateResourceException;
import com.example.testcasemagementservice.Exceptions.ResourceNotFoundException;
import com.example.testcasemagementservice.Model.TestCase;
import com.example.testcasemagementservice.Repository.TestCaseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestCaseServiceTest {

    @Mock
    private TestCaseRepository testCaseRepository;

    @InjectMocks
    private TestCaseService testCaseService;

    @Test
    public void createTestCase_WhenTestCaseExists_ThrowsDuplicateResourceException() {
        // Arrange
        AddTestCaseDto testCaseDto = AddTestCaseDto.builder().title("Mock Title").build();
        when(testCaseRepository.existsByTitle(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> testCaseService.createTestCase(testCaseDto));
        verify(testCaseRepository, times(1)).existsByTitle(anyString());
    }

    @Test
    public void createTestCase_WhenTestCaseDoesNotExist_ReturnsTestCase() {
        // Arrange
        AddTestCaseDto testCaseDto = AddTestCaseDto.builder()
                .title("Mock Title")
                .description("Mock Description")
                .priority(Priority.High)
                .status(Status.Pending)
                .build();

        TestCase testCase = TestCase.builder()
                .title("Mock Title")
                .description("Mock Description")
                .priority(Priority.High)
                .status(Status.Pending)
                .build();

        when(testCaseRepository.existsByTitle(anyString())).thenReturn(false);
        when(testCaseRepository.save(any(TestCase.class))).thenReturn(testCase);

        // Act
        TestCase createdTestCase = testCaseService.createTestCase(testCaseDto);

        // Assert
        assertNotNull(createdTestCase);
        assertEquals("Mock Title", createdTestCase.getTitle());
        verify(testCaseRepository, times(1)).save(any(TestCase.class));
    }

    @Test
    public void getTestCaseById_WhenTestCaseExists_ReturnsTestCase() {
        // Arrange
        String id = "Mock Id";
        TestCase testCase = TestCase.builder().id(id).build();
        when(testCaseRepository.findById(id)).thenReturn(Optional.of(testCase));

        // Act
        Optional<TestCase> fetchedTestCase = testCaseService.getTestCaseById(id);

        // Assert
        assertTrue(fetchedTestCase.isPresent());
        assertEquals(id, fetchedTestCase.get().getId());
        verify(testCaseRepository, times(1)).findById(id);
    }

    @Test
    public void getTestCaseById_WhenTestCaseDoesNotExist_ThrowsException() {
        // Arrange
        String id = "Invalid Id";
        when(testCaseRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> testCaseService.getTestCaseById(id));

        // Verify that findById was called exactly once
        verify(testCaseRepository, times(1)).findById(id);
    }


    @Test
    public void updateTestCase_WhenTestCaseExists_ReturnsUpdatedTestCase() {
        // Arrange
        String id = "Mock Id";
        AddTestCaseDto testCaseDto = AddTestCaseDto.builder()
                .title("Mock Title")
                .description("Mock Description")
                .priority(Priority.High)
                .status(Status.Pending)
                .build();

        TestCase existingTestCase = TestCase.builder()
                .id(id)
                .title("Old Title")
                .description("Old Description")
                .priority(Priority.Low)
                .status(Status.InProgress)
                .build();

        TestCase updatedTestCase = TestCase.builder()
                .id(id)
                .title("Mock Title")
                .description("Mock Description")
                .priority(Priority.High)
                .status(Status.Pending)
                .build();

        when(testCaseRepository.findById(id)).thenReturn(Optional.of(existingTestCase));
        when(testCaseRepository.save(any(TestCase.class))).thenReturn(updatedTestCase);

        // Act
        TestCase returnedTestCase = testCaseService.updateTestCase(id, testCaseDto);

        // Assert
        assertNotNull(returnedTestCase);
        assertEquals("Mock Title", returnedTestCase.getTitle());
        assertEquals("Mock Description", returnedTestCase.getDescription());
        verify(testCaseRepository, times(1)).save(any(TestCase.class));
    }

    @Test
    public void updateTestCase_WhenTestCaseDoesNotExist_ThrowsException() {
        // Arrange
        String id = "Invalid Id";
        AddTestCaseDto testCaseDto = AddTestCaseDto.builder().title("Mock Title").build();
        when(testCaseRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> testCaseService.updateTestCase(id, testCaseDto));
        verify(testCaseRepository, times(1)).findById(id);
    }

    @Test
    public void getAllTestCases_WhenTestCasesExist_ReturnsTestCases() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<TestCase> testCases = new ArrayList<>();
        testCases.add(TestCase.builder().title("Mock Title 1").build());
        testCases.add(TestCase.builder().title("Mock Title 2").build());

        Page<TestCase> testCasePage = new PageImpl<>(testCases);
        when(testCaseRepository.findAll(pageable)).thenReturn(testCasePage);

        // Act
        Page<TestCase> fetchedTestCases = testCaseService.getAllTestCases(pageable, null, null);

        // Assert
        assertNotNull(fetchedTestCases);
        assertEquals(2, fetchedTestCases.getTotalElements());
        verify(testCaseRepository, times(1)).findAll(pageable);
    }

    @Test
    public void getAllTestCases_WhenNoTestCasesExist_ReturnsEmptyList() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<TestCase> emptyPage = Page.empty();
        when(testCaseRepository.findAll(pageable)).thenReturn(emptyPage);

        // Act
        Page<TestCase> fetchedTestCases = testCaseService.getAllTestCases(pageable, null, null);

        // Assert
        assertNotNull(fetchedTestCases);
        assertEquals(0, fetchedTestCases.getTotalElements());
    }

    @Test
    public void deleteTestCase_WhenTestCaseExists_DeletesSuccessfully() {
        // Arrange
        String id = "Mock Id";
        TestCase testCase = new TestCase();
        testCase.setId(id);

        when(testCaseRepository.findById(id)).thenReturn(Optional.of(testCase));

        // Act
        testCaseService.deleteTestCase(id);

        // Assert
        verify(testCaseRepository, times(1)).delete(testCase);
    }


    @Test
    public void deleteTestCase_WhenTestCaseDoesNotExist_ThrowsException() {
        // Arrange
        String id = "Invalid Id";
        when(testCaseRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> testCaseService.deleteTestCase(id));

        // Verify that delete was never called
        verify(testCaseRepository, never()).delete(any());
    }

}
