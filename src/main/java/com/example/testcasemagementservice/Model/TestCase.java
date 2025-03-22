package com.example.testcasemagementservice.Model;

import com.example.testcasemagementservice.Enums.Priority;
import com.example.testcasemagementservice.Enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(value = "testcases")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestCase {
    @Id
    String id;

    @Indexed(unique = true)
    String title;

    String description;

    @Indexed
    Status status;

    @Indexed
    Priority priority;

    @CreatedDate
    Date createdOn;

    @LastModifiedDate
    Date updatedOn;
}
