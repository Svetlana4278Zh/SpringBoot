package ru.skypro.lessons.springboot.weblibrary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.lessons.springboot.weblibrary.pojo.Employee;
import ru.skypro.lessons.springboot.weblibrary.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/salary/sum")
    public int getSalarySum(){
        return employeeService.getSalarySum();
    }
    @GetMapping("/salary/min")
    public Employee getEmployeeWithMinSalary(){
        return employeeService.getEmployeeWithMinSalary();
    }
    @GetMapping("/salary/max")
    public Employee getEmployeeWithMaxSalary(){
        return employeeService.getEmployeeWithMaxSalary();
    }
    @GetMapping("/high-salary")
    public List<Employee> getEmployeesWithHighSalary(){
        return employeeService.getEmployeesWithHighSalary();
    }
}
