package com.example.testcasemagementservice.DTO;

import com.example.testcasemagementservice.Enums.Priority;
import com.example.testcasemagementservice.Enums.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddTestCaseDto {
    @NotNull(message = "Title is required")
    String title;
    String description;
    Priority priority;
    Status status;
}
