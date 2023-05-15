package ru.skypro.lessons.springboot.weblibrary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.lessons.springboot.weblibrary.pojo.Employee;
import ru.skypro.lessons.springboot.weblibrary.repository.EmployeeRepository;

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
    public int getSalarySum() {
        return getAllEmployees().stream().mapToInt(Employee::getSalary).sum();
    }

    @Override
    public Employee getEmployeeWithMinSalary() {
        return getAllEmployees().stream().min((o1, o2) -> o1.getSalary()-o2.getSalary()).get();
    }

    @Override
    public Employee getEmployeeWithMaxSalary() {
        return getAllEmployees().stream().max((o1, o2) -> o1.getSalary()-o2.getSalary()).get();
    }

    @Override
    public int getAverageSalary() {
        return getSalarySum()/getAllEmployees().size();
    }

    @Override
    public List<Employee> getEmployeesWithHighSalary() {
        return getAllEmployees().stream()
                .filter(employee -> (employee.getSalary() > getAverageSalary()))
                .collect(Collectors.toList());
    }
}
