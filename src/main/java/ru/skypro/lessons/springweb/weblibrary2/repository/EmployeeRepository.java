package ru.skypro.lessons.springweb.weblibrary2.repository;

import ru.skypro.lessons.springweb.weblibrary2.pojo.Employee;
import java.util.List;

public interface EmployeeRepository {
    public List<Employee> getAllEmployees();
}
