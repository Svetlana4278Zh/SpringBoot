package ru.skypro.lessons.springweb.weblibrary2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.lessons.springweb.weblibrary2.pojo.Employee;
import ru.skypro.lessons.springweb.weblibrary2.repository.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }

    @Override
    public void addEmployee(Employee employee) {
        getAllEmployees().add(employee);
    }

    @Override
    public void editEmployee(int id, Employee employee) {
        getAllEmployees().set(getIndexById(id), employee);
    }

    @Override
    public Employee getEmployeeById(int id) {
        return getAllEmployees().stream().filter(employee -> employee.getId() == id).findFirst().get();
    }

    @Override
    public void deleteEmployeeById(int id) {
        getAllEmployees().remove(getIndexById(id));
    }

    @Override
    public List<Employee> getEmployeesWithSalaryHigherThan(int salary) {
        return getAllEmployees().stream().filter(employee -> employee.getSalary() > salary).collect(Collectors.toList());
    }

    public int getIndexById(int id){
        return getAllEmployees().indexOf(getEmployeeById(id));
    }
}
