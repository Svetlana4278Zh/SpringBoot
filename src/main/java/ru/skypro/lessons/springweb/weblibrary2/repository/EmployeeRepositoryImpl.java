package ru.skypro.lessons.springweb.weblibrary2.repository;

import org.springframework.stereotype.Repository;
import ru.skypro.lessons.springweb.weblibrary2.pojo.Employee;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository{
    private List<Employee> employeeList = new ArrayList<Employee>(List.of(
            new Employee(1,"Катя", 90_000),
            new Employee(2,"Дима", 102_000),
            new Employee(3,"Олег", 80_000),
            new Employee(4,"Вика", 165_000)));

    @Override
    public List<Employee> getAllEmployees() {
        return employeeList;
    }
}
