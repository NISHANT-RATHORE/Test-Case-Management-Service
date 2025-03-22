package com.example.testcasemagementservice.Service;

import com.example.testcasemagementservice.DTO.AddTestCaseDto;
import com.example.testcasemagementservice.Enums.Priority;
import com.example.testcasemagementservice.Enums.Status;
import com.example.testcasemagementservice.Exceptions.DuplicateResourceException;
import com.example.testcasemagementservice.Exceptions.ResourceNotFoundException;
import com.example.testcasemagementservice.Mapper.TestCaseMapper;
import com.example.testcasemagementservice.Model.TestCase;
import com.example.testcasemagementservice.Repository.TestCaseRepository;
import com.example.testcasemagementservice.Strategy.PriorityContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TestCaseService {

    private final TestCaseRepository testCaseRepository;
    private final PriorityContext priorityContext = new PriorityContext();

    public TestCaseService(TestCaseRepository testCaseRepository) {
        this.testCaseRepository = testCaseRepository;
    }

    public TestCase createTestCase(AddTestCaseDto addTestCaseDto) {
        if (testCaseRepository.existsByTitle(addTestCaseDto.getTitle())) {
            throw new DuplicateResourceException("Test case already exists with title: " + addTestCaseDto.getTitle());
        }
        priorityContext.setStrategy(addTestCaseDto.getPriority());
        priorityContext.applyStrategy();
        TestCase newTestCase = TestCaseMapper.mapToTest(addTestCaseDto);
        return testCaseRepository.save(newTestCase);
    }

    public Optional<TestCase> getTestCaseById(String id) {
        return Optional.ofNullable(testCaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test case not found with id: " + id)));
    }

    public TestCase updateTestCase(String id, AddTestCaseDto addTestCaseDto) {
        return testCaseRepository.findById(id)
                .map(existingTestCase -> {
                    if (addTestCaseDto.getTitle() != null) {
                        existingTestCase.setTitle(addTestCaseDto.getTitle());
                    }

                    if (addTestCaseDto.getDescription() != null) {
                        existingTestCase.setDescription(addTestCaseDto.getDescription());
                    }

                    if (addTestCaseDto.getPriority() != null) {
                        existingTestCase.setPriority(addTestCaseDto.getPriority());
                    }
                    if (addTestCaseDto.getStatus() != null) {
                        existingTestCase.setStatus(addTestCaseDto.getStatus());
                    }

                    // Save and return updated test case
                    return testCaseRepository.save(existingTestCase);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Test case not found with id: " + id));
    }


    public Page<TestCase> getAllTestCases(Pageable pageable, Status status, Priority priority) {
        if ((status == null) && (priority == null)) {
            return testCaseRepository.findAll(pageable);
        }
        if (status == null) {
            return testCaseRepository.findAllByPriority(pageable, priority);
        } else if (priority == null) {
            return testCaseRepository.findAllByStatus(pageable, status);
        } else {
            return testCaseRepository.findAllByStatusAndPriority(pageable, status, priority);
        }
    }

    public void deleteTestCase(String id) {
        TestCase testCase = testCaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test case not found with id: " + id));

        testCaseRepository.delete(testCase);
    }

}
