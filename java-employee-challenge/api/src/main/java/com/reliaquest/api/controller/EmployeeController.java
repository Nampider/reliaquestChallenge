package com.reliaquest.api.controller;

import com.reliaquest.api.dto.EmployeeInput;
import com.reliaquest.api.dto.EmployeeResponse;
import com.reliaquest.api.processor.EmployeeProcessor;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employee")
@Slf4j
public class EmployeeController implements IEmployeeController<EmployeeResponse, EmployeeInput> {

    private final EmployeeProcessor employeeProcessor;

    public EmployeeController(EmployeeProcessor employeeProcessor) {
        this.employeeProcessor = employeeProcessor;
    }

    @Override
    @GetMapping()
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        return ResponseEntity.ok(employeeProcessor.getAllEmployeesProcessor());
    }

    @Override
    @GetMapping("/search/{searchString}")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByNameSearch(@PathVariable String searchString) {
        log.info("GET Request for this string: {}", searchString);
        return ResponseEntity.ok(employeeProcessor.getEmployeesByNameSearchProcessor(searchString));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable String id) {
        log.info("GET Request for this ID: {}", id);
        return ResponseEntity.ok(employeeProcessor.getEmployeeByIdProcessor(id));
    }

    @Override
    @GetMapping("/highestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return ResponseEntity.ok(employeeProcessor.getHighestSalaryOfEmployeesProcessor());
    }

    @Override
    @GetMapping("/topTenHighestEarningEmployeeNames")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return ResponseEntity.ok(employeeProcessor.getTopTenHighestEarningEmployeeNamesProcessor());
    }

    @Override
    @PostMapping()
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody EmployeeInput employeeInput) {
        log.info("POST request for creating a new employee, name: {}", employeeInput.getName());
        return ResponseEntity.ok(employeeProcessor.createEmployeeProcessor(employeeInput));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        log.info("DELETE request for deleting a new employee, ID: {}", id);
        return ResponseEntity.ok(employeeProcessor.deleteEmployeeProcessor(id));
    }
}
