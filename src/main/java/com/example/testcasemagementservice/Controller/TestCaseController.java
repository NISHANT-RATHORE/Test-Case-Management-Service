package com.example.testcasemagementservice.Controller;

import com.example.testcasemagementservice.DTO.AddTestCaseDto;
import com.example.testcasemagementservice.Enums.Priority;
import com.example.testcasemagementservice.Enums.Status;
import com.example.testcasemagementservice.Exceptions.InvalidDataException;
import com.example.testcasemagementservice.Exceptions.ResourceNotFoundException;
import com.example.testcasemagementservice.Model.TestCase;
import com.example.testcasemagementservice.Service.TestCaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/testcases")
@Slf4j
public class TestCaseController {

    private final TestCaseService testCaseService;

    public TestCaseController(TestCaseService testCaseService) {
        this.testCaseService = testCaseService;
    }

    @PostMapping
    public ResponseEntity<TestCase> createTestCase(@RequestBody AddTestCaseDto addTestCaseDto){
        log.info("Creating new test case with title: {}", addTestCaseDto.getTitle());
        if(addTestCaseDto.getTitle() == null){
            log.error("Title cannot be null");
            throw new InvalidDataException("Title cannot be null");
        }
        TestCase testCase = testCaseService.createTestCase(addTestCaseDto);
        log.info("Test case created successfully with id: {}", testCase.getId());
        return ResponseEntity.ok(testCase);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestCase> getTestCase(@PathVariable String id){
        log.info("Fetching test case with id: {}", id);
        Optional<TestCase> testCase = testCaseService.getTestCaseById(id);
        if(testCase.isEmpty()){
            log.error("Test case not found with id: {}", id);
            throw new ResourceNotFoundException("Test case not found with id: " + id);
        }
        log.info("Test case fetched successfully with id: {}", id);
        return ResponseEntity.ok(testCase.get());
    }

    @GetMapping
    public ResponseEntity<Page<TestCase>> getAllTestCases(Pageable pageable, @RequestParam(required = false) Status status, @RequestParam(required = false) Priority priority){
        log.info("Fetching all test cases with status: {} and priority: {}", status, priority);
        Page<TestCase> testCases = testCaseService.getAllTestCases(pageable, status, priority);
        if (testCases.isEmpty()) {
            log.error("No test cases found");
            throw new ResourceNotFoundException("No test cases found");
        }
        log.info("Test cases fetched successfully");
        return ResponseEntity.ok(testCases);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestCase> updateTestCase(@PathVariable String id, @RequestBody AddTestCaseDto addTestCaseDto) {
        log.info("Updating test case with id: {}", id);
        TestCase testCase = testCaseService.updateTestCase(id, addTestCaseDto);
        if (testCase == null) {
            log.error("Test case not found with id: {}", id);
            throw new ResourceNotFoundException("Test case not found with id: " + id);
        }
        log.info("Test case updated successfully with id: {}", id);
        return ResponseEntity.ok(testCase);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTestCase(@PathVariable String id) {
        log.info("Deleting test case with id: {}", id);
        boolean deleted = testCaseService.deleteTestCase(id);
        if (!deleted) {
            log.error("Test case not found with id: {}", id);
            throw new ResourceNotFoundException("Test case not found with id: " + id);
        }
        log.info("Test case deleted successfully with id: {}", id);
        return ResponseEntity.ok("Test case deleted successfully");
    }
}