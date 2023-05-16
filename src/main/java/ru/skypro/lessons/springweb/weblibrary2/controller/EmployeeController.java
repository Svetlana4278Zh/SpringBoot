package ru.skypro.lessons.springweb.weblibrary2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.skypro.lessons.springweb.weblibrary2.pojo.Employee;
import ru.skypro.lessons.springweb.weblibrary2.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }
    @PostMapping("/")
    public void addEmployee(@RequestBody Employee employee) {
        employeeService.addEmployee(employee);
    }
    @PutMapping("/{id}")
    public void editEmployee(@PathVariable(value = "id") Integer id, @RequestBody Employee employee){
        employeeService.editEmployee(id, employee);
    }
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable(value = "id") Integer id){
        return employeeService.getEmployeeById(id);
    }
    @DeleteMapping("/{id}")
    public void deleteEmployeeById(@PathVariable(value = "id") Integer id){
        employeeService.deleteEmployeeById(id);
    }
    @GetMapping("/salaryHigherThan")
    public List<Employee> getEmployeesWithSalaryHigherThan(@RequestParam("salary") Integer salary) {
        return employeeService.getEmployeesWithSalaryHigherThan(salary);
    }
}
