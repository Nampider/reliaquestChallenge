package com.reliaquest.api.processor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import com.reliaquest.api.dto.*;
import com.reliaquest.api.mapper.EmployeeMapper;
import com.reliaquest.api.service.EmployeeService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeProcessorTest {
    @InjectMocks
    public EmployeeProcessor employeeProcessor;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private EmployeeMapper employeeMapper;

    static final String employeeName = "";
    static final String employeeId = "123";

    @Test
    void getAllEmployeesProcessor() {
        AllEmployeeResponse expectedResponse = new AllEmployeeResponse();
        when(employeeService.getAllEmployees()).thenReturn(expectedResponse);
        List<EmployeeResponse> responses = employeeProcessor.getAllEmployeesProcessor();

        assertEquals(expectedResponse.getEmployeeResponse(), responses);
    }

    @Test
    void getEmployeesByNameSearchProcessor() {
        AllEmployeeResponse expectedResponse = new AllEmployeeResponse();
        List<EmployeeResponse> employeeResponseList = new ArrayList<>();
        employeeResponseList.add(new EmployeeResponse());
        expectedResponse.setEmployeeResponse(employeeResponseList);

        when(employeeService.getAllEmployees()).thenReturn(expectedResponse);
        when(employeeMapper.getEmployeesByNameSearchMapper(anyList(), anyString()))
                .thenReturn(employeeResponseList);
        List<EmployeeResponse> responses = employeeProcessor.getEmployeesByNameSearchProcessor(employeeName);

        assertEquals(employeeResponseList, responses);
    }

    @Test
    void getEmployeeByIdProcessor() {
        EmployeeResponse expectedEmployee = new EmployeeResponse();
        expectedEmployee.setId(UUID.fromString("37e742ac-2225-424d-a29f-268177c85b2b"));
        expectedEmployee.setEmployeeName("Chris Nam");
        expectedEmployee.setEmployeeAge(24);

        SingleEmployeeResponse expectedResponse = new SingleEmployeeResponse();
        expectedResponse.setEmployeeResponse(expectedEmployee);

        when(employeeService.getEmployeeById(employeeId)).thenReturn(expectedResponse);

        EmployeeResponse response = employeeProcessor.getEmployeeByIdProcessor(employeeId);

        assertEquals(expectedEmployee, response);
    }

    @Test
    void getHighestSalaryOfEmployeesProcessor() {
        AllEmployeeResponse expectedResponse = new AllEmployeeResponse();
        List<EmployeeResponse> employeeResponseList = new ArrayList<>();
        EmployeeResponse employeeResponse = new EmployeeResponse();
        EmployeeResponse employeeResponse2 = new EmployeeResponse();

        employeeResponse.setId(UUID.fromString("37e742ac-2225-424d-a29f-268177c85b2b"));
        employeeResponse.setEmployeeName("Chris Nam");
        employeeResponse.setEmployeeAge(24);
        employeeResponse.setEmployeeSalary(100);

        employeeResponse2.setId(UUID.fromString("f4555185-369c-4f41-afd5-0c69554e9240"));
        employeeResponse2.setEmployeeName("Monkey D Luffy");
        employeeResponse2.setEmployeeAge(19);
        employeeResponse2.setEmployeeSalary(50);

        employeeResponseList.add(employeeResponse);
        employeeResponseList.add(employeeResponse2);
        expectedResponse.setEmployeeResponse(employeeResponseList);

        when(employeeService.getAllEmployees()).thenReturn(expectedResponse);
        when(employeeMapper.getHighestSalaryOfEmployeesMapper(eq(employeeResponseList)))
                .thenReturn(100);

        Integer salary = employeeProcessor.getHighestSalaryOfEmployeesProcessor();

        assertEquals(100, salary);
    }

    @Test
    void getTopTenHighestEarningEmployeeNamesProcessor() {
        AllEmployeeResponse allEmployeeResponse = new AllEmployeeResponse();
        List<EmployeeResponse> employeeResponseList = new ArrayList<>();
        EmployeeResponse employeeResponse1 = new EmployeeResponse();
        EmployeeResponse employeeResponse2 = new EmployeeResponse();

        employeeResponse1.setId(UUID.fromString("37e742ac-2225-424d-a29f-268177c85b2b"));
        employeeResponse1.setEmployeeName("Chris Nam");
        employeeResponse1.setEmployeeAge(24);
        employeeResponse1.setEmployeeSalary(100);

        employeeResponse2.setId(UUID.fromString("f4555185-369c-4f41-afd5-0c69554e9240"));
        employeeResponse2.setEmployeeName("Monkey D Luffy");
        employeeResponse2.setEmployeeAge(19);
        employeeResponse2.setEmployeeSalary(50);

        employeeResponseList.add(employeeResponse1);
        employeeResponseList.add(employeeResponse2);

        allEmployeeResponse.setEmployeeResponse(employeeResponseList);

        when(employeeService.getAllEmployees()).thenReturn(allEmployeeResponse);
        when(employeeMapper.getTopTenHighestEarningEmployeeNamesMapper(employeeResponseList))
                .thenReturn(List.of("Chris Nam", "Monkey D Luffy"));

        List<String> topTen = employeeProcessor.getTopTenHighestEarningEmployeeNamesProcessor();

        assertEquals(List.of("Chris Nam", "Monkey D Luffy"), topTen);
    }

    @Test
    void createEmployeeProcessor() {
        SingleEmployeeResponse singleEmployeeResponse = new SingleEmployeeResponse();

        EmployeeInput input = new EmployeeInput();
        input.setTitle("SE3");
        input.setAge(24);
        input.setName("Chris Nam");
        input.setSalary(100);

        EmployeeResponse expectedResponse = new EmployeeResponse();
        expectedResponse.setEmployeeSalary(100);
        expectedResponse.setEmployeeEmail("hello@gmail.com");
        expectedResponse.setEmployeeTitle("SE3");
        expectedResponse.setEmployeeName("Chris Nam");
        expectedResponse.setEmployeeAge(24);
        expectedResponse.setId(UUID.randomUUID());

        singleEmployeeResponse.setEmployeeResponse(expectedResponse);

        when(employeeService.createEmployee(eq(input))).thenReturn(singleEmployeeResponse);

        EmployeeResponse response = employeeProcessor.createEmployeeProcessor(input);

        assertEquals(response.getEmployeeName(), expectedResponse.getEmployeeName());
        assertEquals(response.getEmployeeSalary(), expectedResponse.getEmployeeSalary());
        assertEquals(response.getEmployeeTitle(), expectedResponse.getEmployeeTitle());
        assertEquals(response.getEmployeeAge(), expectedResponse.getEmployeeAge());
    }

    @Test
    void deleteEmployeeProcessor() {
        String Id = "f4555185-369c-4f41-afd5-0c69554e9240";
        SingleEmployeeResponse singleEmployeeResponse = new SingleEmployeeResponse();
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setId(UUID.fromString("f4555185-369c-4f41-afd5-0c69554e9240"));
        employeeResponse.setEmployeeName("Chris Nam");
        singleEmployeeResponse.setEmployeeResponse(employeeResponse);

        DeleteEmployeeRequest deleteEmployeeRequest = new DeleteEmployeeRequest();
        deleteEmployeeRequest.setName("Chris Nam");
        when(employeeService.getEmployeeById(Id)).thenReturn(singleEmployeeResponse);
        when(employeeMapper.deleteEmployeeMapper(eq("Chris Nam"))).thenReturn(deleteEmployeeRequest);
        when(employeeService.deleteEmployee(deleteEmployeeRequest)).thenReturn(deleteEmployeeRequest.getName());

        String response = employeeProcessor.deleteEmployeeProcessor(Id);
        assertEquals(response, deleteEmployeeRequest.getName());
    }
}
