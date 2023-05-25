package ru.skypro.lessons.springweb.weblibrary2.service;

import ru.skypro.lessons.springweb.weblibrary2.dto.EmployeeDTO;
import ru.skypro.lessons.springweb.weblibrary2.pojo.Employee;
import ru.skypro.lessons.springweb.weblibrary2.projections.EmployeeFullInfo;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> getAllEmployees();
    void addEmployee(Employee employee);
    void editEmployee(int id, Employee employee);
    EmployeeDTO getEmployeeById(int id);
    void deleteEmployeeById(int id);
    List<EmployeeDTO> getEmployeesWithSalaryHigherThan(int salary);
    EmployeeDTO getEmployeeWithMaxSalary();
    List<EmployeeDTO> getEmployeeswithHighestSalary();
    List<EmployeeDTO> getEmployeeswithPosition(String position);
    List<EmployeeFullInfo> findAllEmployeeFullInfo();
    EmployeeFullInfo getEmployeeByIdFullInfo(int id);
    List<EmployeeDTO> getEmployeeWithPaging(Integer pageIndex, Integer unitPerPage);
}
