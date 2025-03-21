package com.example.testcasemagementservice.Repository;

import com.example.testcasemagementservice.Enums.Priority;
import com.example.testcasemagementservice.Enums.Status;
import com.example.testcasemagementservice.Model.TestCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestCaseRepository extends MongoRepository<TestCase, String> {
    Page<TestCase> findAll(Pageable pageable);

    Optional<TestCase> findById(String id);

    Page<TestCase> findAllByPriority(Pageable pageable, Priority priority);

    Page<TestCase> findAllByStatus(Pageable pageable, Status status);

    Page<TestCase> findAllByStatusAndPriority(Pageable pageable, Status status, Priority priority);
}
