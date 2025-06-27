package com.reliaquest.api.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.reliaquest.api.dto.DeleteEmployeeRequest;
import com.reliaquest.api.dto.EmployeeResponse;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeMapperTest {

    @InjectMocks
    private EmployeeMapper employeeMapper;

    @Test
    void getEmployeesByNameSearchMapper() {
        EmployeeResponse employee1 = new EmployeeResponse();
        employee1.setEmployeeName("Chris Nam");

        EmployeeResponse employee2 = new EmployeeResponse();
        employee2.setEmployeeName("Monkey D Luffy");

        EmployeeResponse employee3 = new EmployeeResponse();
        employee3.setEmployeeName("Jet Li");

        List<EmployeeResponse> employeeList = List.of(employee1, employee2, employee3);
        String searchString = "Luffy";

        List<EmployeeResponse> response = employeeMapper.getEmployeesByNameSearchMapper(employeeList, searchString);

        assertEquals(1, response.size());
        assertEquals("Monkey D Luffy", response.get(0).getEmployeeName());
    }

    @Test
    void getHighestSalaryOfEmployeesMapper() {
        EmployeeResponse employee1 = new EmployeeResponse();
        employee1.setEmployeeName("Chris Nam");
        employee1.setEmployeeSalary(400);

        EmployeeResponse employee2 = new EmployeeResponse();
        employee2.setEmployeeName("Monkey D Luffy");
        employee2.setEmployeeSalary(100);

        EmployeeResponse employee3 = new EmployeeResponse();
        employee3.setEmployeeName("Jet Li");
        employee3.setEmployeeSalary(200);

        List<EmployeeResponse> employeeList = List.of(employee1, employee2, employee3);

        int response = employeeMapper.getHighestSalaryOfEmployeesMapper(employeeList);

        assertEquals(400, response);
    }

    @Test
    void getTopTenHighestEarningEmployeeNamesMapper() {
        List<EmployeeResponse> employeeList = new ArrayList<>(List.of(
                createEmployee("11", 11),
                createEmployee("10", 10),
                createEmployee("9", 9),
                createEmployee("8", 8),
                createEmployee("7", 7),
                createEmployee("6", 6),
                createEmployee("5", 5),
                createEmployee("4", 4),
                createEmployee("3", 3),
                createEmployee("2", 2),
                createEmployee("1", 1)));

        List<String> expectedTop10 = List.of("11", "10", "9", "8", "7", "6", "5", "4", "3", "2");

        List<String> result = employeeMapper.getTopTenHighestEarningEmployeeNamesMapper(employeeList);

        assertEquals(expectedTop10, result);
    }

    private EmployeeResponse createEmployee(String name, int salary) {
        EmployeeResponse emp = new EmployeeResponse();
        emp.setEmployeeName(name);
        emp.setEmployeeSalary(salary);
        return emp;
    }

    @Test
    void deleteEmployeeMapper() {
        String employeeName = "Chris Nam";

        DeleteEmployeeRequest response = employeeMapper.deleteEmployeeMapper(employeeName);

        assertEquals(employeeName, response.getName());
    }
}
