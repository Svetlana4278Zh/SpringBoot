package ru.skypro.lessons.springweb.weblibrary2.service;

import ru.skypro.lessons.springweb.weblibrary2.pojo.Employee;
import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    void addEmployee(Employee employee);
    void editEmployee(int id, Employee employee);
    Employee getEmployeeById(int id);
    void deleteEmployeeById(int id);
    List<Employee> getEmployeesWithSalaryHigherThan(int salary);
}
