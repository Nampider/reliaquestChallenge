package com.reliaquest.api.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.reliaquest.api.dto.EmployeeInput;
import com.reliaquest.api.dto.EmployeeResponse;
import com.reliaquest.api.processor.EmployeeProcessor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class EmployeeControllerTest {

    public EmployeeController employeeController;

    @Mock
    public EmployeeProcessor employeeProcessor;

    static final String employeeName = "";
    static final String employeeId = "";

    @BeforeEach
    public void initMocks() {
        employeeProcessor = Mockito.mock(EmployeeProcessor.class);
        employeeController = new EmployeeController(employeeProcessor);
    }

    @Test
    void getAllEmployees() {
        List<EmployeeResponse> expectedResponse = new ArrayList<>();
        expectedResponse.add(new EmployeeResponse());
        when(employeeProcessor.getAllEmployeesProcessor()).thenReturn(expectedResponse);
        ResponseEntity<List<EmployeeResponse>> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getEmployeesByNameSearch() {
        List<EmployeeResponse> expectedResponse = new ArrayList<>();
        expectedResponse.add(new EmployeeResponse());
        when(employeeProcessor.getEmployeesByNameSearchProcessor(anyString())).thenReturn(expectedResponse);

        ResponseEntity<List<EmployeeResponse>> response = employeeController.getEmployeesByNameSearch(employeeName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getEmployeeById() {
        EmployeeResponse expectedResponse = new EmployeeResponse();
        when(employeeProcessor.getEmployeeByIdProcessor(anyString())).thenReturn(expectedResponse);

        ResponseEntity<EmployeeResponse> response = employeeController.getEmployeeById(employeeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getHighestSalaryOfEmployees() {
        when(employeeProcessor.getHighestSalaryOfEmployeesProcessor()).thenReturn(100000);

        ResponseEntity<Integer> response = employeeController.getHighestSalaryOfEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getTopTenHighestEarningEmployeeNames() {
        List<String> expectedResponse = new ArrayList<>();
        when(employeeProcessor.getTopTenHighestEarningEmployeeNamesProcessor()).thenReturn(expectedResponse);

        ResponseEntity<List<String>> response = employeeController.getTopTenHighestEarningEmployeeNames();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createEmployee() {
        EmployeeInput input = new EmployeeInput();
        input.setAge(24);
        input.setName("Chris");
        input.setSalary(1000000);
        input.setTitle("SE3");

        EmployeeResponse expectedResponse = new EmployeeResponse();
        expectedResponse.setId(UUID.randomUUID());
        expectedResponse.setEmployeeEmail("hellobro@gmail.com");
        expectedResponse.setEmployeeAge(24);
        expectedResponse.setEmployeeName("Chris");
        expectedResponse.setEmployeeSalary(1000000);
        expectedResponse.setEmployeeTitle("SE3");

        when(employeeProcessor.createEmployeeProcessor(input)).thenReturn(expectedResponse);

        ResponseEntity<EmployeeResponse> response = employeeController.createEmployee(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void deleteEmployeeById() {
        when(employeeProcessor.deleteEmployeeProcessor(employeeId)).thenReturn(employeeId);
        ResponseEntity<String> response = employeeController.deleteEmployeeById(employeeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeId, response.getBody());
    }
}
