package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import com.reliaquest.api.dto.AllEmployeeResponse;
import com.reliaquest.api.dto.DeleteEmployeeRequest;
import com.reliaquest.api.dto.EmployeeInput;
import com.reliaquest.api.dto.SingleEmployeeResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @InjectMocks
    EmployeeService employeeService;

    @Mock
    RestTemplate restTemplate;

    @Test
    void getAllEmployees() {
        AllEmployeeResponse mockResponse = new AllEmployeeResponse();

        when(restTemplate.exchange(anyString(), any(), any(), eq(AllEmployeeResponse.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));

        AllEmployeeResponse result = employeeService.getAllEmployees();

        assertEquals(mockResponse, result);
    }

    @Test
    void getEmployeeById() {
        String searchId = "f4555185-369c-4f41-afd5-0c69554e9240";
        SingleEmployeeResponse expectedResponse = new SingleEmployeeResponse();

        when(restTemplate.exchange(anyString(), any(), isNull(), eq(SingleEmployeeResponse.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        SingleEmployeeResponse actual = employeeService.getEmployeeById(searchId);

        assertEquals(expectedResponse, actual);
    }

    @Test
    void getEmployeeById_Not_Found() {
        String searchId = "random val";

        when(restTemplate.exchange(anyString(), any(), isNull(), eq(SingleEmployeeResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        try {
            employeeService.getEmployeeById(searchId);
        } catch (RuntimeException e) {
            assertEquals("Employee is not found! Please try a different ID!", e.getMessage());
        }
    }

    @Test
    void getEmployeeById_Rate_Limit() {
        String searchId = "f4555185-369c-4f41-afd5-0c69554e9240";

        when(restTemplate.exchange(anyString(), any(), isNull(), eq(SingleEmployeeResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS));

        try {
            employeeService.getEmployeeById(searchId);
        } catch (RuntimeException e) {
            assertEquals("429 TOO_MANY_REQUESTS", e.getMessage());
        }
    }

    @Test
    void createEmployee() {
        EmployeeInput input = new EmployeeInput();
        SingleEmployeeResponse expectedResponse = new SingleEmployeeResponse();

        ResponseEntity<SingleEmployeeResponse> mockResponse = ResponseEntity.ok(expectedResponse);

        when(restTemplate.exchange(anyString(), any(), any(), eq(SingleEmployeeResponse.class)))
                .thenReturn(mockResponse);

        SingleEmployeeResponse actual = employeeService.createEmployee(input);

        assertEquals(expectedResponse, actual);
    }

    @Test
    void createEmployee_Not_Found() {
        EmployeeInput input = new EmployeeInput();

        when(restTemplate.exchange(anyString(), any(), any(), eq(SingleEmployeeResponse.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        try {
            employeeService.createEmployee(input);
        } catch (RuntimeException e) {
            assertEquals("Failed to process request. Please double check your request body!", e.getMessage());
        }
    }

    @Test
    void createEmployee_Rate_Limit() {
        EmployeeInput input = new EmployeeInput();

        when(restTemplate.exchange(anyString(), any(), any(), eq(SingleEmployeeResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS));

        try {
            employeeService.createEmployee(input);
        } catch (RuntimeException e) {
            assertEquals("429 TOO_MANY_REQUESTS", e.getMessage());
        }
    }

    @Test
    void deleteEmployee() {
        DeleteEmployeeRequest request = new DeleteEmployeeRequest();
        request.setName("Chris Nam");

        ResponseEntity<Object> mockResponse = ResponseEntity.ok().build();

        when(restTemplate.exchange(anyString(), any(), any(), eq(Object.class))).thenReturn(mockResponse);

        String result = employeeService.deleteEmployee(request);

        assertEquals("Chris Nam", result);
    }

    @Test
    void deleteEmployee_Rate_Limit() {
        DeleteEmployeeRequest request = new DeleteEmployeeRequest();
        request.setName("Chris Nam");

        when(restTemplate.exchange(anyString(), any(), any(), eq(Object.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS));

        try {
            employeeService.deleteEmployee(request);
        } catch (RuntimeException e) {
            assertEquals("429 TOO_MANY_REQUESTS", e.getMessage());
        }
    }
}
