package com.reliaquest.api.mapper;

import com.reliaquest.api.dto.DeleteEmployeeRequest;
import com.reliaquest.api.dto.EmployeeResponse;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    public List<EmployeeResponse> getEmployeesByNameSearchMapper(
            List<EmployeeResponse> employeeResponseList, String searchString) {
        return employeeResponseList.stream()
                .filter(employee -> employee.getEmployeeName().contains(searchString))
                .toList();
    }

    public Integer getHighestSalaryOfEmployeesMapper(List<EmployeeResponse> employeeResponseList) {
        // Utilize stream here for ease of reading. Will look at all the salary element of the users.
        return employeeResponseList.stream()
                .mapToInt(EmployeeResponse::getEmployeeSalary)
                .max()
                .getAsInt(); // Will return the maximum salary value utilizing max()
    }

    public List<String> getTopTenHighestEarningEmployeeNamesMapper(List<EmployeeResponse> employeeResponseList) {
        // first sort the employees in the list by salary. However, we want the first 10 (top 10) so sort them in
        // reverse.
        employeeResponseList.sort(
                Comparator.comparingInt(EmployeeResponse::getEmployeeSalary).reversed());
        return employeeResponseList
                .subList(0, 10)
                .stream() // Since the employees are all sorted, we will return the top 10, 0~10
                .map(EmployeeResponse::getEmployeeName)
                .toList();
    }

    public DeleteEmployeeRequest deleteEmployeeMapper(String employeeName) {
        DeleteEmployeeRequest deleteEmployeeRequest = new DeleteEmployeeRequest();
        deleteEmployeeRequest.setName(employeeName);
        return deleteEmployeeRequest;
    }
}
