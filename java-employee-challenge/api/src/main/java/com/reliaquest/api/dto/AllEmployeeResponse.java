package com.reliaquest.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllEmployeeResponse {
    @JsonProperty("data")
    private List<EmployeeResponse> employeeResponse;
}
