package com.reliaquest.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeInput {
    @JsonProperty("name")
    private String name;

    @JsonProperty("salary")
    private Integer salary;

    @JsonProperty("age")
    private Integer age;

    @JsonProperty("title")
    private String title;
}
