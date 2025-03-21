package com.example.testcasemagementservice.Mapper;

import com.example.testcasemagementservice.DTO.AddTestCaseDto;
import com.example.testcasemagementservice.Model.TestCase;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestCaseMapper {

    public TestCase mapToTest(AddTestCaseDto addTestCaseDto){
        return TestCase.builder()
                .title(addTestCaseDto.getTitle())
                .description(addTestCaseDto.getDescription())
                .priority(addTestCaseDto.getPriority())
                .status(addTestCaseDto.getStatus())
                .build();
    }
}

