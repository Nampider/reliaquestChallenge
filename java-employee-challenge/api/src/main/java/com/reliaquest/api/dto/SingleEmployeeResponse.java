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
public class SingleEmployeeResponse {
    @JsonProperty("data")
    private EmployeeResponse employeeResponse;
}
