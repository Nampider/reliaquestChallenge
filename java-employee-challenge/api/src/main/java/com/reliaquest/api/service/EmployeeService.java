package com.reliaquest.api.service;

import com.reliaquest.api.dto.*;
import com.reliaquest.api.exception.EmployeeNotFoundException;
import com.reliaquest.api.exception.InternalServerException;
import com.reliaquest.api.exception.RateLimitException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class EmployeeService {

    private final RestTemplate restTemplate;

    public EmployeeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "mockEmployeeApi", fallbackMethod = "fallBackForAllEmployees")
    public AllEmployeeResponse getAllEmployees() {
        String url = "http://localhost:8112/api/v1/employee";
        log.info("Returning entire employee list");
        return restTemplate
                .exchange(url, HttpMethod.GET, null, AllEmployeeResponse.class)
                .getBody();
    }

    @CircuitBreaker(name = "mockEmployeeApi", fallbackMethod = "fallBackForSingleEmployee")
    public SingleEmployeeResponse getEmployeeById(String searchId) {
        String url = "http://localhost:8112/api/v1/employee/" + searchId;
        try {
            log.info("Returning single employee information");
            return restTemplate
                    .exchange(url, HttpMethod.GET, null, SingleEmployeeResponse.class)
                    .getBody();
        } catch (HttpStatusCodeException exception) {
            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.info("Employee ID was not found! Please supply a different Id");
                throw new EmployeeNotFoundException("Employee is not found! Please try a different ID!");
            } else if (exception.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                log.info("Employee ID was not found! Perhaps the format is wrong..?");
                throw new InternalServerException("Please double check your parameter value!");
            } else {
                log.info("Too many Requests!");
                throw exception;
            }
        }
    }

    @CircuitBreaker(name = "mockEmployeeApi", fallbackMethod = "fallBackForCreateEmployee")
    public SingleEmployeeResponse createEmployee(EmployeeInput input) {
        String url = "http://localhost:8112/api/v1/employee";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<EmployeeInput> entity = new HttpEntity<>(input, httpHeaders);
        try {
            log.info("Creating a new Employee");
            return restTemplate
                    .exchange(url, HttpMethod.POST, entity, SingleEmployeeResponse.class)
                    .getBody();
        } catch (HttpStatusCodeException exception) {
            if (exception.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                log.info("Something went wrong! Please check your request body information!");
                throw new InternalServerException("Failed to process request. Please double check your request body!");
            } else {
                log.info("Too many Requests!");
                throw exception;
            }
        }
    }

    @CircuitBreaker(name = "mockEmployeeApi", fallbackMethod = "fallBackForDeleteEmployee")
    public String deleteEmployee(DeleteEmployeeRequest deleteEmployeeRequest) {
        String url = "http://localhost:8112/api/v1/employee";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<DeleteEmployeeRequest> entity = new HttpEntity<>(deleteEmployeeRequest, httpHeaders);
        log.info("Deleting an employee: {}", deleteEmployeeRequest.getName());
        restTemplate.exchange(url, HttpMethod.DELETE, entity, Object.class).getBody();
        return deleteEmployeeRequest.getName();
    }

    public AllEmployeeResponse fallBackForAllEmployees(Throwable ex) {
        throw new RateLimitException(
                "Too many requests made to the MockEmployeeAPI to fetch all employees. Circuit breaker will open after a set threshold");
    }

    public SingleEmployeeResponse fallBackForSingleEmployee(Throwable ex) {
        if (ex.getClass() == EmployeeNotFoundException.class) {
            throw new EmployeeNotFoundException(ex.getMessage());
        } else if (ex.getClass() == InternalServerException.class) {
            throw new InternalServerException(ex.getMessage());
        }
        throw new RateLimitException(
                "Too many requests made to the MockEmployeeAPI to fetch a single employee. Circuit breaker will open after a set threshold");
    }

    public String fallBackForDeleteEmployee(Throwable ex) {
        throw new RateLimitException(
                "Too many requests made to the MockEmployeeAPI to delete an employee. Circuit breaker will open after a set threshold");
    }

    public SingleEmployeeResponse fallBackForCreateEmployee(Throwable ex) {
        throw new RateLimitException(
                "Too many requests made to the MockEmployeeAPI to create an employee. Circuit breaker will open after a set threshold");
    }
}
