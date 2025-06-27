package com.reliaquest.api.processor;

import com.reliaquest.api.dto.DeleteEmployeeRequest;
import com.reliaquest.api.dto.EmployeeInput;
import com.reliaquest.api.dto.EmployeeResponse;
import com.reliaquest.api.mapper.EmployeeMapper;
import com.reliaquest.api.service.EmployeeService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmployeeProcessor {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    public EmployeeProcessor(EmployeeService employeeService, EmployeeMapper employeeMapper) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
    }

    public List<EmployeeResponse> getAllEmployeesProcessor() {
        return employeeService.getAllEmployees().getEmployeeResponse();
    }

    public List<EmployeeResponse> getEmployeesByNameSearchProcessor(String searchString) {
        List<EmployeeResponse> employeeResponseList =
                employeeService.getAllEmployees().getEmployeeResponse();
        return employeeMapper.getEmployeesByNameSearchMapper(employeeResponseList, searchString);
    }

    public EmployeeResponse getEmployeeByIdProcessor(String searchString) {
        return employeeService.getEmployeeById(searchString).getEmployeeResponse();
    }

    public Integer getHighestSalaryOfEmployeesProcessor() {
        // First retrieve the list of employees into a list
        List<EmployeeResponse> employeeResponseList =
                employeeService.getAllEmployees().getEmployeeResponse();
        log.info("Returning the highest salary of all the employees.");
        return employeeMapper.getHighestSalaryOfEmployeesMapper(employeeResponseList);
    }

    public List<String> getTopTenHighestEarningEmployeeNamesProcessor() {
        // First retrieve the list of employees into a List to be sorted later.
        List<EmployeeResponse> employeeResponseList =
                employeeService.getAllEmployees().getEmployeeResponse();
        log.info("Returning the Top Ten employees with the highest salary!");
        return employeeMapper.getTopTenHighestEarningEmployeeNamesMapper(employeeResponseList);
    }

    public EmployeeResponse createEmployeeProcessor(EmployeeInput input) {
        return employeeService.createEmployee(input).getEmployeeResponse();
    }

    public String deleteEmployeeProcessor(String id) {
        String employeeName =
                employeeService.getEmployeeById(id).getEmployeeResponse().getEmployeeName();
        DeleteEmployeeRequest deleteEmployeeRequest = employeeMapper.deleteEmployeeMapper(employeeName);
        return employeeService.deleteEmployee(deleteEmployeeRequest);
    }
}
