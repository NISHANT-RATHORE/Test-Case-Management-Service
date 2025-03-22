package com.example.testcasemagementservice.DTO;

import com.example.testcasemagementservice.Enums.Priority;
import com.example.testcasemagementservice.Enums.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddTestCaseDto {
    @NotNull(message = "Title is required")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    String title;

    @Size(max = 500, message = "Description must be less than 500 characters")
    String description;

    Priority priority;

    Status status;
}